package com.apackage.ztmpk;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.MapFragment;

public class BusActivity extends Activity implements BusFragment.OnFragmentInteractionListener, NotificationFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Fragment busFragment = BusFragment.newInstance();
            Fragment notification = NotificationFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.bus_fragment, busFragment)
                    .replace(R.id.notification_fragment, notification).commit();
        }
        StopActivity.allStops.add(this);
        setContentView(R.layout.activity_bus);
        MainActivity.map_reference.setActivity(this);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.bus_map);
        mapFragment.getMapAsync(MainActivity.map_reference);

        Button exit = findViewById(R.id.return_bus);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.map_reference.back_to_main();
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
        MainActivity.map_reference.back("bus");
        StopActivity.allStops.remove(StopActivity.allStops.size() - 1);
        finish();
    }


}
