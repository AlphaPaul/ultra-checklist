package com.example.pol.utilities;

/**
 * Created by pol on 20/12/2016.
 */

public class RaceFileReader {

    public enum RETURN_CODES{
        SUCCESS,
        INVALID_FILE_NAME,
    }

    // Constructor
    public RaceFileReader(){

    }

    // FileName is optional, set to null uses the set fileName in the data class
    public RETURN_CODES FillInraceFileData(String fileName){
        String path = fileName;
        RaceFileData data = RaceFileData.getInstance();

        if(fileName == null){
            path = data.GetFileName();
            if(path == null){
                return RETURN_CODES.INVALID_FILE_NAME;
            }
        }


        return RETURN_CODES.SUCCESS;

    }

}
