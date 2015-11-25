package com.example.washab.trafficapp;

import android.widget.Toast;
import android.content.Context;

/**
 * Created by Shabab on 11/8/2015.
 */
public class Utility {

    public static class CurrentUser{

        private static int id, displayPage = 0;
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

        public static int getDisplayPage() {
            return displayPage;
        }

        public static void setDisplayPage(int i) {
            displayPage = i;
        }

        public static void showConnectionError(Context context) {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
        }

    }


}


