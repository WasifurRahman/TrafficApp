package com.example.washab.trafficapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shabab on 23/11/2015.
 */
public class Notification {

    private int id, notifToId, notifFromId, locationId,relatedId;
    private String notifType, notifAbout, notifFromUsername, timeOfNotification;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;

        Notification that = (Notification) o;

        if (id != that.id) return false;
        if (notifToId != that.notifToId) return false;
        if (notifFromId != that.notifFromId) return false;
        if (locationId != that.locationId) return false;
        if (getRelatedId() != that.getRelatedId()) return false;
        if (getNotifType() != null ? !getNotifType().equals(that.getNotifType()) : that.getNotifType() != null)
            return false;
        if (getNotifAbout() != null ? !getNotifAbout().equals(that.getNotifAbout()) : that.getNotifAbout() != null)
            return false;
        if (getNotifFromUsername() != null ? !getNotifFromUsername().equals(that.getNotifFromUsername()) : that.getNotifFromUsername() != null)
            return false;
        return !(getTimeOfNotification() != null ? !getTimeOfNotification().equals(that.getTimeOfNotification()) : that.getTimeOfNotification() != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + notifToId;
        result = 31 * result + notifFromId;
        result = 31 * result + locationId;
        result = 31 * result + getRelatedId();
        result = 31 * result + (getNotifType() != null ? getNotifType().hashCode() : 0);
        result = 31 * result + (getNotifAbout() != null ? getNotifAbout().hashCode() : 0);
        result = 31 * result + (getNotifFromUsername() != null ? getNotifFromUsername().hashCode() : 0);
        result = 31 * result + (getTimeOfNotification() != null ? getTimeOfNotification().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", notifToId=" + notifToId +
                ", notifFromId=" + notifFromId +
                ", locationId=" + locationId +
                ", relatedId=" + relatedId +
                ", notifType='" + notifType + '\'' +
                ", notifAbout='" + notifAbout + '\'' +
                ", notifFromUsername='" + notifFromUsername + '\'' +
                ", timeOfNotification='" + timeOfNotification + '\'' +
                '}';
    }

    /**
     * Constructor for Notification object.
     *
     * @param id A unique ID for the notification.
     * @param notifFromId The user who generated the notification.
     * @param locationId A location associated with the notification.
     * @param notifType The type of notification this is.
     * @param notifAbout What is the notification about - update, post, request?
     * @param notifFromUsername The name of the user who generated the notification.
     * @param timeOfNotification The time when the notification was generated.
     */

    public Notification (int id, int notifFromId, String notifType, String notifAbout, String notifFromUsername, String timeOfNotification,int relatedId) {
        this.id = id;
        this.notifFromId = notifFromId;
       // this.locationId = locationId;
        this.notifType = notifType;
        this.notifAbout = notifAbout;
        this.notifFromUsername = notifFromUsername;
        this.timeOfNotification = timeOfNotification;
        this.relatedId=relatedId;


    }

    /**
     * A new notification object is created from database info.
     *
     * @param jsonObject Contains notification info from database.
     * @return A notification object.
     */

    public static Notification createNotification(JSONObject jsonObject) {
        try {
            return new Notification(jsonObject.getInt("notifId"),
                    jsonObject.getInt("notifFromId"),
                    jsonObject.getString("notifType"),
                    jsonObject.getString("notifAbout"),
                    jsonObject.getString("notifFromUsername"),
                    jsonObject.getString("timeOfNotification"),
                    jsonObject.getInt("notifAboutId")
            );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getNotifFromUsername() {
        return notifFromUsername;
    }

    public String getNotifType () {
        return notifType;
    }

    public String getNotifAbout () {
        return notifAbout;
    }

    public String getTimeOfNotification() {
        return timeOfNotification;
    }

    public int getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(int relatedId) {
        this.relatedId = relatedId;
    }
}
