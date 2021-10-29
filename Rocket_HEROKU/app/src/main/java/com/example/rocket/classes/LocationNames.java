package com.example.rocket.classes;

public class LocationNames {

    private String locationName;
    private int locationID;

    private float locationrating;
    private String locationType;
    private String locationLocation;

    private String imageURL;

    public LocationNames(String locationName, int locationID, float locationrating, String locationType, String locationLocation,
                         String imageURL) {

        this.locationName = locationName;
        this.locationID = locationID;
        this.locationrating = locationrating;
        this.locationType = locationType;
        this.locationLocation = locationLocation;
        this.imageURL = imageURL;

    }

    public String getLocationName() {
        return this.locationName;
    }
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getLocationID(){return this.locationID;}
    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public float getLocationrating(){return this.locationrating;}

    public String getLocationType() {
        return locationType;
    }

    public String getLocationLocation() {
        return locationLocation;
    }


    public String getImageURL(){return imageURL;}
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
