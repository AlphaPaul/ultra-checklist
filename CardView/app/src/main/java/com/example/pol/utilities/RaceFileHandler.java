package com.example.pol.utilities;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;

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

         Log.d(DEBUG_TAG, "File Created at: " + filename + " is: " + FileNameFromPath(filename));

         // At this point the file is created but empty.
        return RETURN_CODES.SUCCESS;
    }

    public static RETURN_CODES LoadFile(File file){
        if(file.isDirectory()){
            return RETURN_CODES.DIRECTORY_ERROR;
        }
        // Setting the filename of the current file
        filename = file.toString();

        // Updating the Data class with the file
        reader.FillInRaceData();

        return RETURN_CODES.SUCCESS;
    }

    public static String GetActiveFileName(boolean completePath){
        if(filename != null){
            File f = new File(filename);
            if(f.exists()){
                if(completePath)
                    return filename;
                else
                    return FileNameFromPath(filename);
            }
            else{
                return null;
            }
        }
        else {
            return null;
        }
    }

    public static boolean SaveFile(){
        return writer.CommitRaceFile();
    }

    public static File[] GetFilesInDefaultDirectory(){
        return GetFilesInDirectory(GetSaveDirectory());
    }

    public static File GetSaveDirectory(){
        String path = FILE_DIRECTORY;

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), path);
        return file;
    }


    public static File[] GetFilesInDirectory(File path){
        if(!path.isDirectory()){
            Log.d(DEBUG_TAG, "Invalid path for the directory");
            return null;
        }
        File[] files = path.listFiles();

        for(File f : files){
            Log.d(DEBUG_TAG, "File Found: " + f.getAbsoluteFile() + " toString: " + f.toString());
        }

        return files;

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

    public static String FileNameFromPath(String path){
        String ret =path.substring(path.lastIndexOf('/') + 1);
        //Log.d(DEBUG_TAG, "Filename cut from: " + path + " to: " + ret);
        return ret;
    }

}
