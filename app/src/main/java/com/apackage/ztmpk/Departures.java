package com.apackage.ztmpk;

import android.os.Build;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class Departures extends ArrayList<Departure> {
    private static String TAG = "odjazdy";
    private String time;

    public Departures(){}
    public void addLine(String line, String toParse){
        String[] split = toParse.split("values");
        for (int i = 1; i < split.length; ++i){
            add(new Departure(line, split[i]));
        }
    }

    private static int timeSubstract(String t1, String t2){
        int hours1 = Integer.valueOf(t1.substring(0,2));
        int minutes1 = Integer.valueOf(t1.substring(3,5));
        int seconds1 = Integer.valueOf(t1.substring(6,8));
        int secCom1 = hours1 * 3600 + minutes1 * 60 + seconds1;
        int hours2 = Integer.valueOf(t2.substring(0,2));
        int minutes2 = Integer.valueOf(t2.substring(3,5));
        int seconds2 = Integer.valueOf(t2.substring(6,8));
        int secCom2 = hours2 * 3600 + minutes2 * 60 + seconds2;

        int secDiff = secCom2 - secCom1;
        if (secDiff < 0){
            secDiff += 24*3600;
        }
        return secDiff;
    }

    public void sortByCurrentTime(){
        Date currentTime = Calendar.getInstance().getTime();
        time = currentTime.toString().substring(11, 19);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sort(new Comparator<Departure>() {
                    @Override
                    public int compare(Departure o1, Departure o2) {
                        int first = timeSubstract(o1.time, time);
                        int second = timeSubstract(o2.time, time);
                        if (first == second)
                            return 0;
                        else if(first < second)
                            return 1;
                        else
                            return -1;
                    }
                });
            Log.d(TAG, toString());
        }
    }
}
