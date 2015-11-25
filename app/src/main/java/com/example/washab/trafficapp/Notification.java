package com.example.washab.trafficapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shabab on 23/11/2015.
 */
public class Notification {

    private int id, notifToId, notifFromId, locationId;
    private String notifType, notifAbout, notifFromUsername, timeOfNotification;

    public Notification (int id, int notifFromId, int locationId, String notifType, String notifAbout, String notifFromUsername, String timeOfNotification) {
        this.id = id;
        this.notifFromId = notifFromId;
        this.locationId = locationId;
        this.notifType = notifType;
        this.notifAbout = notifAbout;
        this.notifFromUsername = notifFromUsername;
        this.timeOfNotification = timeOfNotification;

    }

    public static Notification createNotification(JSONObject jsonObject) {
        try {
            return new Notification(jsonObject.getInt("notifId"),
                    jsonObject.getInt("notifFromId"),
                    jsonObject.getInt("locationId"),
                    jsonObject.getString("notifType"),
                    jsonObject.getString("notifAbout"),
                    jsonObject.getString("notifFromUsername"),
                    jsonObject.getString("timeOfNotification")
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

}
