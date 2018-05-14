package com.apackage.ztmpk;

import java.util.ArrayList;

public class Buses extends ArrayList<Bus> {
    public Buses(){
    }
    public void addBuses(String toParse, int type){
        toParse = toParse.replace("{\"result\":[{", "").replace("}]}", "");
        String[] split = toParse.split("\\},\\{");
        for (int i = 0; i < split.length; ++i){
            add(new Bus(split[i], type));
        }
    }
    public void detach(){
        for (int i = 0; i <size(); ++i){
            get(i).detach();
        }
    }
}
