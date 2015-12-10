package com.example.washab.trafficapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Shabab on 12/2/2015.
 */
public class SessionManager {

    private static SessionManager instance;

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor = new SharedPreferences.Editor() {
        @Override
        public SharedPreferences.Editor putString(String key, String value) {
            return null;
        }

        @Override
        public SharedPreferences.Editor putStringSet(String key, Set<String> values) {
            return null;
        }

        @Override
        public SharedPreferences.Editor putInt(String key, int value) {
            return null;
        }

        @Override
        public SharedPreferences.Editor putLong(String key, long value) {
            return null;
        }

        @Override
        public SharedPreferences.Editor putFloat(String key, float value) {
            return null;
        }

        @Override
        public SharedPreferences.Editor putBoolean(String key, boolean value) {
            return null;
        }

        @Override
        public SharedPreferences.Editor remove(String key) {
            return null;
        }

        @Override
        public SharedPreferences.Editor clear() {
            return null;
        }

        @Override
        public boolean commit() {
            return false;
        }

        @Override
        public void apply() {

        }
    };

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "SaveSession";

    // All Shared Preferences Keys
    private static final String IS_LOGGED_IN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    public static String getApiKey() {
        return API_KEY;
    }

    // Email address (make variable public to access from outside)
    private static final String API_KEY = "apiKey";

    // Constructor
    private SessionManager(){}

    public static SessionManager Instance() {
        // if no instance initialised yet, then create new instance
        // else return stored instance
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Set instance context
     */

    public void init(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String name, String apiKey){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGGED_IN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing apiKey in pref
        editor.putString(API_KEY, apiKey);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     **/
    public void checkLogin(){
        // Check login status
        if(isLoggedIn()) {
            Intent i = new Intent(_context, Home.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(API_KEY, pref.getString(API_KEY, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGGED_IN, false);
    }
}
