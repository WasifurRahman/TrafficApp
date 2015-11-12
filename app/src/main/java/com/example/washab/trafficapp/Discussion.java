package com.example.washab.trafficapp;

/**
 * Created by Shabab on 11/9/2015.
 */
public class Discussion{
    private int id;
    private  String location;
    private String title,description,timeOfUpdate;
    private String updater;
    private int likeCount,dislikeCount;

    public Discussion(String description, int dislikeCount, int id, int likeCount, String location, String timeOfUpdate, String title, String updater) {
        this.description = description;
        this.dislikeCount = dislikeCount;
        this.id = id;
        this.likeCount = likeCount;
        this.location = location;
        this.timeOfUpdate = timeOfUpdate;
        this.title = title;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimeOfUpdate() {
        return timeOfUpdate;
    }

    public void setTimeOfUpdate(String timeOfUpdate) {
        this.timeOfUpdate = timeOfUpdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }
}

