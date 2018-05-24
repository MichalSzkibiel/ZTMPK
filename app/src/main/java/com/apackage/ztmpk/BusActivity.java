package com.apackage.ztmpk;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;

public class BusActivity extends Activity implements BusFragment.OnFragmentInteractionListener, NotificationFragment.OnFragmentInteractionListener {

    private static final String TAG = "autobus";
    public Bus bus;
    public String line;
    public String brigade;
    private MyMap map_reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        line = intent.getStringExtra("line");
        brigade = intent.getStringExtra("brigade");
        if (!MyMap.bh.buses.containsKey(line + ";" + brigade)){
            Toast.makeText(this, "Nie znaleziono autobusu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        bus = MyMap.bh.buses.get(line + ";" + brigade);
        if (savedInstanceState == null) {
            Fragment busFragment = BusFragment.newInstance();
            Fragment notification = NotificationFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.bus_fragment, busFragment)
                    .replace(R.id.notification_fragment, notification).commit();
        }
        StopActivity.allStops.add(this);
        setContentView(R.layout.activity_bus);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.bus_map);
        map_reference = new MyMap(this, bus);
        mapFragment.getMapAsync(map_reference);

        Button exit = findViewById(R.id.return_bus);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = StopActivity.allStops.size() - 1; i >= 0; --i){
                    StopActivity.allStops.get(i).finish();
                    StopActivity.allStops.remove(i);
                }
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed(){
        StopActivity.allStops.remove(StopActivity.allStops.size() - 1);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode) {
            case NotificationFragment.REQUEST_CODE_A:
                Fragment notification = NotificationFragment.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.notification_fragment, notification).commit();
                break;
            case MyMap.MARKER_REQUEST_CODE:
                MyMap.bh.refresh(map_reference.getMap());
        }
    }

}
