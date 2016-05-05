package com.appealprocess.appeals.activities;

import com.appealprocess.appeals.model.Identifier;
import com.appealprocess.appeals.model.Appeal;
import com.appealprocess.appeals.model.AppealStatus;
import com.appealprocess.appeals.repositories.AppealRepository;
import com.appealprocess.appeals.representations.Link;
import com.appealprocess.appeals.representations.AppealRepresentation;
import com.appealprocess.appeals.representations.Representation;
import com.appealprocess.appeals.representations.AppealsUri;

public class CreateAppealActivity {
    public AppealRepresentation create(Appeal appeal, AppealsUri requestUri) {
        appeal.setStatus(AppealStatus.UNPROCESSED);
                
        Identifier identifier = AppealRepository.current().store(appeal);
        
        AppealsUri appealUri = new AppealsUri(requestUri.getBaseUri() + "/appeals/" + identifier.toString());
        AppealsUri commentsUri = new AppealsUri(requestUri.getBaseUri() + "/comments/" + identifier.toString());
        return new AppealRepresentation(appeal, 
                new Link(Representation.RELATIONS_URI + "cancel", appealUri), 
                new Link(Representation.RELATIONS_URI + "comments", commentsUri), 
                new Link(Representation.RELATIONS_URI + "update", appealUri),
                new Link(Representation.SELF_REL_VALUE, appealUri));
    }
}
