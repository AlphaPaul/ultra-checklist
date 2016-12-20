package com.example.pol.utilities;

import java.util.List;

/**
 * Created by pol on 20/12/2016.
 */

public class RaceFileData {

    public class AidStationData{
        public String name;
        public float kmDone;
        public float dPlusDone;
        public float kmToNextStation;
        public float dPlusToNextStation;
    }

    // The things we need to know:
    private List<AidStationData> stationsData;
    private String fileName;

    // Constructors and Singleton Handling
    private static RaceFileData ourInstance = new RaceFileData();

    public static RaceFileData getInstance() {
        return ourInstance;
    }

    private RaceFileData() {
    }

    //Getters and setters
    // When using this function, make sure the directory is accessible, uses a full path
    public void SetFileName(String pathAndName){
        fileName = pathAndName;
    }

    public String GetFileName(){
        return fileName;
    }



}
