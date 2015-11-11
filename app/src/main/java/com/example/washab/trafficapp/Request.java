package com.example.washab.trafficapp;

/**
 * Created by Shabab on 11/10/2015.
 */

public class Request {

    private int requestId;
    private String requesterName;
    private String locationFrom,locationTo;
    private String timeOfRequest;
    private int followerCount;
    private String extraText;

    public Request(String extraText, int followerCount, String locationFrom, String locationTo, String requesterName, int requestId, String timeOfRequest) {
        this.extraText = extraText;
        this.followerCount = followerCount;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.requesterName = requesterName;
        this.requestId = requestId;
        this.timeOfRequest = timeOfRequest;
    }

    public String getExtraText() {
        return extraText;
    }

    public void setExtraText(String extraText) {
        this.extraText = extraText;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
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
}
