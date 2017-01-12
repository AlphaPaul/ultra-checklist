package com.example.pol.cardview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pol.utilities.AidStationData;
import com.example.pol.utilities.RaceFileData;
import com.example.pol.utilities.RaceFileHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class EditRaceFileActivity extends AppCompatActivity {

    private final String DEBUG_TAG = "EditRaceFileActivity";

    private Button saveButton = null;
    private Button addAidStationButton = null;
    private AppCompatActivity thisActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = this;

        setContentView(R.layout.activity_edit_race_file);


        // Setting up the callback for the buttons
        saveButton = (Button)findViewById(R.id.erf_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSaveButton();
            }
        });

        saveButton.setText(getResources().getString(R.string.erf_save_button) + " " + RaceFileData.getInstance().GetFileName(false));

        addAidStationButton = (Button)findViewById(R.id.erf_add_aid_button);
        addAidStationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAddAidStation();
            }
        });
    }

    // Make sure everything is loaded when the user enters the activity.
    @Override
    protected void onStart() {
        super.onStart();
        DisplayAidStations();
    }

    @Override
    public void onBackPressed() {

        finish();
        // on Back press we finish the activity
        super.onBackPressed();
    }

    // Buton Handlers
    private void handleSaveButton(){
        // Save the file
        RaceFileHandler.SaveFile();
        finish();
    }

    private void handleAddAidStation(){
        Intent intent = new Intent(this, EditAidStationActivity.class);
        startActivity(intent);
    }

    // Loading data from RaceFileData
    private void DisplayAidStations(){
        RaceFileData data = RaceFileData.getInstance();

        ArrayList<AidStationData> stations = data.GetStations();
        ViewGroup stationsLayout = (ViewGroup) findViewById(R.id.erf_aid_station_layout);
        stationsLayout.removeAllViews();

        for(int i = 0 ; i < stations.size(); i ++){

            View dispView = getLayoutInflater().inflate(R.layout.race_event_display, null);
            stationsLayout.addView(dispView);
            TextView txt = (TextView) dispView.findViewById(R.id.red_text_view);
            txt.setText(generateStationString(stations.get(i)));
            ImageView image = (ImageView) dispView.findViewById(R.id.red_image);
            image.setImageResource(R.drawable.plastic_cup_mini);
            ImageButton btn =  (ImageButton) dispView.findViewById(R.id.red_edit_button);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup displayLayout = (ViewGroup) view.getParent();
                    TextView txt = (TextView) displayLayout.findViewById(R.id.red_text_view);
                    if(txt != null){
                        Log.d(DEBUG_TAG, "Text read is: " + txt.getText());
                    }
                    ViewGroup stationsLayout = (ViewGroup) displayLayout.getParent();
                    int index = stationsLayout.indexOfChild(displayLayout);
                    Log.d(DEBUG_TAG, "Index of the button in the layout is: " + index);
                    Intent intent = new Intent(thisActivity, EditAidStationActivity.class);
                    intent.putExtra(Intent.EXTRA_INDEX, index);
                    startActivity(intent);
                }
            });
        }
    }

    private String generateStationString(AidStationData station){
        String ret = station.name + " @km " +  station.kmDone + " - " + station.dPlusDone + "mD+ " + getString(R.string.erf_todo_description) + station.todos.size();
        return ret;
    }



}
