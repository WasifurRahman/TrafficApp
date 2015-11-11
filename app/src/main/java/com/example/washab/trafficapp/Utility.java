package com.example.washab.trafficapp;

/**
 * Created by Shabab on 11/8/2015.
 */
public class Utility {

    public static class CurrentUser{

        private static int id;
        private static String apiKey,username;

        public static void setUser(int i,String uName,String apikey){
            id=i;
            username=uName;
            apiKey=apikey;

        }

        public static int getId(){return id;}
        public static String getName(){return username;}

        public static String getApiKey() {
            return apiKey;
        }

        public static String makeString(){
            return ""+id+" "+username+" "+apiKey;
        }

    }
}


