package com.apackage.ztmpk;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Buses extends HashMap<String, Bus> {
    private static final String TAG = "buseses";

    public Buses(){
    }
    public void addBuses(String toParse, int type){
        toParse = toParse.replace("{\"result\":[{", "").replace("}]}", "");
        String[] split = toParse.split("\\},\\{");
        for (String aSplit : split) {
            Bus bus = new Bus(aSplit, type);
            put(bus.line + ";" + bus.brigade, bus);
        }
    }
    public void detach(){
        for (int i = 0; i <size(); ++i){
            get(i).detach();
        }
    }
}
