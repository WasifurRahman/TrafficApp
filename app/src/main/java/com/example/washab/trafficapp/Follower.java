package com.example.washab.trafficapp;

import java.io.Serializable;

/**
 * Created by wasif on 11/26/15.
 */
public class Follower implements Serializable{
    public Follower(int followerId, String followerName) {
        this.followerId = followerId;
        this.followerName = followerName;
    }

    private int followerId;
    private String followerName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Follower)) return false;

        Follower follower = (Follower) o;

        if (getFollowerId() != follower.getFollowerId()) return false;
        return getFollowerName().equals(follower.getFollowerName());

    }

    @Override
    public int hashCode() {
        int result = getFollowerId();
        result = 31 * result + getFollowerName().hashCode();
        return result;
    }

    public int getFollowerId() {
        return followerId;
    }

    public void setFollowerId(int followerId) {
        this.followerId = followerId;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }
}
