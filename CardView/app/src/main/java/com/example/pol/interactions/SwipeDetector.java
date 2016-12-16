package com.example.pol.interactions;

import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import java.lang.System;

/**
 * Created by pol on 15/12/2016.
 */

// Swipe detector takes in a motion event and calls the interface function
// when it detect a swipe.
public class SwipeDetector {

    // Constants and enums
    public enum SWIPE_DIR{
        SWIPE_DIR_LEFT,
        SWIPE_DIR_RIGHT,
        SWIPE_DIR_TOP,
        SWIPE_DIR_BOTTOM,
        SWIPE_DIR_NONE
    };

    // Swipe parameters
    final private long MAX_SWIPE_TIME_MS = 450;
    final private long MIN_SWIPE_TIME_MS = 10;
    final private long MIN_SWIPE_DIST_DP = 100;

    // Interfaces

    // To send back feedback
    public interface Swipes{
        void OnSwipe(SWIPE_DIR sDir);
    }

    // Public and Protected variables

    // Class Wide Private Variables
    private Swipes swipeInterface = null;



    // Constructor
    public SwipeDetector(Swipes itf){
        swipeInterface = itf;
    }


    // Public Methods
    public boolean DetectSwipes(MotionEvent event){
        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                handleDownEvent(event);
                break;

            case (MotionEvent.ACTION_MOVE) :
                break;

            case (MotionEvent.ACTION_UP) :
                handleUpEvent(event);
                break;

            case (MotionEvent.ACTION_CANCEL) :
                handleCancelEvent(event);
                break;
            case (MotionEvent.ACTION_OUTSIDE) :
                break;
            default :
                break;
        }
        return  true;
    }

    public void SetIterfaceHandler(Swipes parent){
        swipeInterface = parent;
    }

    // Private Methods
    private long upTimeMillis, downTimeMillis, cancelTimeMillis;
    private float upXPos, downXPos, cancelXPos;
    private float upYPos, downYPos, cancelYPos;

    private void handleDownEvent(MotionEvent event){
        downXPos = event.getX();
        downYPos = event.getY();
        downTimeMillis = System.currentTimeMillis();
    }

    private void handleUpEvent(MotionEvent event){
        upXPos = event.getX();
        upYPos = event.getY();
        upTimeMillis = System.currentTimeMillis();
        validateSwipe(upXPos, upYPos, upTimeMillis);
    }

    void handleCancelEvent(MotionEvent event){
        // For now, we don't validate canceled events
        cancelXPos = downXPos;
        cancelYPos = downYPos;
        cancelTimeMillis = System.currentTimeMillis();
        validateSwipe(upXPos, upYPos, upTimeMillis);

    }

    void validateSwipe(float endXPos, float endYPos, long endTime){
        SWIPE_DIR sd = SWIPE_DIR.SWIPE_DIR_NONE;
        // In case we go to the right, the diff is positive (Android defined)
        if(endXPos - downXPos > MIN_SWIPE_DIST_DP){
            sd = SWIPE_DIR.SWIPE_DIR_RIGHT;
        }
        if(endXPos - downXPos < -MIN_SWIPE_DIST_DP){
            sd = SWIPE_DIR.SWIPE_DIR_LEFT;
        }

        // Now for top / bottom
        if(endYPos - downYPos > MIN_SWIPE_DIST_DP){
            sd = SWIPE_DIR.SWIPE_DIR_BOTTOM;
        }
        if(endYPos - downYPos > MIN_SWIPE_DIST_DP){
            sd = SWIPE_DIR.SWIPE_DIR_TOP;
        }

        // Checking if we swiped within the time allowed
        if(endTime - downTimeMillis > MAX_SWIPE_TIME_MS){
            sd = SWIPE_DIR.SWIPE_DIR_NONE;
        }
        if(endTime - downTimeMillis < MIN_SWIPE_TIME_MS){
            sd = SWIPE_DIR.SWIPE_DIR_NONE;
        }

        if(swipeInterface != null){
            swipeInterface.OnSwipe(sd);
        }
    }

}
