package com.example.pol.cardview;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pol.ui_custom_elements.IdStringPair;
import com.example.pol.ui_custom_elements.ImageIdAdapter;
import com.example.pol.utilities.AidStationData;
import com.example.pol.utilities.RaceFileData;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class EditAidStationActivity extends AppCompatActivity {

    private Button saveButton = null;
    private Button addTodoButton = null;

    private ArrayList<IdStringPair> idToStringList;
    private final String DEBUG_TAG = "EditAidStationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_aid_station);

        idToStringList = new ArrayList<>();
        idToStringList.add(new IdStringPair(R.drawable.action_meditate, getString(R.string.drawable_action_meditate)));
        idToStringList.add(new IdStringPair(R.drawable.action_sleep_baby, getString(R.string.drawable_action_sleep)));

        saveButton = (Button)findViewById(R.id.eas_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSave();
                finish();
            }
        });

        addTodoButton = (Button)findViewById(R.id.eas_add_todo_button);
        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAddTodo();
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


    private Dialog addTodoDialog;
    private void handleAddTodo(){
        // Create Dialog W/ ListView

        // Create the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final ImageIdAdapter adapter = new ImageIdAdapter(this, idToStringList);

        ListView imgView = new ListView(this);
        imgView.setAdapter(adapter);
        builder.setView(imgView);

        imgView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                IdStringPair pairSelected = adapter.getItem(position);

                // Launch the next dialog to set the card's trext
                launchTextDialog(pairSelected);
                Log.d(DEBUG_TAG, "Clicked on: " + pairSelected.toString());
                addTodoDialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.eas_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setTitle(R.string.eas_todo_select_image);

        addTodoDialog = builder.create();
        addTodoDialog.show();


    }


    private Dialog textEditDialog;
    private IdStringPair savedPair;
    private void launchTextDialog(IdStringPair pair){

        savedPair = pair;
        // Create the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate((R.layout.create_file_dialog), null));
        builder.setPositiveButton(R.string.cfd_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // retrieve the value set by the user
                EditText edit = (EditText)textEditDialog.findViewById(R.id.cfd_text_edit);
                String userText = edit.getText().toString();
                Log.d(DEBUG_TAG, "Text entered : " + userText);
                createTodoCard(savedPair.id, userText);
            }
        });

        builder.setNegativeButton(R.string.cfd_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setTitle(R.string.cfd_title);

        textEditDialog = builder.create();
        textEditDialog.show();

    }

    private void createTodoCard(int id, String userText){

        ViewGroup todoLayout = (ViewGroup) findViewById(R.id.eas_todo_layout);
        LayoutInflater inflater = getLayoutInflater();
        View cardView = inflater.inflate(R.layout.swipable_button_layout, null);

        TextView txt = (TextView) cardView.findViewById(R.id.sb_text);
        txt.setText(userText);

        ImageView img = (ImageView) cardView.findViewById(R.id.sb_image);
        img.setImageResource(id);

        todoLayout.addView(cardView);

    }



}
