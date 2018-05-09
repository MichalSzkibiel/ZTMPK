package com.apackage.ztmpk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;

public class MainActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener {

    public static boolean first_run = true;
    public static StopsHandler sh;
    public static MyMap map_reference;
    public Locator locator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (first_run){
            first_run = false;
            map_reference = new MyMap();
            sh = new StopsHandler(map_reference);
        }
        map_reference.setActivity(this);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mainMap);
        mapFragment.getMapAsync(map_reference);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String title = marker.getTitle();
        if (title == null)
            return false;
        Intent intent = new Intent(this, StopActivity.class);
        if (title.contains(";")){
            String[] elements = title.split(";");
            int superId = Integer.valueOf(elements[0]);
            int underId = Integer.valueOf(elements[1]);
            intent.putExtra("superId", superId);
            intent.putExtra("underId", underId);
        }
        else{
            int superId = Integer.valueOf(title);
            intent.putExtra("superId", superId);
        }
        startActivity(intent);
        return false;
    }
}
