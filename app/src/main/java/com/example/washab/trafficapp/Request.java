package com.example.washab.trafficapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

/**
 * Created by Shabab on 11/10/2015.
 */

public class Request {

    private int requestId, requesterId;
    private String requesterName;
    private int locationIdFrom,locationIdTo;
    private String timeOfRequest, description, requestStatus;
    private int followerCount;
    private HashSet<Follower>followers=new HashSet<Follower>();

    public Request(int requesterId, String requesterName,String description, int followerCount, int locationIdFrom, int locationIdTo, String status, int requestId, String timeOfRequest) {
        this.requesterId = requesterId;
        this.requesterName=requesterName;
        this.description = description;
        this.followerCount = followerCount;
        this.locationIdFrom = locationIdFrom;
        this.locationIdTo = locationIdTo;
//        this.requesterName = requesterName;
        this.requestId = requestId;
        this.timeOfRequest = timeOfRequest;
        this.requestStatus = status;
    }


    public static Request createRequest(JSONObject jsonObject) {
        try {
            return new Request(jsonObject.getInt("requesterId"),
                    jsonObject.getString("requesterName"),
                    jsonObject.getString("description"),
                    jsonObject.getInt("followerCount"),
                    jsonObject.getInt("locationIdFrom"),
                    jsonObject.getInt("locationIdTo"),
                    jsonObject.getString("status"),
                    jsonObject.getInt("id"),
                    jsonObject.getString("timeOfRequest")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean hasTHeUserFollowedTheRequest(Follower follower){
        return followers.contains(follower);
    }

    public void addFollowerInitially(Follower follower){
        if(!followers.contains(follower)){
            followers.add(follower);
        }
    }

    public void addFollower(Follower follower){
        if(!followers.contains(follower)){
            followers.add(follower);
            followerCount++;
        }
    }

    public void removeFollower(Follower follower){
        if(followers.contains(follower)){
            followers.remove(follower);
            followerCount--;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setExtraText(String extraText) {
        this.description = description;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getTimeOfRequest() {
        return timeOfRequest;
    }

    public void setTimeOfRequest(String timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
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

    public int getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(int requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    @Override
    public String toString() {
        return "Request{" +
                "description='" + description + '\'' +
                ", requestId=" + requestId +
                ", requesterId=" + requesterId +
                ", requesterName='" + requesterName + '\'' +
                ", locationIdFrom=" + locationIdFrom +
                ", locationIdTo=" + locationIdTo +
                ", timeOfRequest='" + timeOfRequest + '\'' +
                ", requestStatus='" + requestStatus + '\'' +
                ", followerCount=" + followerCount +
                '}';
    }
}
