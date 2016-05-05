package com.appealprocess.appeals.model;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.appealprocess.appeals.representations.Representation;

@XmlRootElement
public class Item {

    @XmlElement(namespace = Representation.APPEALS_NAMESPACE)
    private Exam exam;
    
    /**
     * For JAXB :-(
     */
    Item(){}
    
    public Item(Exam exam) {
     
        this.exam = exam;       
    }
    



    public Exam getExam() {
        return exam;
    }
    
    @Override
    public String toString() {
        return " " + exam;
    }
}