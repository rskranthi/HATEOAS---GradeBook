package com.appealprocess.appeals.client.network;

import com.appealprocess.appeals.activities.InvalidCommentsException;
import com.appealprocess.appeals.client.activities.CannotCancelException;
import com.appealprocess.appeals.client.activities.CannotUpdateAppealException;
import com.appealprocess.appeals.client.activities.DuplicateCommentsException;
import com.appealprocess.appeals.client.activities.MalformedAppealException;
import com.appealprocess.appeals.client.activities.NotFoundException;
import com.appealprocess.appeals.client.activities.ServiceFailureException;
import com.appealprocess.appeals.model.Appeal;
import com.appealprocess.appeals.model.Comments;
import static com.appealprocess.appeals.model.CommentsBuilder.comments;
import com.appealprocess.appeals.representations.AppealRepresentation;
import com.appealprocess.appeals.representations.CommentsRepresentation;
import com.appealprocess.appeals.representations.FeedbackRepresentation;
import static com.appealprocess.appeals.representations.Representation.APPEALS_MEDIA_TYPE;

import java.net.URI;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

public class HttpBinding {

    private static final String APPEALS_MEDIA_TYPE = "application/vnd.cse564-appeals+xml ";

    public AppealRepresentation createAppeal(Appeal appeal, URI appealUri) throws MalformedAppealException, ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(appealUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(ClientResponse.class, new AppealRepresentation(appeal));

        int status = response.getStatus();

        if (status == 400) {
            throw new MalformedAppealException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 201) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while creating appeal resource [%s]", status, appealUri.toString()));
    }
    
    public AppealRepresentation retrieveAppeal(URI appealUri) throws NotFoundException, ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(appealUri).accept(APPEALS_MEDIA_TYPE).get(ClientResponse.class);

        int status = response.getStatus();

        if (status == 404) {
            throw new NotFoundException ();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response while retrieving appeal resource [%s]", appealUri.toString()));
    }

    public AppealRepresentation updateAppeal(Appeal appeal, URI appealUri) throws MalformedAppealException, ServiceFailureException, NotFoundException,
            CannotUpdateAppealException {
        Client client = Client.create();
        ClientResponse response = client.resource(appealUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(ClientResponse.class, new AppealRepresentation(appeal));

        int status = response.getStatus();

        if (status == 400) {
            throw new MalformedAppealException();
        } else if (status == 404) {
            throw new NotFoundException();
        } else if (status == 409) {
            throw new CannotUpdateAppealException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while udpating appeal resource [%s]", status, appealUri.toString()));
    }

    public AppealRepresentation deleteAppeal(URI appealUri) throws ServiceFailureException, CannotCancelException, NotFoundException {
        Client client = Client.create();
        ClientResponse response = client.resource(appealUri).accept(APPEALS_MEDIA_TYPE).delete(ClientResponse.class);

        int status = response.getStatus();
        if (status == 404) {
            throw new NotFoundException();
        } else if (status == 405) {
            throw new CannotCancelException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(AppealRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while deleting appeal resource [%s]", status, appealUri.toString()));
    }

    public CommentsRepresentation makeComment(Comments comment, URI commentsUri) throws InvalidCommentsException, NotFoundException, DuplicateCommentsException,
            ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(commentsUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).put(ClientResponse.class, new CommentsRepresentation(comment));

        int status = response.getStatus();
        if (status == 400) {
            throw new InvalidCommentsException();
        } else if (status == 404) {
            throw new NotFoundException();
        } else if (status == 405) {
            throw new DuplicateCommentsException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 201) {
            return response.getEntity(CommentsRepresentation.class);
        }

        throw new RuntimeException(String.format("Unexpected response [%d] while creating comments resource [%s]", status, commentsUri.toString()));
    }

    public FeedbackRepresentation retrieveFeedback(URI feedbackUri) throws NotFoundException, ServiceFailureException {
        Client client = Client.create();
        ClientResponse response = client.resource(feedbackUri).accept(APPEALS_MEDIA_TYPE).get(ClientResponse.class);

        int status = response.getStatus();
        if (status == 404) {
            throw new NotFoundException();
        } else if (status == 500) {
            throw new ServiceFailureException();
        } else if (status == 200) {
            return response.getEntity(FeedbackRepresentation.class);
        }
        
        throw new RuntimeException(String.format("Unexpected response [%d] while retrieving feedback resource [%s]", status, feedbackUri.toString()));
    }
}
