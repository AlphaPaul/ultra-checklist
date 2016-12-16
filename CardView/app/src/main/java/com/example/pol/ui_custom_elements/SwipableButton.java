package com.example.pol.ui_custom_elements;

import com.example.pol.cardview.R;
import com.example.pol.interactions.SwipeDetector;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by pol on 13/12/2016.
 */

public class SwipableButton extends LinearLayout implements
        SwipeDetector.Swipes{

    private static final String DEBUG_TAG = "Swipable Button: ";
    protected TextView textView = null;
    protected ImageView imageView = null;
    protected Button button = null;


    // Private class variables
    // Gesture detection
    private SwipeDetector swipeDetector;

    // Interface
    public interface SBActions{
        public void  OnSwipe(SwipeDetector.SWIPE_DIR dir, SwipableButton sender);
    }

    private  SBActions sbaInterface;

    // Context for communication with other classes
    private Context ctxt = null;

    // Constructors
    public SwipableButton(Context context) {
        this(context, null);
    }
    public SwipableButton(Context context, AttributeSet attrs) { this(context, null, null);}
    public SwipableButton(Context context, AttributeSet attrs, SBActions intf) {
        super(context, attrs);

        ctxt = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.swipable_button_layout, this, true);

        // Find the view in the layout
        textView = (TextView)findViewById(R.id.sb_text);
        imageView = (ImageView)findViewById(R.id.sb_image);
        // Gesture detection
        swipeDetector = new SwipeDetector(this);

    }

    // Public methods to set all the parameters
    public void SetText(String str){

    }

    public int SetImage(){


        return 1;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        // Using Custom Touch detection
        return swipeDetector.DetectSwipes(event);

    }

    // The swipe detection handling, but now we have an interface for this.
    @Override
    public void OnSwipe(SwipeDetector.SWIPE_DIR sd){
        switch ( sd){
            case SWIPE_DIR_LEFT:
                Log.d(DEBUG_TAG,"SWIPE_DIR_LEFT, destroying the view... ");
                removeAllViews();
                break;
            case SWIPE_DIR_RIGHT:
                Log.d(DEBUG_TAG,"SWIPE_DIR_RIGHT: ");
                break;
            case SWIPE_DIR_TOP:
                Log.d(DEBUG_TAG,"SWIPE_DIR_TOP: ");
                break;
            case SWIPE_DIR_BOTTOM:
                Log.d(DEBUG_TAG,"SWIPE_DIR_BOTTOM: ");
                break;
            case SWIPE_DIR_NONE:
                Log.d(DEBUG_TAG,"SWIPE_DIR_NONE: ");
                break;
        }
    }

}
