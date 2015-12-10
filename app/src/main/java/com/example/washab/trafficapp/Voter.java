package com.example.washab.trafficapp;

import java.io.Serializable;

/**
 * Created by wasif on 11/14/15.
 */
public class Voter implements Serializable{

    public Voter(int likerId, String likerName) {
        this.likerId = likerId;
        this.likerName = likerName;
    }

    public int getLikerId() {
        return likerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Voter)) return false;

        Voter voter = (Voter) o;

        if (getLikerId() != voter.getLikerId()) return false;
        return getLikerName().equals(voter.getLikerName());

    }

    @Override
    public int hashCode() {
        int result = getLikerId();
        result = 31 * result + getLikerName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Voter{" +
                "likerId=" + likerId +
                ", likerName='" + likerName + '\'' +
                '}';
    }

    public void setLikerId(int likerId) {
        this.likerId = likerId;
    }

    public String getLikerName() {
        return likerName;
    }

    public void setLikerName(String likerName) {
        this.likerName = likerName;
    }

    private int likerId;
    private String likerName = "" ;


}
