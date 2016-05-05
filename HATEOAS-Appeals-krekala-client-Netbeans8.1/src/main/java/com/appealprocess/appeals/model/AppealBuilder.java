package com.appealprocess.appeals.model;

import static com.appealprocess.appeals.model.ItemBuilder.item;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppealBuilder {
    
    private static final Logger LOG = LoggerFactory.getLogger(AppealBuilder.class);
    
    public static AppealBuilder appeal() {
        return new AppealBuilder();
    }

    private Section section = Section.ONLINE;
    private ArrayList<Item> items = null;
    private AppealStatus status = AppealStatus.UNPROCESSED;
    
    private void defaultItems() {
        LOG.debug("Executing AppealBuilder.defaultItems");
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(item().build());
        this.items = items;
    }
    
    private void corruptItems() {
        LOG.debug("Executing AppealBuilder.corruptItems");
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(null);
        items.add(null);
        items.add(null);
        items.add(null);
        this.items = items;
    }
   
    
    public Appeal build() {
        LOG.debug("Executing AppealBuilder.build");
        if(items == null) {
            defaultItems();
        }
        return new Appeal(section, status, items);
    }

    public AppealBuilder withItem(Item item) {
        LOG.debug("Executing AppealBuilder.withItem");
        if(items == null) {
            items = new ArrayList<Item>();
        }
        items.add(item);
        return this;
    }


    public AppealBuilder withCorruptedValues() {
        LOG.debug("Executing AppealBuilder.withCorruptedValues");
        corruptItems();
        return this;
    }
    
    public AppealBuilder withStatus(AppealStatus status) {
        LOG.debug("Executing AppealBuilder.withRandomItems");
        this.status = status;
        return this;
    }

    public AppealBuilder withRandomItems() {
        LOG.debug("Executing AppealBuilder.withRandomItems");
        int numberOfItems = 1;
        this.items = new ArrayList<Item>();
        for(int i = 0; i < numberOfItems; i++) {
            items.add(item().random().build());
        }
        return this;
    }

}
