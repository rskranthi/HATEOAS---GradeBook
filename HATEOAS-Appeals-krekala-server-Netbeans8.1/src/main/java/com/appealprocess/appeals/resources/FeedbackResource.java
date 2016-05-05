package com.appealprocess.appeals.resources;

import com.appealprocess.appeals.activities.AppealAlreadyCompletedException;
import com.appealprocess.appeals.activities.CompleteAppealActivity;
import com.appealprocess.appeals.activities.NoSuchAppealException;
import com.appealprocess.appeals.activities.ReadFeedbackActivity;
import com.appealprocess.appeals.model.Identifier;
import com.appealprocess.appeals.representations.AppealRepresentation;
import com.appealprocess.appeals.representations.AppealsUri;
import com.appealprocess.appeals.representations.FeedbackRepresentation;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/feedback")
public class FeedbackResource {
    
    private static final Logger LOG = LoggerFactory.getLogger(FeedbackResource.class);

    private @Context
    UriInfo uriInfo;

    public FeedbackResource() {
        LOG.info("Feedback Resource constructor");
    }

    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * 
     * @param uriInfo
     */
    public FeedbackResource(UriInfo uriInfo) {
        this.uriInfo = uriInfo;

    }

    @GET
    @Path("/{appealId}")
    @Produces("application/vnd-cse564-appeals+xml")
    public Response getFeedback() {
        LOG.info("Retrieving a  Feedback Resource");
        
        Response response;
        
        try {
            FeedbackRepresentation responseRepresentation = new ReadFeedbackActivity().read(new AppealsUri(uriInfo.getRequestUri()));
            response = Response.ok().entity(responseRepresentation).build();
        } catch (AppealAlreadyCompletedException oce) {
            LOG.debug("appeal already completed");
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NoSuchAppealException nsoe) {
            LOG.debug("No such appeal");
            response = Response.status(Status.NOT_FOUND).build();
        }
        
        LOG.debug("The response for the retrieve feedback request is {}", response);
        
        return response;
    }
    
    @DELETE
    @Path("/{appealId}")
    public Response completeAppeal(@PathParam("appealId")String identifier) {
        LOG.info("Retrieving a  Feedback Resource");
        
        Response response;
        
        try {
            AppealRepresentation finalizedAppealRepresentation = new CompleteAppealActivity().completeAppeal(new Identifier(identifier));
            response = Response.ok().entity(finalizedAppealRepresentation).build();
        } catch (AppealAlreadyCompletedException oce) {
            LOG.debug("Appeal already completed");
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NoSuchAppealException nsoe) {
            LOG.debug("No such appeal");
            response = Response.status(Status.NOT_FOUND).build();
        } catch(Exception e){
            LOG.debug("Some unknown exception");
             response = Response.status(Status.NOT_FOUND).build();
        }
        LOG.debug("The response for the delete comments request is {}", response);
        
        return response;
    }
}
