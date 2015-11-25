package com.example.washab.trafficapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

/**
 * Created by Shabab on 11/9/2015.
 */
public class Update {
    private int id,updaterId;

    @Override
    public String toString() {
        return "Update{" +
                "id=" + id +
                ", updaterId=" + updaterId +
                ", locationIdFrom=" + locationIdFrom +
                ", locationIdTo=" + locationIdTo +
                ", estTimeToCross=" + estTimeToCross +
                ", situation='" + situation + '\'' +
                ", description='" + description + '\'' +
                ", timeOfSituation='" + timeOfSituation + '\'' +
                ", timeOfUpdate='" + timeOfUpdate + '\'' +
                ", updaterName='" + updaterName + '\'' +
                ", likersId=" + likersId +
                ", dislikersId=" + dislikersId +
                ", likeCount=" + likeCount +
                ", dislikeCount=" + dislikeCount +
                '}';
    }

    private  int locationIdFrom;



    private int locationIdTo;
    private int estTimeToCross;
    private String situation,description,timeOfSituation,timeOfUpdate;
    private String updaterName;
    private HashSet<Liker> likersId=new HashSet<Liker>();
    private HashSet<Liker> dislikersId=new HashSet<Liker>();
    private int likeCount,dislikeCount;

    public boolean hasTheUserLikedTheUpdate(Liker liker){
        return likersId.contains(liker);
    }

    public boolean hasTheUserDisLikedTheUpdate(int id){
        return dislikersId.contains(id);
    }

    public void addLiker(Liker liker){
        synchronized (this) {
            if (!likersId.contains(liker)) {
                likersId.add(liker);
                likeCount++;
            }
        }
    }

    public void addDisliker(Liker liker){
        dislikersId.add(liker);
        dislikeCount++;
    }
    public void addLikerInitially(Liker liker) {
        likersId.add(liker);

    }

    public void addDisLikerInitially(Liker liker) {
        dislikersId.add(liker);

    }

    public void removeLiker(Liker liker){
        if(likersId.contains(liker)){
            likersId.remove(liker);
        }
    }

    public void removeDisliker(Liker liker){
        if(dislikersId.contains(liker)){
            dislikersId.remove(liker);
        }
    }






//    public Update(int updaterId, String description, int dislikeCount, int estTimeToCross, int id, int likeCount, int locationIdFrom, int locationIdTo, String situation, String timeOfSituation, String timeOfUpdate, String updaterName, HashSet<Integer> likersId, HashSet<Integer> disLikersId) {
//        this.updaterId = updaterId;
//        this.description = description;
//        this.dislikeCount = dislikeCount;
//        this.estTimeToCross = estTimeToCross;
//        this.id = id;
//        this.likeCount = likeCount;
//        this.locationIdFrom = locationIdFrom;
//        this.locationIdTo = locationIdTo;
//        this.situation = situation;
//        this.timeOfSituation = timeOfSituation;
//        this.timeOfUpdate = timeOfUpdate;
//        this.updaterName = updaterName;
//        this.likersId = likersId;
//        this.dislikersId = disLikersId;
//    }



    public Update(int updaterId, String description, int dislikeCount, int estTimeToCross, int id, int likeCount, int locationIdFrom, int locationIdTo, String situation, String timeOfSituation, String timeOfUpdate, String updaterName) {
        this.updaterId = updaterId;
        this.description = description;
        this.dislikeCount = dislikeCount;
        this.estTimeToCross = estTimeToCross;
        this.id = id;
        this.likeCount = likeCount;
        this.locationIdFrom = locationIdFrom;
        this.locationIdTo = locationIdTo;
        this.situation = situation;
        this.timeOfSituation = timeOfSituation;
        this.timeOfUpdate = timeOfUpdate;
        this.updaterName = updaterName;
        //this.likersId = likersId;
        //this.dislikersId = disLikersId;
    }

    public static Update createUpdate(JSONObject jsonObject) {
        try {
             return new Update(jsonObject.getInt("id"),
                     jsonObject.getString("description"),
                    jsonObject.getInt("dislikeCount"),
                    jsonObject.getInt("estTimeToCross"),
                    jsonObject.getInt("id"),
                    jsonObject.getInt("likeCount"),
                    jsonObject.getInt("locationIdFrom"),
                    jsonObject.getInt("locationIdTo"),
                    jsonObject.getString("situation"),
                    jsonObject.getString("timeOfSituation"),
                    jsonObject.getString("timeOfUpdate"),
                    jsonObject.getString("updaterName")
                     );
            //getLikersIdTreeSet(jsonObject.getJSONArray("likers")),
                    //getdislikersIdTreeSet(jsonObject.getJSONArray("dislikers")

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HashSet<Integer> getdislikersIdTreeSet(JSONArray dislikers) {

        HashSet<Integer>dislikerHashset=new HashSet<Integer>();
        int N=dislikers.length();
        for(int i=0;i<N;i++){
            try {
                dislikerHashset.add(dislikers.getInt(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dislikerHashset;


    }

    private static HashSet<Integer> getLikersIdTreeSet(JSONArray likers) {

        HashSet<Integer>likerHashset=new HashSet<Integer>();
        int N=likers.length();
        for(int i=0;i<N;i++){
            try {
                likerHashset.add(likers.getInt(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return likerHashset;
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

    public int getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(int updaterId) {
        this.updaterId = updaterId;
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

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }
}
