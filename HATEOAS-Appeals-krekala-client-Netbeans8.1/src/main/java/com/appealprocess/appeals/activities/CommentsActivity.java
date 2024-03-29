package com.appealprocess.appeals.activities;

import com.appealprocess.appeals.model.AppealStatus;
import com.appealprocess.appeals.model.Comments;
import com.appealprocess.appeals.model.Identifier;
import com.appealprocess.appeals.repositories.AppealRepository;
import com.appealprocess.appeals.repositories.CommentsRepository;
import com.appealprocess.appeals.representations.AppealsUri;
import com.appealprocess.appeals.representations.CommentsRepresentation;
import com.appealprocess.appeals.representations.Link;
import com.appealprocess.appeals.representations.Representation;


public class CommentsActivity {
    public CommentsRepresentation comment(Comments comment, AppealsUri commentsUri) {
        Identifier identifier = commentsUri.getId();
        
        // Don't know the Appeal!
        if(!AppealRepository.current().has(identifier)) {
            throw new NoSuchAppealException();
        }
        
        // Already paid
        if(CommentsRepository.current().has(identifier)) {
            throw new UpdateException();
        }
        
        // Business rules - if the payment amount doesn't match the amount outstanding, then reject
        if(AppealRepository.current().get(identifier).calculateTotal()!= comment.getScoreChange()) {
            throw new InvalidCommentsException();
        }
        
        // If we get here, let's create the payment and update the Appeal status
        AppealRepository.current().get(identifier).setStatus(AppealStatus.PROCESSING);
        CommentsRepository.current().store(identifier, comment);
        
        return new CommentsRepresentation(comment, new Link(Representation.RELATIONS_URI + "appeals", UriExchange.appealForComments(commentsUri)),
                new Link(Representation.RELATIONS_URI + "feedback", UriExchange.feedbackForComments(commentsUri)));
    }
}
