package com.example.pol.utilities;

import android.os.Debug;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;

import static android.R.attr.id;

/**
 * Created by pol on 19/12/2016.
 */
public class RaceFileHandler {

    public enum RETURN_CODES{
        SUCCESS,
        ALREADY_EXISTS,
        CREATION_ERROR,
        DIRECTORY_ERROR,
        DEVICE_NOT_READY
    }


    public static final String FILE_DIRECTORY = "/Ultra Checklist/";
    private static final String DEBUG_TAG = "RaceFileHandler";

    private static RaceFileReader reader = new RaceFileReader();
    private static RaceFileWriter writer = new RaceFileWriter();
    private static String filename = null;


    // Constructors and Singleton Handling
    private static RaceFileHandler ourInstance = new RaceFileHandler();

    public static RaceFileHandler getInstance() {
        return ourInstance;
    }

    private RaceFileHandler() {
    }


    // staitc file handling specific methods
    // Returns 1 if file exists, 0 if success, -1 if failure
     public static RETURN_CODES CreateFileAtDefaultPath(String name){

        if(!isExternalStorageWritable()){
            Log.d(DEBUG_TAG, "External Storage not writable");
            return RETURN_CODES.DEVICE_NOT_READY;
        }
         String path = FILE_DIRECTORY;

         File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), path);
         filename = file.getAbsolutePath() + "/" + name;
         // Making sure the directory is created
         file.mkdirs();

         File f = new File(filename);
         if(f.exists() && !f.isDirectory()){
             Log.d(DEBUG_TAG, "File already exists: " + filename);
            return RETURN_CODES.ALREADY_EXISTS;
         }

         f = null;
         try {
             FileWriter fw = new FileWriter(filename);
         }
         catch (Exception e){
             Log.d(DEBUG_TAG, "Impossible to create file for writing: " + e.toString());
             return RETURN_CODES.CREATION_ERROR;
         }

         Log.d(DEBUG_TAG, "File Created at: " + filename + " is: " + filenameFromPath(filename));

         // At this point the file is created but empty.
         // Not really needed, but a nice way to validate everything is working bf the user
         // goes into the process of updating the file...
         RaceFileData.getInstance().SetFileName(filename);
        return RETURN_CODES.SUCCESS;
    }

    public static String getActiveFileName(boolean completePath){
        if(filename != null){
            File f = new File(filename);
            if(f.exists()){
                if(completePath)
                    return filename;
                else
                    return filenameFromPath(filename);
            }
            else{
                return null;
            }
        }
        else {
            return null;
        }
    }



    // Static utility methods
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    // Utility methods only for this class

    private static String filenameFromPath(String path){
        String ret =path.substring(path.lastIndexOf('/') + 1);
        //Log.d(DEBUG_TAG, "Filename cut from: " + path + " to: " + ret);
        return ret;
    }

}
