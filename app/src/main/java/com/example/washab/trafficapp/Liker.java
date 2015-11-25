package com.example.washab.trafficapp;

/**
 * Created by wasif on 11/14/15.
 */
public class Liker {

    public Liker(int likerId, String likerName) {
        this.likerId = likerId;
        this.likerName = likerName;
    }

    public int getLikerId() {
        return likerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Liker)) return false;

        Liker liker = (Liker) o;

        if (getLikerId() != liker.getLikerId()) return false;
        return getLikerName().equals(liker.getLikerName());

    }

    @Override
    public int hashCode() {
        int result = getLikerId();
        result = 31 * result + getLikerName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Liker{" +
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
