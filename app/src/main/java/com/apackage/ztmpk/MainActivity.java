package com.apackage.ztmpk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;

public class MainActivity extends AppCompatActivity {

    public static boolean first_run = true;
    public static MyMap map_reference;
    public Locator locator;
    private static String TAG = "Mejn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (first_run){
            first_run = false;
            map_reference = com.apackage.ztmpk.MyMap.newInstance();
        }
        map_reference.setActivity(this);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mainMap);
        mapFragment.getMapAsync(map_reference);

    }

}
