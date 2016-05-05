package com.appealprocess.appeals.model;

import java.util.Random;


public class ItemBuilder {
    public static ItemBuilder item() {
        return new ItemBuilder();
    }
  
    private Exam exam = Exam.MIDTERM;
    //private Size size = Size.SMALL;
    
    public Item build() {
        return new Item(exam);
    }
   
    
    public ItemBuilder withExam(Exam exam) {
        this.exam = exam;
        return this;
    }
    
   

    public ItemBuilder random() {
        do {
        Random r = new Random();
        exam = Exam.values()[r.nextInt(Exam.values().length)];
        //subcategory = SubCategory.values()[r.nextInt(SubCategory.values().length)];
        } while((exam == Exam.ASSIGNMENTS )); // Obviously Espresso doesn't have milk, other drinks do!
        
        return this;
    }
}
