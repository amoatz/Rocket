package com.example.rocket.classes;

public class CommentWord {

    private String name;
    private String comment;
    private float rating;

    public CommentWord(String name, String comment, float rating){
        this.name=name;
        this.comment=comment;
        this.rating=rating;
    }

    public String getName(){
        return name;
    }

    public String getComment() {
        return comment;
    }

    public float getRating() {
        return rating;
    }
}
