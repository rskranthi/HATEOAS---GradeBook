package com.appealprocess.appeals.client;

import com.appealprocess.appeals.model.Appeal;
import com.appealprocess.appeals.model.AppealStatus;
import com.appealprocess.appeals.model.Item;
import com.appealprocess.appeals.model.Section;
import com.appealprocess.appeals.representations.Representation;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "appeals", namespace = Representation.APPEALS_NAMESPACE)
public class ClientAppeal {
    
    private static final Logger LOG = LoggerFactory.getLogger(ClientAppeal.class);
    
    @XmlElement(name = "item", namespace = Representation.APPEALS_NAMESPACE)
    private List<Item> items;
    @XmlElement(name = "section", namespace = Representation.APPEALS_NAMESPACE)
    private Section section;
    @XmlElement(name = "status", namespace = Representation.APPEALS_NAMESPACE)
    private AppealStatus status;
    
    private ClientAppeal(){}
    
    public ClientAppeal(Appeal appeal) {
        LOG.debug("Executing ClientAppeal constructor");
        this.section = appeal.getSection();
        this.items = appeal.getItems();
    }
    
    public Appeal getAppeal() {
        LOG.debug("Executing ClientAppeal.getAppeal");
        return new Appeal(section, status, items);
    }
    
    public Section getSection() {
        LOG.debug("Executing ClientAppeal.getSection");
        return section;
    }
    
    public List<Item> getItems() {
        LOG.debug("Executing ClientAppeal.getItems");
        return items;
    }

    @Override
    public String toString() {
        LOG.debug("Executing ClientAppeal.toString");
        try {
            JAXBContext context = JAXBContext.newInstance(ClientAppeal.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public AppealStatus getStatus() {
        LOG.debug("Executing Appeal.getStatus");
        return status;
    }

    public double getScore() {
        LOG.debug("Executing Appeal.getCost");
        double total = 0.0;
        if (items != null) {
            for (Item item : items) {
                if(item != null && item.getExam()!= null) {
                    total += item.getExam().getScore();
                }
            }
        }
        return total;
    }
}