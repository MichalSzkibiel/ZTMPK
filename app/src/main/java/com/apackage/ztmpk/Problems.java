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
    public void add(String name, int yes, int no){
        this.name.add(name);
        this.yes.add(yes);
        this.no.add(no);
    }
}
