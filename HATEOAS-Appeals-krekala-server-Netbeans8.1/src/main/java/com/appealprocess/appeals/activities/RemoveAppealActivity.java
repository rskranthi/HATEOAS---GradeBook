package com.appealprocess.appeals.activities;

import com.appealprocess.appeals.model.Identifier;
import com.appealprocess.appeals.model.Appeal;
import com.appealprocess.appeals.model.AppealStatus;
import com.appealprocess.appeals.repositories.AppealRepository;
import com.appealprocess.appeals.representations.AppealRepresentation;
import com.appealprocess.appeals.representations.AppealsUri;

public class RemoveAppealActivity {
    public AppealRepresentation delete(AppealsUri appealUri) {
        // Discover the URI of the Appeal that has been cancelled
        
        Identifier identifier = appealUri.getId();

        AppealRepository appealRepository = AppealRepository.current();

        if (appealRepository.appealNotPlaced(identifier)) {
            throw new NoSuchAppealException();
        }

        Appeal appeal = appealRepository.get(identifier);

        // Can't delete a ready or preparing Appeal
        if (appeal.getStatus() == AppealStatus.PROCESSING || appeal.getStatus() == AppealStatus.PROCESSED) {
            throw new AppealDeletionException();
        }

        if(appeal.getStatus() == AppealStatus.UNPROCESSED) {
            appealRepository.remove(identifier);
        }

        return new AppealRepresentation(appeal);
    }

}
