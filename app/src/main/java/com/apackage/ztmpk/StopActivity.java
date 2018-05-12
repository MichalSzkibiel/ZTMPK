package com.apackage.ztmpk;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class StopActivity extends Activity implements StopFragment.OnFragmentInteractionListener, NotificationFragment.OnFragmentInteractionListener {

    private static String TAG = "przyst";
    public static List<Activity> allStops = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Fragment stopFragment = StopFragment.newInstance();
            Fragment notification = NotificationFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.stop_fragment, stopFragment)
                    .replace(R.id.notification_fragment, notification).commit();
        }
        allStops.add(this);
        setContentView(R.layout.activity_stop);
        MainActivity.map_reference.setActivity(this);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.stop_map);
        mapFragment.getMapAsync(MainActivity.map_reference);

        Button exit = findViewById(R.id.return_stop);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.map_reference.back_to_main();
                for (int i = allStops.size() - 1; i >= 0; --i){
                    allStops.get(i).finish();
                    allStops.remove(i);
                }
            }
        });

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.stop_depatures);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new Timetable(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed(){
        MainActivity.map_reference.back();
        allStops.remove(allStops.size() - 1);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
