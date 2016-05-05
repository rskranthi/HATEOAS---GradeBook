package com.appealprocess.appeals.client.activities;

import com.appealprocess.appeals.representations.AppealRepresentation;
import com.appealprocess.appeals.representations.CommentsRepresentation;

class RepresentationHypermediaProcessor {

    Actions extractNextActionsFromAppealRepresentation(AppealRepresentation representation) {
        Actions actions = new Actions();

        if (representation != null) {

            if (representation.getCommentsLink() != null) {
                actions.add(new CommentsActivity(representation.getCommentsLink().getUri()));
            }

            if (representation.getUpdateLink() != null) {
                actions.add(new UpdateAppealActivity(representation.getUpdateLink().getUri()));
            }

            if (representation.getSelfLink() != null) {
                actions.add(new ReadAppealActivity(representation.getSelfLink().getUri()));
            }

            if (representation.getCancelLink() != null) {
                actions.add(new CancelAppealActivity(representation.getCancelLink().getUri()));
            }
        }

        return actions;
    }

    public Actions extractNextActionsFromCommentsRepresentation(CommentsRepresentation representation) {
        Actions actions = new Actions();
        
        if(representation.getAppealsLink() != null) {
            actions.add(new ReadAppealActivity(representation.getAppealsLink().getUri()));
        }
        
        if(representation.getFeedbackLink() != null) {
            actions.add(new GetFeedbackActivity(representation.getFeedbackLink().getUri()));
        }
        
        return actions;
    }

}
