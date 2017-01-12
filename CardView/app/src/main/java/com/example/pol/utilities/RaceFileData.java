package com.example.pol.utilities;

import java.util.ArrayList;
import java.util.List;
import com.example.pol.utilities.AidStationData;

/**
 * Created by pol on 20/12/2016.
 */

public class RaceFileData {


    // The things we need to know:
    private ArrayList<AidStationData> stationsData;

    // Constructors and Singleton Handling
    private static RaceFileData ourInstance = new RaceFileData();

    public static RaceFileData getInstance() {
        return ourInstance;
    }

    private RaceFileData() {
        stationsData = new ArrayList<AidStationData>();
    }

    //Getters and setters
    // When using this function, make sure the directory is accessible, uses a full path


    public String GetFileName(boolean completePath){
        return RaceFileHandler.GetActiveFileName(completePath);
    }


    public ArrayList<AidStationData> GetStations(){
        return stationsData;
    }

    public void AddStation(AidStationData data){
        // TODO: Order the stations based on km
        stationsData.add(data);

    }

    public void ReplaceStation(int index, AidStationData data){
        stationsData.set(index, data);
    }

    public void ClearAllData(){
        stationsData.clear();
    }

    public AidStationData GetAidStationAtIndex(int index){
        return stationsData.get(index);
    }

    private void addDummyAidStation(){

        AidStationData s = new AidStationData();
        s.name = "Dummy Station";
        s.kmDone = -10.5;
        s.dPlusDone = 251;
        s.dPlusToNextStation = 5610;
        s.kmToNextStation = 6123;
        stationsData.add(s);

    }



}
