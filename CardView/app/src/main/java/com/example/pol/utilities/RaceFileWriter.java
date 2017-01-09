package com.example.pol.utilities;

import android.util.JsonWriter;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by pol on 20/12/2016.
 */

public class RaceFileWriter {

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

    // Save in file the RaceFileData
    public boolean CommitRaceFile(){
        FileOutputStream out = openFileStream(RaceFileHandler.GetActiveFileName(true));
        RaceFileData data = RaceFileData.getInstance();
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out));
        writer.setIndent(RaceFileStrings.JSON_INDENT);
        // Create the base object
        try{
            writer.beginObject();
            writeFileParameters(writer, data);
            writeAidStations(writer,data);
            writeAlarms(writer,data);
            writeRaceTrace(writer,data);
            writer.endObject();
            writer.close();
        }
        catch (Exception e){
            Log.d(DEBUG_TAG, "Exception while writing file: " + e.toString());
        }
        return true;
    }


    private FileOutputStream openFileStream(String fileName){
        FileOutputStream out;
        try{
            // We are overriding the file
            out = new FileOutputStream(fileName, false);
        }
        catch (Exception e){
            Log.d(DEBUG_TAG, "initialize File: " + e.toString() );
            out = null;
        }
        return out;
    }

    // Each method filling in something is responsible for naming, openning and closing all ist JSON
    // Objects and Arrays, and catching its exceptions
    private boolean writeFileParameters(JsonWriter writer, RaceFileData data){
        boolean ret = true;
        try{
            writer.name(RaceFileStrings.HEADER_FILE_PARAMETERS);
            writer.beginObject();
            writer.name(RaceFileStrings.VERSION_NAME).value(RaceFileStrings.VERSION_DATA);
            writer.endObject();
        }
        catch (Exception e){
            ret = false;
            Log.d(DEBUG_TAG, "Exception in writeFileParameters: " + e.toString());
        }
        return ret;
    }

    private boolean writeAidStations(JsonWriter writer, RaceFileData data){
        boolean ret = true;
        ArrayList<AidStationData> stations = data.GetStations();

        try{
            writer.name(RaceFileStrings.HEADER_AID_STATIONS);
            writer.beginArray();
            for (AidStationData d  : stations) {
                writeOneAidStation(writer, d);
            }
            writer.endArray();
        }
        catch (Exception e){
            ret = false;
            Log.d(DEBUG_TAG, "Exception in writeFileParameters: " + e.toString());
        }
        return ret;
    }

    private boolean writeOneAidStation(JsonWriter writer, AidStationData data){
        boolean ret = true;
        try{
            writer.beginObject();
            writer.name(RaceFileStrings.AS_NAME).value(data.name);
            writer.name(RaceFileStrings.AS_KM_DONE).value(data.kmDone);
            writer.name(RaceFileStrings.AS_DP_DONE).value(data.dPlusDone);
            writer.name(RaceFileStrings.AS_KM_NEXT).value(data.kmToNextStation);
            writer.name(RaceFileStrings.AS_DP_NEXT).value(data.dPlusToNextStation);
            writeTodos(writer, data.todos);
            writer.endObject();
        }
        catch (Exception e){
            ret = false;
            Log.d(DEBUG_TAG, "Exception in writeOneAidStation: " + e.toString());
        }
        return ret;
    }

    private boolean writeTodos(JsonWriter writer, ArrayList<TodoActionsData> todos){
        boolean ret = true;
        try{
            writer.name(RaceFileStrings.AS_TODOS);
            writer.beginArray();
            for(TodoActionsData todo : todos){
                writeOneTodo(writer, todo);
            }
            writer.endArray();

        }
        catch (Exception e){
            ret = false;
            Log.d(DEBUG_TAG, "Exception in writeTodos: " + e.toString());
        }
        return ret;
    }

    private boolean writeOneTodo(JsonWriter writer, TodoActionsData data){
        boolean ret = true;
        try{
            writer.beginObject();
            writer.name(RaceFileStrings.AS_TODOS_IMAGE_ID).value(data.id);
            writer.name(RaceFileStrings.AS_TODOS_TEXT).value(data.text);
            // TODO: Add the todo lists
            writer.endObject();
        }
        catch (Exception e){
            ret = false;
            Log.d(DEBUG_TAG, "Exception in writeOneTodo: " + e.toString());
        }
        return ret;
    }

    private boolean writeAlarms(JsonWriter writer, RaceFileData data){
        boolean ret = true;
        try{
            writer.name(RaceFileStrings.HEADER_ALARMS);
            writer.beginArray();
            writer.endArray();

        }
        catch (Exception e){
            ret = false;
            Log.d(DEBUG_TAG, "Exception in writeFileParameters: " + e.toString());
        }
        return ret;
    }

    private boolean writeRaceTrace(JsonWriter writer, RaceFileData data){
        boolean ret = true;
        try{
            writer.name(RaceFileStrings.HEADER_RACE_TRACE);
            writer.beginArray();
            writer.endArray();
        }
        catch (Exception e){
            ret = false;
            Log.d(DEBUG_TAG, "Exception in writeFileParameters: " + e.toString());
        }
        return ret;
    }



    // Debug Public Method to fill in som crap
    public boolean TestWrite(String filename){
        FileOutputStream out = openFileStream(filename);
        if(out == null){
            return false;
        }


        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out));
        writer.setIndent(RaceFileStrings.JSON_INDENT);
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
