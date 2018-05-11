package com.apackage.ztmpk;

import android.util.Log;

import java.sql.Time;

public class Departure {
    public String line;
    public String headsign;
    public String brigade;
    public String time;
    private static String TAG = "odjazdy";

    public Departure(String line, String toParse){
        this.line = line;
        String[] split = toParse.split("\\},\\{");
        String[] split_brigade = split[2].split("\"");
        brigade = split_brigade[3];
        String[] split_headsign = split[3].split("\"");
        headsign = split_headsign[3];
        String[] split_time = split[5].split("\"");
        time = split_time[3];
        int hours = Integer.valueOf(time.substring(0,2));
        hours %= 24;
        String hoursString = String.valueOf(hours);
        if (hoursString.length() == 1){
            hoursString = "0" + hoursString;
        }
        time = hoursString + time.substring(2);
    }

    @Override
    public String toString(){
        return time;
    }
}
