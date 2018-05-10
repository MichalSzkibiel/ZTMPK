package com.apackage.ztmpk;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class StopActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            StopFragment stopFragment = StopFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.stop_fragment, stopFragment).commit();
        }
        setContentView(R.layout.activity_stop);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent startIntent = getIntent();
        int superId = startIntent.getIntExtra("superId", -1);
        int underId = startIntent.getIntExtra("underId", -1);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mainMap);
        LatLng position = new LatLng(0.0, 0.0);
        if (underId != -1){
            UnderStop stop = MyMap.sh.stops.get(superId).underStops.get(underId);
            position = stop.position;
        }
        else{
            SuperStop stop = MyMap.sh.stops.get(superId);
            position = stop.position;
        }
        MainActivity.map_reference.move(position, 15);
        MainActivity.map_reference.setActivity(this);
        mapFragment.getMapAsync(MainActivity.map_reference);

    }

    @Override
    public void onBackPressed(){
        //MainActivity.map_reference.back();
        finish();
    }

}
