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

import java.util.ArrayList;

public class MyMap implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private ArrayList<LatLng> positions;
    private ArrayList<Integer> zooms;
    private Locator locator;
    private Activity current_activity;
    private static String TAG = "MyMap";
    public static StopsHandler sh;
    private static boolean first_start;
    private static BitmapDescriptor bitmap;
    public static SuperStop currentSuperStop;
    public static UnderStop currentUnderStop;
    public static BusHandler bh;
    public static Bus currentBus;

    public MyMap() {
        first_start = true;
        positions = new ArrayList<LatLng>();
        positions.add(new LatLng(52.25, 21.0));
        zooms = new ArrayList<>();
        zooms.add(10);
        Log.d(TAG, "Konstruktor");
    }

    public static MyMap newInstance(){
        Log.d(TAG, "Nowa Instancja");
        return new MyMap();
    }

    public void move(LatLng newPosition, int newZoom) {
        positions.add(newPosition);
        zooms.add(newZoom);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, newZoom));
    }

    public void back() {
        positions.remove(positions.size() - 1);
        zooms.remove(zooms.size() - 1);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positions.get(positions.size() - 1), zooms.get(zooms.size() -1)));
    }

    public void back_to_main(){
        for (int i = positions.size() - 2; i >= 0; ++i){
            positions.remove(i);
            zooms.remove(i);
        }
    }

    public GoogleMap getMap() {
        return mMap;
    }

    public void setActivity(Activity act){
        current_activity = act;
        bitmap = getMarkerBitmapFromView(R.drawable.ic_bus_stop, act);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Warszawa
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positions.get(positions.size() - 1), zooms.get(zooms.size() -1)));
        if (first_start){
            sh = new StopsHandler(this);
            bh = new BusHandler();
        }
        else {
            sh.draw();
            bh.refresh();
        }
        locator = new Locator(mMap, current_activity, getMarkerBitmapFromView(R.drawable.ic_gps_location_symbol, current_activity));
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
        else if(title.contains("bus")){
            int idx = Integer.valueOf(title.replace("bus", ""));
            currentBus = bh.buses.get(idx);
            move(currentBus.position, 15);
            current_activity.startActivity(new Intent(current_activity, BusActivity.class));
        }
        return false;
    }

    private BitmapDescriptor getMarkerBitmapFromView(@DrawableRes int resId, Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            VectorDrawable vectorDrawable = (VectorDrawable) activity.getDrawable(resId);

            int h = vectorDrawable.getIntrinsicHeight();
            int w = vectorDrawable.getIntrinsicWidth();

            vectorDrawable.setBounds(0, 0, w, h);

            Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            vectorDrawable.draw(canvas);

            return BitmapDescriptorFactory.fromBitmap(bm);

        } else {
            return BitmapDescriptorFactory.fromResource(resId);
        }
    }

    public static BitmapDescriptor getBitmap(){
        return bitmap;
    }
}
