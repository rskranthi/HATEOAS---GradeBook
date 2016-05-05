package com.appealprocess.appeals.model;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

public class Appeal {
    
    private final Section section;
    private final List<Item> items;
    @XmlTransient
    private AppealStatus status = AppealStatus.UNPROCESSED;

    public Appeal(Section section, List<Item> items) {
      this(section, AppealStatus.UNPROCESSED, items);
    }
    

    public Appeal(Section section, AppealStatus status, List<Item> items) {
        this.section = section;
        this.items = items;
        this.status = status;
    }

    public Section getSection() {
        return section;
    }
    
    public List<Item> getItems() {
        return items;
    }

    public double calculateTotal() {
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

    public void setStatus(AppealStatus status) {
        this.status = status;
    }

    public AppealStatus getStatus() {
        return status;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Section: " + section + "\n");
        sb.append("Status: " + status + "\n");
        for(Item i : items) {
            sb.append("Item: " + i.toString()+ "\n");
        }
        return sb.toString();
    }
}