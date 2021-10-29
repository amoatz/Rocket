package com.example.rocket.classes;

public class QuestionWord {

    private String question;
    private int rating;

    public QuestionWord(String question, int rating){
        this.question=question;
        this.rating=rating;
    }

    public String getQuestion(){
        return question;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
