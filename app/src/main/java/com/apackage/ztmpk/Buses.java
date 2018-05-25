package com.apackage.ztmpk;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Buses extends ConcurrentHashMap<String, Bus> {
    private static final String TAG = "buseses";

    public Buses(){
        Log.d(TAG, String.valueOf(isEmpty()));
    }
    public void addBuses(String toParse, int type){
        toParse = toParse.replace("{\"result\":[{", "").replace("}]}", "");
        String[] split = toParse.split("\\},\\{");
        int it = 0;
        for (String aSplit : split) {
            it += 1;
            Log.d(TAG, String.valueOf(it));
            Bus bus = new Bus(aSplit, type);
            if (containsKey(bus.line + ";" + bus.brigade)){
                Bus prevBus = get(bus.line + ";" + bus.brigade);
                bus.marker = prevBus.marker;
                bus.textMarker = prevBus.textMarker;
                boolean result = bus.setPrevPosition(prevBus.position);
                if (!result && prevBus.prevPosition != null){
                    bus.setPrevPosition(prevBus.prevPosition);
                }
            }
            put(bus.line + ";" + bus.brigade, bus);
        }
    }
    public void detach(){
        for (int i = 0; i <size(); ++i){
            if (get(i) != null)
                get(i).detach();
        }
    }
}
