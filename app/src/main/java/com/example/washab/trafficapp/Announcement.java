package com.example.washab.trafficapp;

/**
 * Created by Shabab on 11/9/2015.
 */
public class Announcement {
    private int id;
    private  String locationTo,locationFrom;
    private String timeFrom,timeTo;
    private String title,description,timeOfUpdate;
    private String updater;

    private int likeCount,dislikeCount;

    public Announcement(String description, int dislikeCount, String timeFrom,String timeTo,int id, int likeCount, String locationFrom, String locationTo, String title,  String timeOfUpdate, String updater) {
        this.description = description;
        this.dislikeCount = dislikeCount;
        this.timeFrom=timeFrom;
        this.timeTo=timeTo;
        this.id = id;
        this.likeCount = likeCount;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.title = title;

        this.timeOfUpdate = timeOfUpdate;
        this.updater = updater;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getLocationFrom() {
        return locationFrom;
    }

    public void setLocationFrom(String locationFrom) {
        this.locationFrom = locationFrom;
    }

    public String getLocationTo() {
        return locationTo;
    }

    public void setLocationTo(String locationTo) {
        this.locationTo = locationTo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getTimeOfUpdate() {
        return timeOfUpdate;
    }

    public void setTimeOfUpdate(String timeOfUpdate) {
        this.timeOfUpdate = timeOfUpdate;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }
}
