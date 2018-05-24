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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import static com.apackage.ztmpk.Locator.getPosition;
//import static com.apackage.ztmpk.Locator.position;

public class MyMap implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private LatLng position;
    private int zoom;
    private Locator locator;
    private Activity current_activity;
    private static String TAG = "MyMap";
    public static StopsHandler sh;
    private static boolean first_start;
    private static BitmapDescriptor bitmapSuper;
    private static BitmapDescriptor bitmapUnder;
    private static BitmapDescriptor bitmapBus;
    private static BitmapDescriptor bitmapTram;
    public static BusHandler bh;
    private static Integer widenStop;
    private String tribe;
    private StopsHandler sh1;
    private Marker lineNumber;
    private static ArrayList<Marker> busMarkers;

    public MyMap(Activity act) {
        tribe = "main";
        first_start = true;
        position = new LatLng(52.25, 21.0);
        zoom = 10;
        current_activity = act;
        bitmapUnder = getMarkerBitmapFromView(R.drawable.ic_bus_stop, act);
        bitmapSuper = getMarkerBitmapFromView(R.drawable.ic_complex_stop, act);
        bitmapBus = getMarkerBitmapFromView(R.drawable.ic_bus, act);
        bitmapTram = getMarkerBitmapFromView(R.drawable.ic_tram, act);
        busMarkers = new ArrayList<>();
    }

    public MyMap(Activity act, Bus bus){
        tribe = "bus";
        position = bus.position;
        zoom = 15;
        current_activity = act;
    }

    public MyMap(Activity act, UnderStop underStop){
        tribe = "stop";
        position = underStop.position;
        zoom = 15;
        current_activity = act;
    }

    public GoogleMap getMap() {
        return mMap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Warszawa
        mMap = googleMap;
        mMap.setMapStyle(new MapStyleOptions(MainActivity.current.getString(R.string.mapStyle)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
        if (first_start){
            sh = new StopsHandler(this);
            bh = new BusHandler(mMap);
            first_start = false;
            sh1 = sh;
        }
        else {
            sh1 = new StopsHandler(sh);
            Log.d("Place", sh.toString() + " " + sh1.toString());
            sh1.draw(mMap);
            if ( widenStop != null){
                sh1.stops.get(widenStop).drawUnderStops(mMap, widenStop);
                }
            bh.refresh(mMap);
        }
        locator = new Locator(mMap, current_activity, getMarkerBitmapFromView(R.drawable.ic_gps_location_symbol, current_activity));
        mMap.setOnMarkerClickListener(this);
    }



    @Override
    public String toString(){
        return String.valueOf(getPosition().latitude) +  " " + String.valueOf(getPosition().longitude);
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
        else if (title.contains("stop")){
            if (widenStop != null){
                sh1.stops.get(widenStop).detachUnderStops(mMap, widenStop);
            }
            widenStop = Integer.valueOf(title.replace("stop", ""));
            sh1.stops.get(widenStop).drawUnderStops(mMap, widenStop);
        }
        else if(title.contains("bus")){
            if (bh.buses.get(title.replace("bus;", "")).textMarker == null){
                if (lineNumber != null){
                    lineNumber.remove();
                    lineNumber = null;
                }
                lineNumber = bh.buses.get(title.replace("bus;", "")).drawText(mMap);
                return false;
            }
            String[] split = title.replace("bus;", "").split(";");
            Intent intent = new Intent(current_activity, BusActivity.class);
            intent.putExtra("line", split[0]);
            intent.putExtra("brigade", split[1]);
            Log.d(TAG, split[0] + "DDW" + split[1]);
            current_activity.startActivity(intent);
        }
        else if (title.contains(";")){
            Intent intent = new Intent(current_activity, StopActivity.class);
            String[] elements = title.split(";");
            int superId = Integer.valueOf(elements[0]);
            int underId = Integer.valueOf(elements[1]);
            intent.putExtra("superId", superId);
            intent.putExtra("underId", underId);
            current_activity.startActivity(intent);
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

    public static BitmapDescriptor getBitmap(String id){
        switch(id){
            case "super":
                return bitmapSuper;
            case "bus":
                return bitmapBus;
            case "tram":
                return bitmapTram;
            default:
                return bitmapUnder;
        }
    }

    public void move(LatLng position, int zoom){
        this.position = position;
        this.zoom = zoom;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
    }

    public static void addBusMarker(Marker marker){
        busMarkers.add(marker);
    }

    public static void detachBusMarkers(){
        for (Marker marker : busMarkers){
            marker.remove();
        }
        busMarkers.clear();
    }
}
