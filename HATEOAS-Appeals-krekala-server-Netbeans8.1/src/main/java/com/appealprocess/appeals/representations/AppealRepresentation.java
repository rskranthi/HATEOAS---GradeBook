package com.appealprocess.appeals.representations;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.appealprocess.appeals.activities.InvalidAppealException;
import com.appealprocess.appeals.activities.UriExchange;
import com.appealprocess.appeals.model.Item;
import com.appealprocess.appeals.model.Section;
import com.appealprocess.appeals.model.Appeal;
import com.appealprocess.appeals.model.AppealStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "appeals", namespace = Representation.APPEALS_NAMESPACE)
public class AppealRepresentation extends Representation {
    
    private static final Logger LOG = LoggerFactory.getLogger(AppealRepresentation.class);

    @XmlElement(name = "item", namespace = Representation.APPEALS_NAMESPACE)
    private List<Item> items;
    @XmlElement(name = "section", namespace = Representation.APPEALS_NAMESPACE)
    private Section section;
    @XmlElement(name = "score", namespace = Representation.APPEALS_NAMESPACE)
    private double score;
    @XmlElement(name = "status", namespace = Representation.APPEALS_NAMESPACE)
    private AppealStatus status;

    /**
     * For JAXB :-(
     */
    AppealRepresentation() {
        LOG.debug("In AppealRepresentation Constructor");
    }

    public static AppealRepresentation fromXmlString(String xmlRepresentation) {
        LOG.info("Creating an Appeal object from the XML = {}", xmlRepresentation);
                
        AppealRepresentation appealRepresentation = null;     
        try {
            JAXBContext context = JAXBContext.newInstance(AppealRepresentation.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            appealRepresentation = (AppealRepresentation) unmarshaller.unmarshal(new ByteArrayInputStream(xmlRepresentation.getBytes()));
        } catch (Exception e) {
            throw new InvalidAppealException(e);
        }
        
        LOG.debug("Generated the object {}", appealRepresentation);
        return appealRepresentation;
    }
    
    public static AppealRepresentation createResponseAppealRepresentation(Appeal appeal, AppealsUri appealUri) {
        LOG.info("Creating a Response Appeal for appeal = {} and appeal URI", appeal.toString(), appealUri.toString());
        
        AppealRepresentation appealRepresentation;     
        
        AppealsUri commentsUri = new AppealsUri(appealUri.getBaseUri() + "/comments/" + appealUri.getId().toString());
        LOG.debug("Comments URI = {}", commentsUri);
        
        if(appeal.getStatus() == AppealStatus.UNPROCESSED) {
            LOG.debug("The appeal status is {}", AppealStatus.UNPROCESSED);
            appealRepresentation = new AppealRepresentation(appeal, 
                    new Link(RELATIONS_URI + "cancel", appealUri), 
                    new Link(RELATIONS_URI + "comments", commentsUri), 
                    new Link(RELATIONS_URI + "update", appealUri),
                    new Link(Representation.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.PROCESSING) {
            LOG.debug("The appeal status is {}", AppealStatus.PROCESSING);
            appealRepresentation = new AppealRepresentation(appeal, new Link(Representation.SELF_REL_VALUE, appealUri));
        } else if(appeal.getStatus() == AppealStatus.PROCESSED) {
            LOG.debug("The appeal status is {}", AppealStatus.PROCESSED);
            appealRepresentation = new AppealRepresentation(appeal, new Link(Representation.RELATIONS_URI + "feedback", UriExchange.feedbackForAppeal(commentsUri)));
        } else {
            LOG.debug("The appeal status is in an unknown status");
            throw new RuntimeException("Unknown Appeal Status");
        }
        
        LOG.debug("The appeal representation created for the Create Response Appeal Representation is {}", appealRepresentation);
        
        return appealRepresentation;
    }

    public AppealRepresentation(Appeal appeal, Link... links) {
        LOG.info("Creating an Appeal Representation for appeal = {} and links = {}", appeal.toString(), links.toString());
        
        try {
            this.section = appeal.getSection();
            this.items = appeal.getItems();
            this.status = appeal.getStatus();
            this.score = appeal.calculateTotal();
            this.links = java.util.Arrays.asList(links);
        } catch (Exception ex) {
            throw new InvalidAppealException(ex);
        }
        
        LOG.debug("Created the AppealRepresentation {}", this);
    }

    @Override
    public String toString() {
        LOG.info("Converting Appeal Representation object to string");
        try {
            JAXBContext context = JAXBContext.newInstance(AppealRepresentation.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Appeal getAppeal() {
        LOG.info("Retrieving the Appeal Representation");
        LOG.debug("Section = {}", section);
        LOG.debug("Items = {}", items);
        if (section == null || items == null) {
            throw new InvalidAppealException();
        }
        for (Item i : items) {
            if (i == null) {
                throw new InvalidAppealException();
            }
        }
        
        Appeal appeal = new Appeal(section, status, items);
        
        LOG.debug("Retrieving the Appeal Representation {}", appeal);

        return appeal;
    }

    public Link getCancelLink() {
        LOG.info("Retrieving the Cancel link ");
        return getLinkByName(RELATIONS_URI + "cancel");
    }

    public Link getCommentsLink() {
        LOG.info("Retrieving the Comments link ");
        return getLinkByName(RELATIONS_URI + "comments");
    }

    public Link getUpdateLink() {
        LOG.info("Retrieving the Update link ");
        return getLinkByName(RELATIONS_URI + "update");
    }

    public Link getSelfLink() {
        LOG.info("Retrieving the Self link ");
        return getLinkByName("self");
    }
    
    public AppealStatus getStatus() {
        LOG.info("Retrieving the Appeal status {}", status);
        return status;
    }
}
