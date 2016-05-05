package com.appealprocess.appeals.activities;

import com.appealprocess.appeals.model.Identifier;
import com.appealprocess.appeals.model.Appeal;
import com.appealprocess.appeals.model.AppealStatus;
import com.appealprocess.appeals.repositories.AppealRepository;
import com.appealprocess.appeals.representations.AppealRepresentation;
import com.appealprocess.appeals.representations.AppealsUri;

public class UpdateAppealActivity {
    public AppealRepresentation update(Appeal appeal, AppealsUri appealUri) {
        Identifier appealIdentifier = appealUri.getId();

        AppealRepository repository = AppealRepository.current();
        if (AppealRepository.current().appealNotPlaced(appealIdentifier)) { // Defensive check to see if we have the Appeal
            throw new NoSuchAppealException();
        }

        if (!appealCanBeChanged(appealIdentifier)) {
            throw new UpdateException();
        }

        Appeal storedAppeal = repository.get(appealIdentifier);
        
        storedAppeal.setStatus(storedAppeal.getStatus());
        storedAppeal.calculateTotal();


        return AppealRepresentation.createResponseAppealRepresentation(storedAppeal, appealUri); 
    }
    
    private boolean appealCanBeChanged(Identifier identifier) {
        return AppealRepository.current().get(identifier).getStatus() == AppealStatus.UNPROCESSED;
    }
}
