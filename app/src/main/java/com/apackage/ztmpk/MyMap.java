package com.apackage.ztmpk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.os.AsyncTask;
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
import java.util.Timer;
import java.util.TimerTask;

import static com.apackage.ztmpk.Locator.getPosition;
//import static com.apackage.ztmpk.Locator.position;

public class MyMap implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private LatLng position;
    public int zoom;
    private Locator locator;
    private Activity current_activity;
    private static String TAG = "MyMap";
    public static StopsHandler sh;
    private static boolean first_start;
    private static BitmapDescriptor bitmapSuper;
    private static BitmapDescriptor bitmapUnder;
    private static BitmapDescriptor bitmapBus;
    private static BitmapDescriptor bitmapTram;
    private static BitmapDescriptor activeBitmapSuper;
    private static BitmapDescriptor activeBitmapUnder;
    private static BitmapDescriptor activeBitmapBus;
    private static BitmapDescriptor activeBitmapTram;
    public static BusHandler bh;
    private Integer widenStop;
    private String tribe;
    private StopsHandler sh1;
    private Marker lineNumber;
    private String currentBus;
    private static ArrayList<Marker> busMarkers;
    public final static int MARKER_REQUEST_CODE = 2132;
    private int superId;
    private int underId;
    public static String BusId;

    private class busRefresher extends TimerTask {

        @Override
        public void run() {
            (new AsyncTask<Void, Void, Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    return null;
                }
                @Override
                protected void onPostExecute(Void voids){
                    detachBusMarkers();
                    bh.refresh(mMap, BusId);
                    if (tribe.equals("bus")){
                        if (bh.buses.containsKey(BusId)){
                            move(bh.buses.get(BusId).position, zoom);
                        }
                    }
                }
            }).execute();
        }
    }

    public MyMap(Activity act) {
        tribe = "main";
        first_start = true;
        position = new LatLng(52.25, 21.0);
        zoom = 10;
        current_activity = act;
        currentBus = "";
        bitmapUnder = getMarkerBitmapFromView(R.drawable.bus_stop, act);
        bitmapSuper = getMarkerBitmapFromView(R.drawable.complex_stop, act);
        bitmapBus = getMarkerBitmapFromView(R.drawable.ic_bus, act);
        bitmapTram = getMarkerBitmapFromView(R.drawable.ic_tram, act);
        activeBitmapBus = getMarkerBitmapFromView(R.drawable.active_bus, act);
        activeBitmapSuper = getMarkerBitmapFromView(R.drawable.active_complex, act);
        activeBitmapTram = getMarkerBitmapFromView(R.drawable.active_tram, act);
        activeBitmapUnder = getMarkerBitmapFromView(R.drawable.active_stop, act);
        busMarkers = new ArrayList<>();
    }

    public MyMap(Activity act, String key){
        tribe = "bus";
        if (bh.buses.containsKey(key)) {
            Bus bus = bh.buses.get(key);
            position = bus.position;
            BusId = key;
        }
        zoom = 15;
        current_activity = act;
        currentBus = "";
    }

    public MyMap(Activity act, int superId, int underId){
        tribe = "stop";
        position = sh.stops.get(superId).underStops.get(underId).position;
        zoom = 15;
        current_activity = act;
        currentBus = "";
        this.superId = superId;
        this.underId = underId;
        widenStop = superId;
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
            sh1.draw(mMap);
            if ( tribe.equals("stop")){
                sh1.stops.get(widenStop).drawUnderStops(mMap, widenStop);
                sh1.stops.get(widenStop).underStops.get(underId).detach();
                sh1.stops.get(widenStop).underStops.get(underId).drawActive(mMap);
            }
            bh.refresh(mMap, BusId);
            if (lineNumber != null) {
                lineNumber.remove();
                if (bh.buses.containsKey(currentBus)){
                    lineNumber = bh.buses.get(currentBus).drawText(mMap);
                }
                else{
                    lineNumber = null;
                    currentBus = "";
                }
            }
        }
        locator = new Locator(mMap, current_activity, getMarkerBitmapFromView(R.drawable.ic_gps_location_symbol, current_activity));
        mMap.setOnMarkerClickListener(this);
        Timer timer = new Timer();
        timer.schedule(new busRefresher(), 30000, 30000);
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
                if (widenStop == superId){
                    sh1.stops.get(widenStop).setActiveMarker(mMap, widenStop);
                }
            }
            widenStop = Integer.valueOf(title.replace("stop", ""));
            sh1.stops.get(widenStop).drawUnderStops(mMap, widenStop);
            if (widenStop == superId){
                sh1.stops.get(widenStop).underStops.get(underId).detach();
                sh1.stops.get(widenStop).underStops.get(underId).drawActive(mMap);
            }
        }
        else if(title.contains("bus")){
            if (!currentBus.equals(title.replace("bus;", ""))){
                if (lineNumber != null){
                    lineNumber.remove();
                    lineNumber = null;
                }
                lineNumber = bh.buses.get(title.replace("bus;", "")).drawText(mMap);
                currentBus = title.replace("bus;", "");
                return false;
            }
            String[] split = title.replace("bus;", "").split(";");
            Intent intent = new Intent(current_activity, BusActivity.class);
            intent.putExtra("line", split[0]);
            intent.putExtra("brigade", split[1]);
            Log.d(TAG, split[0] + "DDW" + split[1]);
            current_activity.startActivityForResult(intent, MARKER_REQUEST_CODE);
        }
        else if (title.contains(";")){
            Intent intent = new Intent(current_activity, StopActivity.class);
            String[] elements = title.split(";");
            int superId = Integer.valueOf(elements[0]);
            int underId = Integer.valueOf(elements[1]);
            intent.putExtra("superId", superId);
            intent.putExtra("underId", underId);
            current_activity.startActivityForResult(intent, MARKER_REQUEST_CODE);
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

    public static BitmapDescriptor getActiveBitmap(String id){
        switch(id){
            case "super":
                return activeBitmapSuper;
            case "bus":
                return activeBitmapBus;
            case "tram":
                return activeBitmapTram;
            default:
                return activeBitmapUnder;
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
