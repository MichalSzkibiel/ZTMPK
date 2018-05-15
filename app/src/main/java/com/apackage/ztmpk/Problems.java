package com.apackage.ztmpk;

import java.util.ArrayList;

public class Problems{
    public ArrayList<String> name;
    public ArrayList<Integer> yes;
    public ArrayList<Integer> no;

    public Problems(){
        name = new ArrayList<>();
        yes = new ArrayList<>();
        no = new ArrayList<>();
    }
    public void add(String name){
        this.name.add(name);
        this.yes.add(0);
        this.no.add(0);
    }
    public void yesAdd(){
        yes.set(yes.size() - 1, yes.get(yes.size() - 1) + 1);
    }
    public void noAdd(){
        yes.set(no.size() - 1, no.get(no.size() - 1) + 1);
    }
}
