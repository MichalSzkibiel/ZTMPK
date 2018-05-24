package com.apackage.ztmpk;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class StopActivity extends Activity implements StopFragment.OnFragmentInteractionListener, NotificationFragment.OnFragmentInteractionListener {

    private RecyclerView mRecyclerView;

    private class TimetableDownload extends AsyncTask<Integer, Integer, Timetable>{

        @Override
        protected Timetable doInBackground(Integer... views) {
            return new Timetable(StopActivity.this, mRecyclerView);
        }

        @Override
        protected void onPostExecute(Timetable result) {
            mRecyclerView.setAdapter(result);
        }

    }

    public SuperStop superStop;
    public UnderStop underStop;
    private static String TAG = "przyst";
    public static List<Activity> allStops = new ArrayList<>();
    public int superId;
    public int underId;
    private MyMap map_reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        superId = intent.getIntExtra("superId", -1);
        underId = intent.getIntExtra("underId", -1);
        if (superId == -1 || superId >= MyMap.sh.stops.size() || underId == -1 || underId >= MyMap.sh.stops.get(superId).underStops.size()) {
            Toast.makeText(this, "Nie znaleziono przystanku", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        superStop = MyMap.sh.stops.get(superId);
        underStop = superStop.underStops.get(underId);

        if (savedInstanceState == null) {
            Fragment stopFragment = StopFragment.newInstance();
            Fragment notification = NotificationFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.stop_fragment, stopFragment)
                    .replace(R.id.notification_fragment, notification).commit();
        }
        allStops.add(this);
        setContentView(R.layout.activity_stop);

        mRecyclerView = (RecyclerView) findViewById(R.id.stop_depatures);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        (new TimetableDownload()).execute();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.stop_map);
        map_reference = new MyMap(this, underStop);
        mapFragment.getMapAsync(map_reference);

        Button exit = findViewById(R.id.return_stop);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = allStops.size() - 1; i >= 0; --i){
                    allStops.get(i).finish();
                    allStops.remove(i);
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        allStops.remove(allStops.size() - 1);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
