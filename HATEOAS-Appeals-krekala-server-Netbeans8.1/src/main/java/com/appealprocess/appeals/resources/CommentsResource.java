package com.appealprocess.appeals.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.appealprocess.appeals.activities.InvalidCommentsException;
import com.appealprocess.appeals.activities.NoSuchAppealException;
import com.appealprocess.appeals.activities.CommentsActivity;
import com.appealprocess.appeals.activities.UpdateException;
import com.appealprocess.appeals.model.Identifier;
import com.appealprocess.appeals.representations.Link;
import com.appealprocess.appeals.representations.CommentsRepresentation;
import com.appealprocess.appeals.representations.Representation;
import com.appealprocess.appeals.representations.AppealsUri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/comments/{commentsId}")
public class CommentsResource {
    
    private static final Logger LOG = LoggerFactory.getLogger(CommentsResource.class);
    
    private @Context UriInfo uriInfo;
    
    public CommentsResource(){
        LOG.info("Comments Resource Constructor");
    }
    
    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * @param uriInfo
     */
    public CommentsResource(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @PUT
    @Consumes("application/vnd-cse564-appeals+xml ")
    @Produces("application/vnd-cse564-appeals+xml ")
    public Response comment(CommentsRepresentation commentRepresentation) {
        LOG.info("Making a new comment");
        
        Response response;
        
        try {
            response = Response.created(uriInfo.getRequestUri()).entity(new CommentsActivity().comment(commentRepresentation.getComments(), 
                            new AppealsUri(uriInfo.getRequestUri()))).build();
        } catch(NoSuchAppealException nsoe) {
            LOG.debug("No appeal for comments {}", commentRepresentation);
            response = Response.status(Status.NOT_FOUND).build();
        } catch(UpdateException ue) {
            LOG.debug("Invalid update to comments {}", commentRepresentation);
            Identifier identifier = new AppealsUri(uriInfo.getRequestUri()).getId();
            Link link = new Link(Representation.SELF_REL_VALUE, new AppealsUri(uriInfo.getBaseUri().toString() + "appeals/" + identifier));
            response = Response.status(Status.FORBIDDEN).entity(link).build();
        } catch(InvalidCommentsException ipe) {
            LOG.debug("Invalid Comments for Appeal");
            response = Response.status(Status.BAD_REQUEST).build();
        } catch(Exception e) {
            LOG.debug("Someting when wrong with processing comments");
            response = Response.serverError().build();
        }
        
        LOG.debug("Created the new Comments activity {}", response);
        
        return response;
    }
}
