package com.example.pol.utilities;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by pol on 20/12/2016.
 */

public class RaceFileStrings {


    public static final String JSON_INDENT = "    ";
    public static final String HEADER_AID_STATIONS= "aid_stations";
    public static final String HEADER_ALARMS = "alarms";
    public static final String HEADER_RACE_TRACE = "race_trace";
    public static final String HEADER_FILE_PARAMETERS = "file_parameters";


    // File Parameters Names
    public static final String VERSION_NAME = "version";
    public static final String VERSION_DATA = "Dev Version " + GregorianCalendar.getInstance().getTime().toString();

    // File Aid Stations Name
    public static final String AS_NAME = "name";
    public static final String AS_KM_DONE = "km_done";
    public static final String AS_DP_DONE = "dp_done";
    public static final String AS_KM_NEXT = "km_next";
    public static final String AS_DP_NEXT = "dp_next";
    public static final String AS_TODOS = "todos";


}
