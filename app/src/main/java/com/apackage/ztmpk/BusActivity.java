package com.apackage.ztmpk;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;

public class BusActivity extends Activity implements BusFragment.OnFragmentInteractionListener, NotificationFragment.OnFragmentInteractionListener {

    public Bus bus;
    public int idx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        idx = intent.getIntExtra("idx", -1);
        if (idx == -1 || idx >= MyMap.bh.buses.size()){
            Toast.makeText(this, "Nie znaleziono autobusu", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        bus = MyMap.bh.buses.get(idx);
        if (savedInstanceState == null) {
            Fragment busFragment = BusFragment.newInstance();
            Fragment notification = NotificationFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.bus_fragment, busFragment)
                    .replace(R.id.notification_fragment, notification).commit();
        }
        StopActivity.allStops.add(this);
        setContentView(R.layout.activity_bus);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.bus_map);
        mapFragment.getMapAsync(new MyMap(this, bus));

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


}
