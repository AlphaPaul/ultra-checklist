package com.example.pol.utilities;

import android.util.JsonWriter;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by pol on 20/12/2016.
 */

public class RaceFileWriter {

    public final String WRITER_INDENT = "    ";

    private final String DEBUG_TAG = "RaceFileWriter";

    // Constructor
    public RaceFileWriter(){

    }

    public boolean WriteFileData(){

        RaceFileData data = RaceFileData.getInstance();
        FileOutputStream out;

        try{

        }
        catch(Exception e){
            Log.d(DEBUG_TAG, "Exception caught writing: " + e.toString());
        }

        return true;
    }


    // Debug Public class
    public boolean TestWrite(String filename){

        FileOutputStream out;
        try{
            out = new FileOutputStream(filename);
        }
        catch (Exception e){
            Log.d(DEBUG_TAG, "initialize File: " + e.toString() );
            return false;
        }

        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out));
        writer.setIndent(WRITER_INDENT);
        try {
            // Main Oject
            writer.beginObject();
            // Aid Stations Array
            writer.name(RaceFileStrings.HEADER_AID_STATIONS);
            writer.beginArray();
            writer.endArray();

            // Alarms arrays
            writer.name(RaceFileStrings.HEADER_ALARMS);
            writer.beginArray();
            writer.endArray();

            // Race Trace Object
            writer.name(RaceFileStrings.HEADER_RACE_TRACE);
            writer.beginObject();
            writer.endObject();

            // closing main object
            writer.endObject();

            // Closing writer
            writer.close();

        }
        catch (Exception e){
            Log.d(DEBUG_TAG, "initialize File: " + e.toString() );
            return false;
        }

        return true;
    }



}
