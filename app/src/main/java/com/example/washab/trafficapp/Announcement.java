package com.example.washab.trafficapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by Shabab on 11/9/2015.
 */
public class Announcement implements Serializable{
    private int id;
    private  int locationId;

    private String title,description,timeOfUpdate,source;
    private String posterName;
    private int posterId;
    private int likeCount,dislikeCount;
    private HashSet<Voter> likersId=new HashSet<Voter>();
    private HashSet<Voter> dislikersId=new HashSet<Voter>();

    public Announcement(String description, int dislikeCount,int id, int likeCount,int locationId, String title, String timeOfUpdate, String posterName, int posterId,String source) {
        this.description = description;
        this.dislikeCount = dislikeCount;
        this.source=source;


        this.id = id;
        this.likeCount = likeCount;

        this.locationId = locationId;
        this.title = title;

        this.timeOfUpdate = timeOfUpdate;
        this.posterName = posterName;
        this.posterId = posterId;
    }


    public boolean hasTheUserLikedTheDiscussion(Voter curVoter){
        return likersId.contains(curVoter);
    }

    public boolean hasTheUserDislikedTheDiscussion(Voter curVoter){
        return dislikersId.contains(curVoter);
    }


    public void addLikerInitially(Voter voter){
        likersId.add(voter);
    }

    public void addLiker(Voter voter){
        if(!likersId.contains(voter)){
            likersId.add(voter);
            likeCount++;
        }
    }

    public void removeLiker(Voter voter){
        if(likersId.contains(voter)){
            likersId.remove(voter);
            likeCount--;
        }
    }

    public void addDisliker(Voter voter){
        if(!dislikersId.contains(voter)){
            dislikersId.add(voter);
            dislikeCount++;
        }
    }

    public void addDisLikerInitially(Voter voter){
        dislikersId.add(voter);
    }

    public void removeDisliker(Voter voter){

        if(dislikersId.contains(voter)){
            dislikersId.remove(voter);
            dislikeCount--;
        }
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




    public static Announcement createAnnouncement(JSONObject jsonObject) {
        //Announcement(String description, int dislikeCount,int id, int likeCount,int locationId, String title, String timeOfUpdate, String posterName, int posterId) {
        //Announcement(String description, int dislikeCount, String timeFrom, String timeTo, int id, int likeCount, String locationFrom, String locationTo, String title, String timeOfUpdate, String posterName, int posterId)
        //Announcement(String description, int dislikeCount, String timeFrom, String timeTo, int id, int likeCount, int locationIdFrom, int locationIdTo, String title, String timeOfUpdate, String posterName, int posterId) {
        try {
            return new Announcement(
                    jsonObject.getString("description"),
                    jsonObject.getInt("dislikeCount"),

                    jsonObject.getInt("id"),
                    jsonObject.getInt("likeCount"),
                    jsonObject.getInt("locationId"),

                    jsonObject.getString("title"),
                    jsonObject.getString("timeOfPost"),
                    jsonObject.getString("posterName"),
                    jsonObject.getInt("posterId"),
                    jsonObject.getString("source")


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



    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationIdTo) {
        this.locationId = locationIdTo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
