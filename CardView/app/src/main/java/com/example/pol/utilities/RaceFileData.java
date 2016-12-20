package com.example.pol.utilities;

import java.util.List;

/**
 * Created by pol on 20/12/2016.
 */

public class RaceFileData {

    public class AidStationData{


    }

    public List<AidStationData> stationsData;

    private String fileName;

    // Constructors and Singleton Handling
    private static RaceFileData ourInstance = new RaceFileData();

    public static RaceFileData getInstance() {
        return ourInstance;
    }

    private RaceFileData() {
    }

    // When using this function, make sure the directory is accessible, uses a full path
    public void SetFileName(String pathAndName){
        fileName = pathAndName;
    }

}
