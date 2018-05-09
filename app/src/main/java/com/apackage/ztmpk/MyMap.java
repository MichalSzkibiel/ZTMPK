package com.apackage.ztmpk;

import android.app.Activity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MyMap implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng previousLL;
    private int previousZoom;
    private LatLng position;
    private int zoom;
    private Locator locator;
    private Activity current_activity;

    public MyMap() {
        position = new LatLng(51.5, 21.0);
        zoom = 10;
        previousLL = position;
        previousZoom = zoom;
    }

    public void move(LatLng newPosition, int newZoom) {
        previousLL = position;
        previousZoom = zoom;
        position = newPosition;
        zoom = newZoom;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
    }

    public void back() {
        position = previousLL;
        zoom = previousZoom;
    }

    public GoogleMap getMap() {
        return mMap;
    }

    public void setActivity(Activity act){
        current_activity = act;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Warszawa
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
        MainActivity.sh.draw();
        locator = new Locator(mMap, current_activity);
    }
}
