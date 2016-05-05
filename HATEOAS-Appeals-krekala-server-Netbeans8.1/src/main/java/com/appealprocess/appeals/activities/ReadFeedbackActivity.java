package com.appealprocess.appeals.activities;

import com.appealprocess.appeals.model.Identifier;
import com.appealprocess.appeals.model.AppealStatus;
import com.appealprocess.appeals.model.Comments;
import com.appealprocess.appeals.repositories.AppealRepository;
import com.appealprocess.appeals.repositories.CommentsRepository;
import com.appealprocess.appeals.representations.Link;
import com.appealprocess.appeals.representations.FeedbackRepresentation;
import com.appealprocess.appeals.representations.Representation;
import com.appealprocess.appeals.representations.AppealsUri;

public class ReadFeedbackActivity {

    public FeedbackRepresentation read(AppealsUri feedbackUri) {
        Identifier identifier = feedbackUri.getId();
        if(!appealHasBeenProcessed(identifier)) {
            throw new CommentsNotProvidedException();
        } else if (AppealRepository.current().has(identifier) && AppealRepository.current().get(identifier).getStatus() == AppealStatus.PROCESSED) {
            throw new AppealAlreadyCompletedException();
        }
        
        Comments payment = CommentsRepository.current().get(identifier);
        
        return new FeedbackRepresentation(payment, new Link(Representation.RELATIONS_URI + "appeals", UriExchange.appealForFeedback(feedbackUri)));
    }

    private boolean appealHasBeenProcessed(Identifier id) {
        return CommentsRepository.current().has(id);
    }

}
