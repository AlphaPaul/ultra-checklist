package com.example.pol.cardview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        for(int i = stationsLayout.getChildCount() - 1; i < stations.size(); i ++){

            View sbView = getLayoutInflater().inflate(R.layout.swipable_button_layout, null);
            stationsLayout.addView(sbView);
            TextView txt = (TextView) sbView.findViewById(R.id.sb_text);
            txt.setText(generateStationString(stations.get(i)));
        }

    }

    private String generateStationString(AidStationData station){
        String ret = station.name + " @km: " +  station.kmDone + "-" + station.dPlusDone + " m D+";
        return ret;
    }



}
