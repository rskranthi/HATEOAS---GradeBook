package com.appealprocess.appeals.model;

public class CommentsBuilder {
    
    private double score = 10.0f;
    private String studentName = "A. N. Other";
    private String idNumber = "123456789";
    private int creationMonth = 12;
    private int creationYear = 12;
    
    public static CommentsBuilder comments() {
        return new CommentsBuilder();
    }
    
    public CommentsBuilder withScore(double score) {
        if(score >= 0.0f) {
            this.score = score;
        }
        return this;
    }
    
    public CommentsBuilder withStudentName(String name) {
        this.studentName = name;
        return this;
    }
    
    public CommentsBuilder withIdNumber(String cardNumber) {
        this.idNumber = cardNumber;
        return this;
    }
    
    public CommentsBuilder withCreationMonth(int month) {
        if(month > 0 && month < 13) {
            this.creationMonth= month;
        }
        return this;
    }
    
    public CommentsBuilder withCreationYear(int year) {
        if(year >= 0) {
            this.creationYear= year;
        }
        return this;
    }

    public Comments build() {
        return new Comments(score, studentName, idNumber, creationMonth, creationYear);
    }
}
