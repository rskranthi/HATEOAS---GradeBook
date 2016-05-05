package com.appealprocess.appeals.model;

import org.joda.time.DateTime;

public class Comments {

    private final double score;
    private final String studentName;
    private final String idNumber;
    private final int creationMonth;
    private final int creationYear;
    private DateTime commentsDate;

    public Comments(double score, String studentName, String idNumber, int creationMonth, int creationYear) {
        this.score = score;
        this.studentName = studentName;
        this.idNumber = idNumber;
        this.creationMonth = creationMonth;
        this.creationYear = creationYear;
        commentsDate = new DateTime();

    }

    public String getStudentName() {
        return studentName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public int getCreationMonth() {
        return creationMonth;
    }

    public int getCreationYear() {
        return creationYear;
    }
    public DateTime getCommentsDate() {
        return commentsDate;
    }
    
    public double getScoreChange(){
        return score;
    }
}
