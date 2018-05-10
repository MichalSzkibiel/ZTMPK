package com.apackage.ztmpk;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SuperStop {
    public int id;
    public String name;
    public String borough;
    public LatLng position;
    public ArrayList<UnderStop> underStops;
    private Marker stopMarker;
    private static String TAG = "SuperStop";

    public SuperStop(JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
        name = name.replace("_", " ");
        borough = json.getString("borough");
        borough = borough.replace("_", " ");
        Double lat = json.getDouble("lat");
        Double lon = json.getDouble("lon");
        position = new LatLng(lat, lon);
        underStops = new ArrayList<>();
        JSONArray us =  json.getJSONArray("under_stops");
        for (int i = 0; i < us.length(); ++i) {
            try {
                Log.d(TAG, us.getJSONObject(i).toString());
                UnderStop element = new UnderStop(us.getJSONObject(i));
                underStops.add(element);
            } catch(JSONException e) {
                Log.d(TAG, "Coś poszło nie tak");
            }
        }
    }

    public void draw(GoogleMap mMap, int idx){
        MarkerOptions superMarker = new MarkerOptions();
        superMarker.position(position);
        superMarker.draggable(false);
        superMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        superMarker.title(String.valueOf(idx));
        for (int i = 0; i < underStops.size(); ++i){
            underStops.get(i).draw(mMap, idx, i);
        }
        stopMarker = mMap.addMarker(superMarker);
        Log.d(TAG, "Przystanek");

    }

    public Marker getStopMarker() {
        return stopMarker;
    }
}
