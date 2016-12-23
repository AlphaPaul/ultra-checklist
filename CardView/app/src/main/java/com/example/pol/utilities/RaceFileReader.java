package com.example.pol.utilities;

import android.util.JsonReader;
import android.util.Log;

import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by pol on 20/12/2016.
 */

public class RaceFileReader {
    private final String DEBUG_TAG = "RaceFileReader";

    public enum RETURN_CODES{
        SUCCESS,
        INVALID_FILE_NAME,
    }

    // Constructor
    public RaceFileReader(){

    }

    // FileName is optional, set to null uses the set fileName in the data class
    public RETURN_CODES FillInRaceData(){
        FileInputStream in = OpenReadStream(RaceFileData.getInstance().GetFileName(true));
        RaceFileData data = RaceFileData.getInstance();
        if(in == null){
            return RETURN_CODES.INVALID_FILE_NAME;
        }
        // Clean up before loading
        data.ClearAllData();
        JsonReader reader = new JsonReader(new InputStreamReader(in));

        try{
            reader.beginObject();
            while(reader.hasNext()){
                String name = reader.nextName();
                if(name.equals(RaceFileStrings.HEADER_FILE_PARAMETERS)){
                    readFileParameters(reader, data);
                }
                else if(name.equals(RaceFileStrings.HEADER_AID_STATIONS)){
                    readAidStations(reader, data);
                }
                else if(name.equals(RaceFileStrings.HEADER_ALARMS)){
                    readAlarms(reader, data);
                }
                else if(name.equals(RaceFileStrings.HEADER_RACE_TRACE)){
                    readRaceTrace(reader, data);
                }
                else{
                    Log.d(DEBUG_TAG, "Name Unknown: " + name);
                }
            }

            reader.endObject();

        }
        catch(Exception e){
            Log.d(DEBUG_TAG, "Fill In Race Data " + e.toString());
        }
        return RETURN_CODES.SUCCESS;

    }

    private boolean readFileParameters(JsonReader reader, RaceFileData data){
        boolean ret = true;
        try{
            reader.beginObject();
            String name = reader.nextName();
            if(name.equals(RaceFileStrings.VERSION_NAME)){
                String val = reader.nextString();
                Log.d(DEBUG_TAG, "Version of the file is: " + val);
            }
            reader.endObject();
        }
        catch(Exception e){
            Log.d(DEBUG_TAG, "Exception in ReadFileParameters: " + e.toString());
        }
        return ret;
    }

    private boolean readAidStations(JsonReader reader, RaceFileData data){
        boolean ret = true;
        try{
            reader.beginArray();
            while(reader.hasNext()){
                readOneAidStation(reader, data);
            }

        }
        catch(Exception e){
            Log.d(DEBUG_TAG, "Exception in ReadFileParameters: " + e.toString());
        }

        return ret;
    }

    private boolean readOneAidStation(JsonReader reader, RaceFileData data){
        boolean ret = true;
        AidStationData as = new AidStationData();
        try{
            reader.beginObject();
            while(reader.hasNext()){
                String name = reader.nextName();
                if(name.equals(RaceFileStrings.AS_NAME)){
                    as.name = reader.nextString();
                }
                else if(name.equals(RaceFileStrings.AS_KM_DONE)){
                    as.kmDone = reader.nextDouble();
                }
                else if(name.equals(RaceFileStrings.AS_DP_DONE)){
                    as.dPlusDone = reader.nextDouble();
                }
                else if(name.equals(RaceFileStrings.AS_KM_NEXT)){
                    as.kmToNextStation = reader.nextDouble();
                }
                else if(name.equals(RaceFileStrings.AS_DP_NEXT)){
                    as.dPlusToNextStation = reader.nextDouble();
                }
                else if(name.equals(RaceFileStrings.AS_TODOS)){
                    //TODO: handle the todo list
                }
                else{
                    Log.d(DEBUG_TAG, "unknown aid station parameter: " + name);
                }
            }
            reader.endObject();
            data.AddStation(as);
        }
        catch(Exception e){
            Log.d(DEBUG_TAG, "Exception in ReadFileParameters: " + e.toString());
        }

        return ret;
    }

    private boolean readAlarms(JsonReader reader, RaceFileData data){
        boolean ret = true;
        try{

        }
        catch(Exception e){
            Log.d(DEBUG_TAG, "Exception in ReadFileParameters: " + e.toString());
        }

        return ret;
    }

    private boolean readRaceTrace(JsonReader reader, RaceFileData data){
        boolean ret = true;
        try{

        }
        catch(Exception e){
            Log.d(DEBUG_TAG, "Exception in ReadFileParameters: " + e.toString());
        }

        return ret;
    }


    private FileInputStream OpenReadStream(String path){

        FileInputStream in = null;
        try{
            in = new FileInputStream(path);
        }
        catch (Exception e){
            Log.d(DEBUG_TAG, "File Not Found: " + path);
        }
        return in;

    }

}
