package com.example.pol.cardview;

import com.example.pol.interactions.SwipeDetector;
import  com.example.pol.ui_custom_elements.SwipableButton;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class CardViewer extends AppCompatActivity implements
SwipableButton.SBActions{

    TextView topText = null;
    ViewGroup cardLayout = null;
    private final String DEBUG_TAG = "CardViewer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_viewer);

        topText = (TextView)findViewById(R.id.cv_top_text);
        // Debug test, putting the text all caps
        topText.setAllCaps(true);

        // Find the layout in which we will add the cards
        cardLayout = (ViewGroup) findViewById(R.id.cv_card_layout);

        // Fill in all the test buttons in the card layouts
        for(int i = 0; i < 12; i++){

            SwipableButton sb = new SwipableButton(this, null, this);
            sb.SetImage(R.drawable.poulet_nourriture_salee);
            sb.SetText("This is the number: " + i);

            Button btn = new Button(this);
            btn.setText("ID: " + i);
            btn.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            cardLayout.addView(btn);
            cardLayout.addView(sb);
        }

    }

    @Override
    public void OnSwipe(SwipeDetector.SWIPE_DIR dir, ViewGroup sender) {

        switch ( dir){
            case SWIPE_DIR_LEFT:
                Log.d(DEBUG_TAG,"SWIPE_DIR_LEFT, destroying the view... ");
                ((SwipableButton)sender).SetInterface(null);
                cardLayout.removeView(sender);
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
