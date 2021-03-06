package com.example.washab.trafficapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by Shabab on 11/9/2015.
 */
public class Discussion implements Serializable{
    private int id;
    private  int locationId;
    private String title,description, timeOfPost;
    private int posterId;
    private String posterName;
    private int likeCount,dislikeCount;
    private HashSet<Voter> likersId=new HashSet<Voter>();
    private HashSet<Voter> dislikersId=new HashSet<Voter>();

//    public Discussion(String description, int dislikeCount, int id, int likeCount, int locationId, String timeOfPost, String title,int posterId, String posterName,HashSet<Integer> likersId, HashSet<Integer> dislikersId) {
//        this.description = description;
//        this.dislikeCount = dislikeCount;
//        this.id = id;
//        this.likeCount = likeCount;
//        this.locationId = locationId;
//        this.timeOfPost = timeOfPost;
//        this.title = title;
//        this.posterId = posterId;
//        this.likersId = likersId;
//        this.dislikersId = dislikersId;
//    }

    public Discussion(String description, int dislikeCount, int id, int likeCount,int locationId, String timeOfPost, int posterId,String posterName) {
        this.description = description;
        this.dislikeCount = dislikeCount;
        this.id = id;
        this.likeCount = likeCount;
        this.locationId = locationId;
        this.timeOfPost = timeOfPost;
        this.posterId = posterId;
        this.posterName = posterName;
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

    public void addDisLikerInitially(Voter voter){
        dislikersId.add(voter);
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



    public String getTimeOfPost() {
        return timeOfPost;
    }

    public void setTimeOfPost(String timeOfPost) {
        this.timeOfPost = timeOfPost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public static Discussion createDiscussion(JSONObject jsonObject) {

        //Discussion(String description, int dislikeCount, int id, int likeCount, String location, String timeOfPost, String title, String updater, HashSet<Integer> likersId, HashSet<Integer> dislikersId)
        //Discussion(String description, int dislikeCount, int id, int likeCount, int locationId, String timeOfPost, String title,int updaterId, HashSet<Integer> likersId, HashSet<Integer> dislikersId)
        try {
            return new Discussion(
                    jsonObject.getString("description"),
                    jsonObject.getInt("dislikeCount"),
                    jsonObject.getInt("id"),
                    jsonObject.getInt("likeCount"),
                    jsonObject.getInt("locationId"),
                    jsonObject.getString("timeOfPost"),
                    jsonObject.getInt("posterId"),
                    jsonObject.getString("posterName")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HashSet<Integer> getdislikersIdTreeSet(JSONArray dislikers) {
        return null;
    }


    private static HashSet<Integer> getLikersIdTreeSet(JSONArray likers) {

        return null;
    }

    public HashSet<Voter> getLikersId() {
        return likersId;
    }

    public void setLikersId(HashSet<Voter> likersId) {
        this.likersId = likersId;
    }

    public HashSet<Voter> getDislikersId() {
        return dislikersId;
    }

    public void setDislikersId(HashSet<Voter> dislikersId) {
        this.dislikersId = dislikersId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getPosterId() {
        return posterId;
    }

    public void setPosterId(int posterId) {
        this.posterId = posterId;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    @Override
    public String toString() {
        return "Discussion{" +
                "id=" + id +
                ", locationId=" + locationId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", timeOfPost='" + timeOfPost + '\'' +
                ", posterId=" + posterId +
                ", posterName='" + posterName + '\'' +
                ", likeCount=" + likeCount +
                ", dislikeCount=" + dislikeCount +
                ", likersId=" + likersId +
                ", dislikersId=" + dislikersId +
                '}';
    }
}

