package com.example.washab.trafficapp;

/**
 * Created by wasif on 11/26/15.
 */
public class Interfaces {
    public interface WhoIsCallingUpdateInterface{
           public final int ADD_UPDATE_WILLINGLY=100;
           public final int ADD_UPDATE_TO_RESPOND_TO_REQUEST=101;
    }

    public interface WhichFragmentIsCallingDetailedActivity {
        public final int UPDATE_FRAGMENT =110;
        public final int REQUEST_FRAGMENT=111;
        public final  int ANNOUNCEMENT_FRAGMENT=112;
        public final int DISCUSSION_FRAGMENT =113;
    }

    public interface ToWhichActivityIsTheFragmentAttached{
        public  final int HOME_ACTIVITY=500;
        public final int DETAILED_ACTIVITY=501;

    }
}


