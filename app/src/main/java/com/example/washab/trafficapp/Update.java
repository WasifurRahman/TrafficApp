package com.example.washab.trafficapp;

/**
 * Created by Shabab on 11/9/2015.
 */
public class Update {
    private int id;
    private  String locationTo,locationFrom;
    private int estTimeToCross;
    private String situation,description,timeOfSituation,timeOfUpdate;
    private String updater;

    public Update(String description, int dislikeCount, int estTimeToCross,int id, int likeCount, String locationFrom, String locationTo, String situation, String timeOfSituation, String timeOfUpdate, String updater) {
        this.description = description;
        this.dislikeCount = dislikeCount;
        this.estTimeToCross = estTimeToCross;
        this.id = id;
        this.likeCount = likeCount;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.situation = situation;
        this.timeOfSituation = timeOfSituation;
        this.timeOfUpdate = timeOfUpdate;
        this.updater = updater;
    }

    private int likeCount,dislikeCount;

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

    public int getEstTimeToCross() {
        return estTimeToCross;
    }

    public void setEstTimeToCross(int estTimeToCross) {
        this.estTimeToCross = estTimeToCross;
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

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getTimeOfSituation() {
        return timeOfSituation;
    }

    public void setTimeOfSituation(String timeOfSituation) {
        this.timeOfSituation = timeOfSituation;
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
}
