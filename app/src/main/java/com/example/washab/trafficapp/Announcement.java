package com.example.washab.trafficapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shabab on 11/9/2015.
 */
public class Announcement {
    private int id;
    private  int locationIdTo,locationIdFrom;
    private String timeFrom,timeTo;
    private String title,description,timeOfUpdate;
    private String posterName;
    private int posterId;
    private int likeCount,dislikeCount;

    public Announcement(String description, int dislikeCount, String timeFrom, String timeTo, int id, int likeCount, int locationIdFrom, int locationIdTo, String title, String timeOfUpdate, String posterName, int posterId) {
        this.description = description;
        this.dislikeCount = dislikeCount;
        this.timeFrom=timeFrom;
        this.timeTo=timeTo;
        this.id = id;
        this.likeCount = likeCount;
        this.locationIdFrom = locationIdFrom;
        this.locationIdTo = locationIdTo;
        this.title = title;

        this.timeOfUpdate = timeOfUpdate;
        this.posterName = posterName;
        this.posterId = posterId;
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

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
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

    public static Announcement createAnnouncement(JSONObject jsonObject) {

        //Announcement(String description, int dislikeCount, String timeFrom, String timeTo, int id, int likeCount, String locationFrom, String locationTo, String title, String timeOfUpdate, String posterName, int posterId)
        //Announcement(String description, int dislikeCount, String timeFrom, String timeTo, int id, int likeCount, int locationIdFrom, int locationIdTo, String title, String timeOfUpdate, String posterName, int posterId) {
        try {
            return new Announcement(
                    jsonObject.getString("description"),
                    jsonObject.getInt("dislikeCount"),
                    jsonObject.getString("timeFrom"),
                    jsonObject.getString("timeTo"),
                    jsonObject.getInt("id"),
                    jsonObject.getInt("likeCount"),
                    jsonObject.getInt("locationIdFrom"),
                    jsonObject.getInt("locationIdTo"),
                    jsonObject.getString("title"),
                    jsonObject.getString("timeOfUpdate"),
                    jsonObject.getString("posterName"),
                    jsonObject.getInt("posterId")


            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getPosterId() {
        return posterId;
    }

    public void setPosterId(int posterId) {
        this.posterId = posterId;
    }

    public int getLocationIdFrom() {
        return locationIdFrom;
    }

    public void setLocationIdFrom(int locationIdFrom) {
        this.locationIdFrom = locationIdFrom;
    }

    public int getLocationIdTo() {
        return locationIdTo;
    }

    public void setLocationIdTo(int locationIdTo) {
        this.locationIdTo = locationIdTo;
    }
}
