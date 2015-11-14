package com.example.washab.trafficapp;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by Shabab on 11/12/2015.
 */
public class Locations {

    private static Hashtable<String, Integer> locations = new Hashtable<String, Integer>();
    private static String[] locationNames;

    public static void addLocation (String name, int id) {
        locations.put(name, new Integer (id));
    }

    public static String[] getAllLocationNames() {
        return locationNames;
    }

    public static void prepareLocationNameArray() {
        Enumeration names = locations.keys();
        locationNames=new String[locations.size()];
        int curInd=0;
        while (names.hasMoreElements()) {
            String temp = (String) names.nextElement();
            locationNames[curInd++]=temp;
        }
    }

    public static int getLocationId(String name) {
        if(locations.containsKey(name)){
            return new Integer(locations.get(name)).intValue();
        }
        return -1;
    }
}
