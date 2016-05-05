package com.appealprocess.appeals.activities;

import com.appealprocess.appeals.representations.AppealsUri;

public class UriExchange {

    public static AppealsUri commentsForAppeal(AppealsUri appealUri) {
        checkForValidAppealUri(appealUri);
        return new AppealsUri(appealUri.getBaseUri() + "/comments/" + appealUri.getId().toString());
    }
    
    public static AppealsUri appealForComments(AppealsUri commentsUri) {
        checkForValidCommentsUri(commentsUri);
        return new AppealsUri(commentsUri.getBaseUri() + "/appeals/" + commentsUri.getId().toString());
    }

    public static AppealsUri feedbackForAppeal(AppealsUri commentsUri) {
        checkForValidCommentsUri(commentsUri);
        return new AppealsUri(commentsUri.getBaseUri() + "/feedback/" + commentsUri.getId().toString());
    }
    
    public static AppealsUri appealForFeedback(AppealsUri feedbackUri) {
        checkForValidFeedbackUri(feedbackUri);
        return new AppealsUri(feedbackUri.getBaseUri() + "/appeals/" + feedbackUri.getId().toString());
    }

    private static void checkForValidAppealUri(AppealsUri appealUri) {
        if(!appealUri.toString().contains("/appeals/")) {
            throw new RuntimeException("Invalid Appeal URI");
        }
    }
    
    private static void checkForValidCommentsUri(AppealsUri comments) {
        if(!comments.toString().contains("/comments/")) {
            throw new RuntimeException("Invalid Comments URI");
        }
    }
    
    private static void checkForValidFeedbackUri(AppealsUri feedback) {
        if(!feedback.toString().contains("/feedback/")) {
            throw new RuntimeException("Invalid Feedback URI");
        }
    }
}