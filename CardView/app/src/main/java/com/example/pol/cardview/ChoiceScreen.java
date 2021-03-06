package com.example.pol.cardview;

import com.example.pol.cardview.CardViewer;
import com.example.pol.utilities.RaceFileHandler;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.lang.Class;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChoiceScreen extends AppCompatActivity {

    private final String DEBUG_TAG ="ChoiceScreen";
    private Button createButton = null;
    private Button loadButton = null;

    // References for intricate classes
    private ChoiceScreen choiceScreen = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        choiceScreen = this;

        setContentView(R.layout.activity_choice_screen);

        // Retrieve the buttons on the activiy
        createButton = (Button)findViewById(R.id.cs_create_button);
        loadButton = (Button)findViewById(R.id.cs_load_button);

        // set the on click listeners
        if(createButton == null || loadButton == null){
            Log.d(DEBUG_TAG, "Button was not found");
        }



        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(DEBUG_TAG, "Clicked on Create");
                handleCreate();
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(DEBUG_TAG, "Clicked on Load, starting CardViewe Activity");
                handleLoad();
            }
        });

    }


    private Dialog cfdDialog;
    // Method registered in the onClickListener of the create button
    private void handleCreate(){

        // Create the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate((R.layout.create_file_dialog), null));
        builder.setPositiveButton(R.string.cfd_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // retrieve the value set by the user
                EditText edit = (EditText)cfdDialog.findViewById(R.id.cfd_text_edit);
                String userText = edit.getText().toString();
                Log.d(DEBUG_TAG, "Text entered : " + userText);

                // Create the file, warns if already exists
                RaceFileHandler.RETURN_CODES ret = RaceFileHandler.CreateFileAtDefaultPath(userText);
                if(ret != RaceFileHandler.RETURN_CODES.SUCCESS){
                    Log.d(DEBUG_TAG, "Impossible to create file: " + ret.toString());
                }


                // start the card edit activity
                Log.d(DEBUG_TAG, "Clicked on Create, all is good, starting Edit Race File Activity");
                // For now we just start the CardViewer Activity
                Intent intent = new Intent(choiceScreen, EditRaceFileActivity.class);
                startActivity(intent);


            }
        });

        builder.setNegativeButton(R.string.cfd_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setTitle(R.string.cfd_title);

        cfdDialog = builder.create();
        cfdDialog.show();

    }

    private Dialog fileDialog;
    private File[] files;
    private class StringComparator implements Comparator<String>{
        @Override
        public int compare(String s, String t1) {
            return s.compareToIgnoreCase(t1);
        }
    }
    private File fileSelected;
    private void handleLoad(){

        files = RaceFileHandler.GetFilesInDefaultDirectory();
        ArrayList<String> fileNames = new ArrayList<String>();

        for(File f : files){
            fileNames.add(RaceFileHandler.FileNameFromPath(f.toString()));
        }

        // Create the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ListView fileView = new ListView(this);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.file_list_adapter,R.id.fla_file_text, fileNames);
        adapter.sort(new StringComparator());
        fileView.setAdapter(adapter);
        builder.setView(fileView);

        fileView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String name = adapter.getItem(position);
                Log.d(DEBUG_TAG, "Clicked on: " + name);
                for(File f : files){
                    Log.d(DEBUG_TAG, "Comparing: " + RaceFileHandler.FileNameFromPath(f.toString()) + " and " + name);
                    if(RaceFileHandler.FileNameFromPath(f.toString()).equals(name)){
                        Log.d(DEBUG_TAG, "File Found: " + f.toString());
                        fileSelected = f;
                        RaceFileHandler.LoadFile(f);
                        Intent intent = new Intent(choiceScreen, EditRaceFileActivity.class);
                        startActivity(intent);
                        fileDialog.dismiss();
                        break;
                    }
                }
            }
        });

        builder.setNegativeButton(R.string.lfd_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setTitle(R.string.lfd_title);

        fileDialog = builder.create();
        fileDialog.show();

        // For now we just start the CardViewer Activity
        //Intent intent = new Intent(choiceScreen, CardViewer.class);
        //startActivity(intent);

    }







}
