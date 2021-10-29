package com.example.rocket.classes;

public class LocationWord {

    private int mid;

    private String mName;
    private String mFormatted_adress;
    private String mFormatted_phone_number;
    private String mgeomtry;
    private String mIcon;
    private Float mRating;
    private String mWebsite;
    private Integer mPlace_id;

    private Integer mOne_star;
    private Integer mTwo_star;
    private Integer mThree_star;
    private Integer mFour_star;
    private Integer mFive_star;

    private String mCity;
    private String mZone;

    private String mTypeOf;


    public LocationWord(int id,
                        String name,
                        String formated_adress,
                        String formatted_phone_number,
                        String geomtry,
                        String icon,
                        Float rating,
                        String website,
                        Integer place_id,
                        Integer one_star,
                        Integer two_star,
                        Integer three_star,
                        Integer four_star,
                        Integer five_star,
                        String city,
                        String zone,
                        String typeOf){

        mid = id;
        mName = name;
        mFormatted_adress = formated_adress;
        mFormatted_phone_number = formatted_phone_number;
        mgeomtry = geomtry;
        mIcon = icon;
        mRating = rating;
        mWebsite = website;
        mPlace_id = place_id;
        mOne_star = one_star;
        mTwo_star = two_star;
        mThree_star = three_star;
        mFour_star = four_star;
        mFive_star = five_star;
        mCity = city;
        mZone = zone;
        mTypeOf = typeOf;
    }

    public int getmid(){
        return mid;
    }
    public String getmName(){
        return mName;
    }
    public String getmFormatted_adress(){
        return mFormatted_adress;
    }
    public String getmFormatted_phone_number(){
        return mFormatted_phone_number;
    }
    public String getMgeomtry(){
        return mgeomtry;
    }
    public String getmIcon(){
        return mIcon;
    }
    public Float getmRating() {
        return mRating;
    }
    public String getmWebsite() {
        return mWebsite;
    }
    public Integer getmOne_star() {
        return mOne_star;
    }
    public Integer getmPlace_id() {
        return mPlace_id;
    }
    public Integer getmTwo_star() {
        return mTwo_star;
    }
    public Integer getmThree_star() {
        return mThree_star;
    }
    public Integer getmFive_star() {
        return mFive_star;
    }
    public Integer getmFour_star() {
        return mFour_star;
    }
    public String getmCity() {
        return mCity;
    }
    public String getmZone() {
        return mZone;
    }
    public String getmTypeOf() {
        return mTypeOf;
    }
}
