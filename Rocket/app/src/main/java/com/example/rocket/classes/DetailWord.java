package com.example.rocket.classes;

public class DetailWord {

    private String titleWord;
    private String valueWord;

    public DetailWord(String titleWord, String valueWord){
        this.titleWord=titleWord;
        this.valueWord=valueWord;
    }

    public String getTitleWord() {
        return titleWord;
    }

    public String getValueWord() {
        return valueWord;
    }
}
