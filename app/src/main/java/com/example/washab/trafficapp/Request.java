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

    /**
     * Constructor for request object.
     *
     * @param requesterId The user ID of the user who made the request.
     * @param description Any additional information regarding the request.
     * @param followerCount The number of users following the request.
     * @param locationIdFrom Source location of request.
     * @param locationIdTo Destination location of request.
     * @param status Current status of the request - active, answered or expired?
     * @param requestId A unique ID for the request.
     * @param timeOfRequest The time when the request was made.
     */

    public Request(int requesterId, String description, int followerCount, int locationIdFrom, int locationIdTo, String status, int requestId, String timeOfRequest) {
        this.requesterId = requesterId;
        this.description = description;
        this.followerCount = followerCount;
        this.locationIdFrom = locationIdFrom;
        this.locationIdTo = locationIdTo;
//        this.requesterName = requesterName;
        this.requestId = requestId;
        this.timeOfRequest = timeOfRequest;
        this.requestStatus = status;
    }

    /**
     * A new request object is created from database info.
     * @param jsonObject
     * @return A request object.
     */

    public static Request createRequest(JSONObject jsonObject) {
        try {
            return new Request(jsonObject.getInt("requesterId"),
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

    /**
     *
     * @param follower A Follower object.
     * @return True if the user is already following the request, false otherwise.
     */

    public boolean hasTHeUserFollowedTheRequest(Follower follower){
        return followers.contains(follower);
    }

    /**
     * Adds the user to the initial list of followers of the request from database.
     *
     * @param follower A Follower object.
     */

    public void addFollowerInitially(Follower follower){
        if(!followers.contains(follower)){
            followers.add(follower);
        }
    }

    /**
     * Adds the user to the followers' list of the request if s/he isn't already there.
     *
     * @param follower A Follower object.
     */

    public void addFollower(Follower follower){
        if(!followers.contains(follower)){
            followers.add(follower);
            followerCount++;
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
