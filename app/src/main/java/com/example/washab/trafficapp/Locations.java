package com.example.washab.trafficapp;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by Shabab on 11/12/2015.
 */
public class Locations {

    private static Hashtable<String, Integer> locations = new Hashtable<String, Integer>();
    private static Hashtable<Integer,String> reverseLocations = new Hashtable<Integer,String>();
    private static String[] locationNames;
    private static String[] locationNamesForSearch;

    public static void addLocation (String name, int id) {
        locations.put(name, new Integer (id));
        reverseLocations.put(new Integer(id),name);
    }

    public static String[] getAllLocationNames() {
        return locationNames;
    }

    public static void prepareLocationNameArray() {
        Enumeration names = locations.keys();
        locationNames=new String[locations.size()];
        locationNamesForSearch=new String[locations.size()+1];
        locationNamesForSearch[0]="All Locations";
        int curInd=0;
        while (names.hasMoreElements()) {
            String temp = (String) names.nextElement();
            locationNames[curInd++]=temp;
            locationNamesForSearch[curInd]=temp;
        }
    }

    public static int getLocationId(String name) {
        if(locations.containsKey(name)){
            return new Integer(locations.get(name)).intValue();
        }
        return -1;
    }

    public static String getLocationName(int id) {
        if(reverseLocations.containsKey(id)){
            return reverseLocations.get(id);
        }
        return null;
    }

    public static String prepString() {
        String ans="";
        for(int i=0;i<locationNames.length;i++){
            ans+=locationNames[i];
        }
        return ans;
    }

    public static String[] getAllLocationNamesForSearch() {
        return locationNamesForSearch;
    }
}
