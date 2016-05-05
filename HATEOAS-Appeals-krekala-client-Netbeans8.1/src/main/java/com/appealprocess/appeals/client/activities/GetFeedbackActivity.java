package com.appealprocess.appeals.client.activities;

import com.appealprocess.appeals.representations.FeedbackRepresentation;
import java.net.URI;



public class GetFeedbackActivity extends Activity {
    private final URI feedbackUri;
    private FeedbackRepresentation representation;

    public GetFeedbackActivity(URI feedbackUri) {
        this.feedbackUri = feedbackUri;
    }

    public void getFeedbackForAppeal() {
        try {
            representation = binding.retrieveFeedback(feedbackUri);
            actions = new Actions();
            if(representation.getAppealLink() != null) {
                actions.add(new ReadAppealActivity(representation.getAppealLink().getUri()));
            } else {
                actions =  noFurtherActivities();
            }
        } catch (NotFoundException e) {
            actions = noFurtherActivities();
        } catch (ServiceFailureException e) {
            actions = retryCurrentActivity();
        }
    }

    public FeedbackRepresentation getFeedback() {
        return representation;
    }
}
