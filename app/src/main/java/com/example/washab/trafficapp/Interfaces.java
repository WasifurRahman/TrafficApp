package com.example.washab.trafficapp;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wasif on 11/26/15.
 */
public class Interfaces {
    public interface WhoIsCallingUpdateInterface {
        public final int ADD_UPDATE_WILLINGLY = 100;
        public final int ADD_UPDATE_TO_RESPOND_TO_REQUEST = 101;
    }


    public interface WhichFragmentIsCallingDetailedActivity {
        public final int UPDATE_FRAGMENT = 110;
        public final int REQUEST_FRAGMENT = 111;
        public final int ANNOUNCEMENT_FRAGMENT = 112;
        public final int DISCUSSION_FRAGMENT = 113;
    }

    public interface ToWhichActivityIsTheFragmentAttached {
        public final int HOME_ACTIVITY = 500;
        public final int DETAILED_ACTIVITY = 501;


        /**
         * Detects left and right swipes across a view.
         */
        public static class OnSwipeTouchListener implements View.OnTouchListener {

            private final GestureDetector gestureDetector;

            public OnSwipeTouchListener(Context context) {
                gestureDetector = new GestureDetector(context, new GestureListener());
            }

            public void onSwipeLeft() {
            }

            public void onSwipeRight() {
            }

            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }

            private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

                private static final int SWIPE_DISTANCE_THRESHOLD = 100;
                private static final int SWIPE_VELOCITY_THRESHOLD = 100;

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float distanceX = e2.getX() - e1.getX();
                    float distanceY = e2.getY() - e1.getY();
                    if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (distanceX > 0)
                            onSwipeRight();
                        else
                            onSwipeLeft();
                        return true;
                    }
                    return false;
                }
            }

        }
    }
}

