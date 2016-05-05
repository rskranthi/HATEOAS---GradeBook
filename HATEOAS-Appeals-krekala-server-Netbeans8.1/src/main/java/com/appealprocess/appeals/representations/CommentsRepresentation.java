package com.appealprocess.appeals.representations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.appealprocess.appeals.model.Comments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "comments", namespace = Representation.APPEALS_NAMESPACE)
public class CommentsRepresentation extends Representation {
    
    private static final Logger LOG = LoggerFactory.getLogger(CommentsRepresentation.class);
       
    @XmlElement(namespace = CommentsRepresentation.APPEALS_NAMESPACE) private double score;
    @XmlElement(namespace = CommentsRepresentation.APPEALS_NAMESPACE) private String studentName;
    @XmlElement(namespace = CommentsRepresentation.APPEALS_NAMESPACE) private String idNumber;
    @XmlElement(namespace = CommentsRepresentation.APPEALS_NAMESPACE) private int creationMonth;
    @XmlElement(namespace = CommentsRepresentation.APPEALS_NAMESPACE) private int creationYear;
    
    
    /**
     * For JAXB :-(
     */
     CommentsRepresentation(){
        LOG.debug("In CommentsRepresentation Constructor");
     }
    
    public CommentsRepresentation(Comments comment, Link...links) {
        LOG.info("Creating a Comment Representation with the comment = {} and links = {}",comment, links);
        
        score = comment.getScoreChange();
        studentName = comment.getStudentName();
        idNumber = comment.getIdNumber();
        creationMonth = comment.getCreationMonth();
        creationYear = comment.getCreationYear();
        this.links = java.util.Arrays.asList(links);
        
        LOG.debug("Created the Comments Representation {}", this);
    }

    public Comments getComments() {
        return new Comments(score,studentName, idNumber, creationMonth, creationYear);
    }
    
    public Link getFeedbackLink() {
        return getLinkByName(Representation.RELATIONS_URI + "feedback");
    }
    
    public Link getAppealsLink() {
        return getLinkByName(Representation.RELATIONS_URI + "appeals");
    }
}
