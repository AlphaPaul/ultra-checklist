package com.example.pol.cardview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pol.utilities.AidStationData;
import com.example.pol.utilities.RaceFileData;

public class EditAidStationActivity extends AppCompatActivity {

    Button saveButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_aid_station);

        saveButton = (Button)findViewById(R.id.eas_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSave();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void handleSave(){
        // reads all the components of EAS and sets them to a new aidStation data.

        String name = getEditTextValue(R.id.eas_name_edit);
        double kmDone = Double.parseDouble(getEditTextValue(R.id.eas_km_done_edit));
        double dpDone = Double.parseDouble(getEditTextValue(R.id.eas_dplus_done_edit));
        double kmNext = Double.parseDouble(getEditTextValue(R.id.eas_km_to_next_edit));
        double dpNext = Double.parseDouble(getEditTextValue(R.id.eas_dplus_to_next_edit));

        RaceFileData data = RaceFileData.getInstance();
        AidStationData s = new AidStationData();
        s.name = name;
        s.kmDone = kmDone;
        s.dPlusDone = dpDone;
        s.dPlusToNextStation = kmNext;
        s.kmToNextStation = dpNext;
        data.AddStation(s);
    }

    private String getEditTextValue(int editTextId){
        return ((EditText)findViewById(editTextId)).getText().toString();
    }
}
