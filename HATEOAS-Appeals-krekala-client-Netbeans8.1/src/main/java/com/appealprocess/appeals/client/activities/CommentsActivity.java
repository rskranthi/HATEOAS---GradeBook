package com.appealprocess.appeals.client.activities;

import com.appealprocess.appeals.activities.InvalidCommentsException;
import com.appealprocess.appeals.model.Comments;
import com.appealprocess.appeals.representations.CommentsRepresentation;
import java.net.URI;


public class CommentsActivity extends Activity {

    private final URI commentUri;
    private Comments comment;

    public CommentsActivity(URI commentUri) {
        this.commentUri = commentUri;
    }

    public void commentForAppeal(Comments comment) {        
        try {
            CommentsRepresentation commentRepresentation = binding.makeComment(comment, commentUri);
            actions = new RepresentationHypermediaProcessor().extractNextActionsFromCommentsRepresentation(commentRepresentation);
            comment = commentRepresentation.getComments();
        }  catch (DuplicateCommentsException e) {
            actions = noFurtherActivities();
        } catch (ServiceFailureException e) {
            actions = retryCurrentActivity();            
        }catch (InvalidCommentsException e) {
            actions = retryCurrentActivity();
        }catch (NotFoundException e) {
            actions = noFurtherActivities();
        }
    }
    public Comments getComment() {
        return comment;
    }
}
