package com.apackage.ztmpk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MyMap implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private LatLng previousLL;
    private int previousZoom;
    private LatLng position;
    private int zoom;
    private Locator locator;
    private Activity current_activity;
    private static String TAG = "MyMap";
    public static StopsHandler sh;
    private static boolean first_start;
    private static BitmapDescriptor bitmap;
    public static SuperStop currentSuperStop;
    public static UnderStop currentUnderStop;

    public MyMap() {
        first_start = true;
        position = new LatLng(52.25, 21.0);
        zoom = 10;
        previousLL = position;
        previousZoom = zoom;
        Log.d(TAG, "Konstruktor");
    }

    public static MyMap newInstance(){
        Log.d(TAG, "Nowa Instancja");
        return new MyMap();
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
        getMarkerBitmapFromView(R.drawable.ic_bus_stop, act);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Warszawa
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
        if (first_start){
            sh = new StopsHandler(this);
        }
        else {
            sh.draw();
        }
        locator = new Locator(mMap, current_activity);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public String toString(){
        return String.valueOf(position.latitude) +  " " + String.valueOf(position.longitude);
    }


    public Activity getCurrent_activity() {
        return current_activity;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "MarkerClick");
        String title = marker.getTitle();
        if (title == null)
            return false;
        if (title.contains(";")){
            Intent intent = new Intent(current_activity, StopActivity.class);
            String[] elements = title.split(";");
            int superId = Integer.valueOf(elements[0]);
            int underId = Integer.valueOf(elements[1]);
            currentSuperStop = sh.stops.get(superId);
            currentUnderStop = sh.stops.get(superId).underStops.get(underId);
            move(currentUnderStop.position, 15);
            current_activity.startActivity(intent);
        }
        return false;
    }

    private void getMarkerBitmapFromView(@DrawableRes int resId, Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            VectorDrawable vectorDrawable = (VectorDrawable) activity.getDrawable(resId);

            int h = vectorDrawable.getIntrinsicHeight();
            int w = vectorDrawable.getIntrinsicWidth();

            vectorDrawable.setBounds(0, 0, w, h);

            Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            vectorDrawable.draw(canvas);

            bitmap = BitmapDescriptorFactory.fromBitmap(bm);

        } else {
            bitmap = BitmapDescriptorFactory.fromResource(resId);
        }
    }

    public static BitmapDescriptor getBitmap(){
        return bitmap;
    }
}
