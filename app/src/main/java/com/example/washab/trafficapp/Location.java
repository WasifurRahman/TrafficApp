package com.example.washab.trafficapp;

/**
 * Created by wasif on 11/25/15.
 */
public class Location {

    public int getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }



    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }





    public Location(int locationId, String locationName, boolean alreadyChecked) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.alreadyChecked = this.checkedInThisSession=alreadyChecked;
    }

    private int locationId;
    private String locationName;

    public boolean isAlreadyChecked() {
        return alreadyChecked;
    }

    public void setAlreadyChecked(boolean alreadyChecked) {
        this.alreadyChecked = alreadyChecked;
    }

    public boolean isCheckedInThisSession() {
        return checkedInThisSession;
    }

    public void setCheckedInThisSession(boolean checkedInThisSession) {
        this.checkedInThisSession = checkedInThisSession;
    }

    private boolean alreadyChecked,checkedInThisSession;
}
