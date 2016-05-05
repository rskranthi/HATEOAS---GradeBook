package com.appealprocess.appeals.activities;

import com.appealprocess.appeals.model.Identifier;
import com.appealprocess.appeals.model.Appeal;
import com.appealprocess.appeals.model.AppealStatus;
import com.appealprocess.appeals.repositories.AppealRepository;
import com.appealprocess.appeals.representations.AppealRepresentation;

public class CompleteAppealActivity {

    public AppealRepresentation completeAppeal(Identifier id) {
        AppealRepository repository = AppealRepository.current();
        if (repository.has(id)) {
            Appeal appeal = repository.get(id);

            if (appeal.getStatus() == AppealStatus.PROCESSING) {
                appeal.setStatus(AppealStatus.PROCESSED);
            } else if (appeal.getStatus() == AppealStatus.PROCESSED) {
                throw new AppealAlreadyCompletedException();
            } else if (appeal.getStatus() == AppealStatus.UNPROCESSED) {
                throw new CommentsNotProvidedException();
            }

            return new AppealRepresentation(appeal);
        } else {
            throw new NoSuchAppealException();
        }
    }
}
