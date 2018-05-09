package com.apackage.ztmpk;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class StopsHandler {
    public ArrayList<SuperStop> stops;
    private MyMap caller;

    public StopsHandler(MyMap call) {
        caller = call;
        FirebaseDatabase FD = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = FD.getReference("Przystanki");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String list = dataSnapshot.getValue().toString();
                try {
                    stops = new ArrayList<>();
                    JSONArray json = new JSONArray(list);
                    for (int i = 0; i < json.length(); ++i){
                        try {
                            stops.add(new SuperStop(json.getJSONObject(i)));
                        } catch (JSONException e){}
                    }
                } catch (JSONException e) {
                }
                draw();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void draw(){
        GoogleMap mMap = caller.getMap();
        for (int i = 0; i < stops.size(); ++i){
            stops.get(i).draw(mMap, i);
        }
    }
}
