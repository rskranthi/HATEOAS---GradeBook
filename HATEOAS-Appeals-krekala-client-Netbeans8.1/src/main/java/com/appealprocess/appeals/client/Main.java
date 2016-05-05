package com.appealprocess.appeals.client;


import static com.appealprocess.appeals.model.AppealBuilder.appeal;
import com.appealprocess.appeals.model.Appeal;
import com.appealprocess.appeals.model.Comments;
import com.appealprocess.appeals.model.Identifier;
import com.appealprocess.appeals.representations.AppealRepresentation;
import com.appealprocess.appeals.representations.AppealsUri;
import com.appealprocess.appeals.representations.CommentsRepresentation;
import com.appealprocess.appeals.representations.FeedbackRepresentation;
import com.appealprocess.appeals.representations.Link;
import java.net.URI;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import org.apache.log4j.BasicConfigurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    
    
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    
    private static final String APPEALS_MEDIA_TYPE = "application/vnd-cse564-appeals+xml ";
    private static final long ONE_MINUTE = 60000; 
    
    private static final String ENTRY_POINT_URI = "http://localhost:8080/HATEOAS-Appeals-krekala-server-Netbeans8.1/webresources/appeals";

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        URI serviceUri = new URI(ENTRY_POINT_URI);
        happyPathTest(serviceUri);
        abandonedPathTest(serviceUri);
        badStartPathTest(serviceUri);
        badIdPathTest(serviceUri);
    }

    private static void hangAround(long backOffTimeInMillis) {
        try {
            Thread.sleep(backOffTimeInMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void happyPathTest(URI serviceUri) throws Exception {
        LOG.info("Starting Happy Path Test with Service URI {}", serviceUri);
        
        // Create the appeal aka Happy Case
        System.out.println("==========================================================");
        System.out.println("#######################HAPPY CASE#########################");
        System.out.println("==========================================================");
        LOG.info("Step 1. Place the Appeal");
        System.out.println(String.format("About to start happy path test. Placing Appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = appeal().withRandomItems().build();
        System.out.println(appeal);
        LOG.debug("Created base appeal {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.debug("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        System.out.println(appeal);
        // Try to update a different appeal
        /*System.out.println("##BAD START##");
        LOG.info("\n\nStep 2. Try to update a different appeal");
        System.out.println(String.format("About to update an appeal with bad URI [%s] via POST", appealRepresentation.getUpdateLink().getUri().toString() + "/bad-uri"));
        appeal = appeal().withRandomItems().build();
        LOG.debug("Created base appeal {}", appeal);
        Link badLink = new Link("bad", new AppealsUri(appealRepresentation.getSelfLink().getUri().toString() + "/bad-uri"), APPEALS_MEDIA_TYPE);
        LOG.debug("Create bad link {}", badLink);
        ClientResponse badUpdateResponse = client.resource(badLink.getUri()).accept(badLink.getMediaType()).type(badLink.getMediaType()).post(ClientResponse.class, new AppealRepresentation(appeal));
        LOG.debug("Created Bad Update Response {}", badUpdateResponse);
        System.out.println(String.format("Tried to update appeal with bad URI at [%s] via POST, outcome [%d]", badLink.getUri().toString(), badUpdateResponse.getStatus()));
        */
        // Change the appeal
        
        System.out.println("==========================================================");
        System.out.println("#######################FORGOTTEN CASE#########################");
        System.out.println("==========================================================");
        LOG.debug("\n\nStep 3. Change the appeal");
        System.out.println(String.format("About to update appeal at [%s] via POST", appealRepresentation.getUpdateLink().getUri().toString()));
        appeal = appeal().withRandomItems().build();
        LOG.debug("Created base appeal {}", appeal);
        Link updateLink = appealRepresentation.getUpdateLink();
        LOG.debug("Created appeal update link {}", updateLink);
        AppealRepresentation updatedRepresentation = client.resource(updateLink.getUri()).accept(updateLink.getMediaType()).type(updateLink.getMediaType()).post(AppealRepresentation.class, new AppealRepresentation(appeal));
        LOG.debug("Created updated appeal representation link {}", updatedRepresentation);
        System.out.println(String.format("Appeal updated at [%s]", updatedRepresentation.getSelfLink().getUri().toString()));
        // Allow the professor some time to make the change
        LOG.debug("\n\nStep 7. Allow the professor some time to make the change");
        System.out.println("Pausing a while after appeal has been placed, press Enter to Follow-up");
        System.in.read();
        
        System.out.println("==========================================================");
        System.out.println("#######################FORGOTTEN CASE COMPLETED#########################");
        System.out.println("==========================================================");
        
        // Provide comments for the appeal 
        LOG.debug("\n\nStep 4. Add comments for the appeal");
        System.out.println(String.format("About to create a comments resource at [%s] via PUT", updatedRepresentation.getCommentsLink().getUri().toString()));
        Link commentLink = updatedRepresentation.getCommentsLink();
        LOG.debug("Created comments link {} for updated appeal representation {}", commentLink, updatedRepresentation);
        LOG.debug("commentLink.getRelValue() = {}", commentLink.getRelValue());
        LOG.debug("commentLink.getUri() = {}", commentLink.getUri());
        LOG.debug("commentLink.getMediaType() = {}", commentLink.getMediaType());
        Comments comment = new Comments(updatedRepresentation.getScore(), "A.N. Other", "12345677878", 12, 2999);
        LOG.debug("Created new payment object {}", comment);
        CommentsRepresentation  commentRepresentation = client.resource(commentLink.getUri()).accept(commentLink.getMediaType()).type(commentLink.getMediaType()).put(CommentsRepresentation.class, new CommentsRepresentation(comment));        
        LOG.debug("Created new comment representation {}", commentRepresentation);
        System.out.println(String.format("Comments made, feedback at [%s]", commentRepresentation.getFeedbackLink().getUri().toString()));
         
        // Get feedback
        LOG.debug("\n\nStep 5. Get feedback");
        System.out.println(String.format("About to request feedback from [%s] via GET", commentRepresentation.getFeedbackLink().getUri().toString()));
        Link feedbackLink = commentRepresentation.getFeedbackLink();
        LOG.debug("Retrieved the feedback link {} for Comments represntation {}", feedbackLink, commentRepresentation);
        FeedbackRepresentation feedbackRepresentation = client.resource(feedbackLink.getUri()).get(FeedbackRepresentation.class);
        System.out.println(String.format("Comments made, score is [%f]", feedbackRepresentation.getScoreChange()));
        
        // Check on the appeal status
        LOG.debug("\n\nStep 6. Check on the appeal status");
        System.out.println(String.format("About to check appeal status at [%s] via GET", feedbackRepresentation.getAppealLink().getUri().toString()));
        Link appealLink = feedbackRepresentation.getAppealLink();
        AppealRepresentation finalAppealRepresentation = client.resource(appealLink.getUri()).accept(APPEALS_MEDIA_TYPE).get(AppealRepresentation.class);
        System.out.println(String.format("Final appeal placed, current status [%s]", finalAppealRepresentation.getStatus()));
        
        System.out.println("==========================================================");
        System.out.println("#######################HAPPY CASE COMPLETED#########################");
        System.out.println("==========================================================");
        
        
       /* // Abandoned 
       System.out.println("##ABANDONED CASE##");
        LOG.debug("\n\nStep 8. View feedback if possible");
        appeal = appeal().withRandomItems().build();
        appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        Link appealLink1=appealRepresentation.getSelfLink();
        System.out.println(String.format("Trying to view the ready feedback from [%s] via DELETE. Note: the internal state machine must progress the appeal to ready before this should work, otherwise expect a 405 response.", feedbackRepresentation.getAppealLink().getUri().toString()));
        ClientResponse finalResponse = client.resource(appealLink1.getUri()).delete(ClientResponse.class);
        System.out.println(String.format("Tried to delete appeal, HTTP status [%d]", finalResponse.getStatus()));
        if(finalResponse.getStatus() == 200) {
            System.out.println(String.format("Appeal deleted [%s]", finalResponse.getEntity(AppealRepresentation.class).getStatus()));
        }
        */
        /*
        System.out.println("###BAD ID###");
       
        
        LOG.debug("\n\n To check the appeal with bad URI");   
        appeal = appeal().withRandomItems().build();
        client = Client.create();
        LOG.debug("Created client {}", client);
        appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        Identifier identifier=new Identifier();
        LOG.info(appealRepresentation.getSelfLink().getUri().toString());
        Link badLink2 = new Link("bad", new AppealsUri(serviceUri,identifier), APPEALS_MEDIA_TYPE);
        try
        {
        AppealRepresentation badidAppealRepresentation = client.resource(badLink2.getUri()).accept(APPEALS_MEDIA_TYPE).get(AppealRepresentation.class);
        System.out.println(String.format("Final appeal placed, current status [%s]", badidAppealRepresentation.getStatus()));
        }
        catch(Exception e)
        {
        System.out.println("Resource Not Found for given appeal Id "+badLink2.getUri().toString()+" Please look at it and provide the correct appeal ID again,HTTP status [405]");
      
        }
        */
    }
    
    private static void abandonedPathTest(URI serviceUri) throws Exception {
        System.out.println("==========================================================");
        System.out.println("#######################ABANDONED CASE#########################");
        System.out.println("==========================================================");
        
        LOG.info("Step 1. Place the Appeal");
        System.out.println(String.format("About to start abandoned path test. Placing Appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = appeal().withRandomItems().build();
        System.out.println(appeal);
        LOG.debug("Created base appeal {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);
       
        
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        
        LOG.debug("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
       
        System.out.println(appeal);
        System.out.println("Abandoning the appeal as I will it will not be considered");
        Link appealLink1=appealRepresentation.getSelfLink();
        
        ClientResponse finalResponse = client.resource(appealLink1.getUri()).delete(ClientResponse.class);
        System.out.println(String.format("Tried to delete appeal, HTTP status [%d]", finalResponse.getStatus()));
        if(finalResponse.getStatus() == 200) {
            System.out.println(String.format("Appeal deleted [%s]", finalResponse.getEntity(AppealRepresentation.class).getStatus()));
        }
        
        System.out.println("==========================================================");
        System.out.println("#######################ABANDONED CASE COMPLETED#########################");
        System.out.println("==========================================================");
       
    }
    
    private static void badStartPathTest(URI serviceUri) throws Exception {
        
        System.out.println("==========================================================");
        System.out.println("#######################BAD START CASE#########################");
        System.out.println("==========================================================");
    LOG.info("Starting Bad Path Test with Service URI {}", serviceUri);
        
        // Create the appeal 
       
        LOG.info("Step 1. Place the Appeal");
        //System.out.println(String.format("About to start happy path test. Placing Appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = appeal().withRandomItems().build();
        System.out.println(appeal);
        LOG.debug("Created base appeal {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.debug("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        
       
        LOG.info("\n\nStep 2. Try to update a different appeal");
        System.out.println(String.format("About to update an appeal with bad URI [%s] via POST", appealRepresentation.getUpdateLink().getUri().toString() + "/bad-uri"));
        //appeal = appeal().withRandomItems().build();
        //LOG.debug("Created base appeal {}", appeal);
        Link badLink = new Link("bad", new AppealsUri(appealRepresentation.getSelfLink().getUri().toString() + "/bad-uri"), APPEALS_MEDIA_TYPE);
        LOG.debug("Create bad link {}", badLink);
        ClientResponse badUpdateResponse = client.resource(badLink.getUri()).accept(badLink.getMediaType()).type(badLink.getMediaType()).post(ClientResponse.class, new AppealRepresentation(appeal));
        LOG.debug("Created Bad Update Response {}", badUpdateResponse);
        System.out.println(String.format("Tried to update appeal with bad URI at [%s] via POST, outcome [%d]", badLink.getUri().toString(), badUpdateResponse.getStatus()));
         System.out.println("==========================================================");
        System.out.println("#######################BAD START CASE COMPLETED#########################");
        System.out.println("==========================================================");
    }
    
    private static void badIdPathTest(URI serviceUri) throws Exception {
        
        System.out.println("==========================================================");
        System.out.println("#######################BAD ID CASE#########################");
        System.out.println("==========================================================");
    LOG.info("Starting Bad Id Path Test with Service URI {}", serviceUri);
        
        // Create the appeal 
       
        LOG.info("Step 1. Place the Appeal");
        //System.out.println(String.format("About to start happy path test. Placing Appeal at [%s] via POST", serviceUri.toString()));
        Appeal appeal = appeal().withRandomItems().build();
        System.out.println(appeal);
        LOG.debug("Created base appeal {}", appeal);
        Client client = Client.create();
        LOG.debug("Created client {}", client);
        AppealRepresentation appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        LOG.debug("Created appealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeal placed at [%s]", appealRepresentation.getSelfLink().getUri().toString()));
        
       
        //LOG.debug("\n\n To check the appeal with bad ID ");   
       // appeal = appeal().withRandomItems().build();
        client = Client.create();
        //LOG.debug("Created client {}", client);
        appealRepresentation = client.resource(serviceUri).accept(APPEALS_MEDIA_TYPE).type(APPEALS_MEDIA_TYPE).post(AppealRepresentation.class, new ClientAppeal(appeal));
        Identifier identifier=new Identifier();
        LOG.info(appealRepresentation.getSelfLink().getUri().toString());
        Link badLink2 = new Link("bad", new AppealsUri(serviceUri,identifier), APPEALS_MEDIA_TYPE);
        try
        {
        AppealRepresentation badidAppealRepresentation = client.resource(badLink2.getUri()).accept(APPEALS_MEDIA_TYPE).get(AppealRepresentation.class);
        System.out.println(String.format("Final appeal placed, current status [%s]", badidAppealRepresentation.getStatus()));
        }
        catch(Exception e)
        {
        System.out.println("Resource Not Found for given appeal Id "+badLink2.getUri().toString()+" Please look at it and provide the correct appeal ID again");
      
        }
        System.out.println("==========================================================");
        System.out.println("#######################BAD ID CASE#########################");
        System.out.println("==========================================================");
    }
    
    
}
