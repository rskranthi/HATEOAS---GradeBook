package com.appealprocess.appeals.activities;

import com.appealprocess.appeals.model.Identifier;
import com.appealprocess.appeals.model.Appeal;
import com.appealprocess.appeals.repositories.AppealRepository;
import com.appealprocess.appeals.representations.AppealRepresentation;
import com.appealprocess.appeals.representations.AppealsUri;

public class ReadAppealActivity {
    public AppealRepresentation retrieveByUri(AppealsUri appealUri) {
        Identifier identifier  = appealUri.getId();
        
        Appeal appeal = AppealRepository.current().get(identifier);
        
        if(appeal == null) {
            throw new NoSuchAppealException();
        }
        
        return AppealRepresentation.createResponseAppealRepresentation(appeal, appealUri);
    }
}
