package com.apackage.ztmpk;

import android.util.Log;

import java.sql.Time;

public class Departure {
    public String line;
    public String headsign;
    public String brigade;
    public String time;
    private static String TAG = "odjazdy";

    private static String decode(String text){
        text = text.replace("\\u0104", "Ą");
        text = text.replace("\\u0106", "Ć");
        text = text.replace("\\u0118", "Ę");
        text = text.replace("\\u0141", "Ł");
        text = text.replace("\\u0143", "Ń");
        text = text.replace("\\u00d3", "Ó");
        text = text.replace("\\u015a", "Ś");
        text = text.replace("\\u0179", "Ź");
        text = text.replace("\\u017b", "Ż");
        text = text.replace("\\u0105", "ą");
        text = text.replace("\\u0107", "ć");
        text = text.replace("\\u0119", "ę");
        text = text.replace("\\u0142", "ł");
        text = text.replace("\\u0144", "ń");
        text = text.replace("\\u00f3", "ó");
        text = text.replace("\\u015b", "ś");
        text = text.replace("\\u017a", "ź");
        text = text.replace("\\u017c", "ż");
        return text;
    }

    public Departure(String line, String toParse){
        this.line = line;
        String[] split = toParse.split("\\},\\{");
        String[] split_brigade = split[2].split("\"");
        brigade = split_brigade[3];
        String[] split_headsign = split[3].split("\"");
        headsign = decode(split_headsign[3]);
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
