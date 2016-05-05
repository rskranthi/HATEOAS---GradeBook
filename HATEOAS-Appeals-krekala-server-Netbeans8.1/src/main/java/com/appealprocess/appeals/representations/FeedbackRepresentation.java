package com.appealprocess.appeals.representations;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.joda.time.DateTime;

import com.appealprocess.appeals.model.Comments;

@XmlRootElement(name = "feedback", namespace = Representation.APPEALS_NAMESPACE)
public class FeedbackRepresentation extends Representation {
    
    private static final Logger LOG = LoggerFactory.getLogger(FeedbackRepresentation.class);

    @XmlElement(name = "score", namespace = Representation.APPEALS_NAMESPACE)
    private double scoreChange;
    @XmlElement(name = "processed", namespace = Representation.APPEALS_NAMESPACE)
    private String processedDate;
    
    FeedbackRepresentation(){
        LOG.debug("In FeedbackRepresentation Constructor");
    } // For JAXB :-(
    
    public FeedbackRepresentation(Comments comment, Link appealLink) {
        LOG.info("Creating an Feedback Representation with the comments = {} and links = {}", comment, links);
        
        this.scoreChange = comment.getScoreChange();
        this.processedDate = comment.getCommentsDate().toString();
        this.links = new ArrayList<Link>();
        links.add(appealLink);
        
        LOG.debug("Created the Feedback Representation {}", this);
    }

    public DateTime getProcessedDate() {
        return new DateTime(processedDate);
    }
    
    public double getScoreChange() {
        return scoreChange;
    }

    public Link getAppealLink() {
        return getLinkByName(Representation.RELATIONS_URI + "appeals");
    }
    
    @Override
    public String toString() {
        try {
            JAXBContext context = JAXBContext.newInstance(FeedbackRepresentation.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
