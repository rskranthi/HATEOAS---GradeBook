package com.appealprocess.appeals.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum Exam {

    
    @XmlEnumValue(value="midterm")
    MIDTERM(20),
    
    @XmlEnumValue(value="final")
    FINAL(20),

    @XmlEnumValue(value="assignments")
    ASSIGNMENTS(30),
    
    @XmlEnumValue(value="quizzes")
    QUIZZES(30);

    // weightage support
    final double score;
    Exam(double score) { this.score = score; }
    public double getScore() { return this.score; }
 }
