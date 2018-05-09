package com.apackage.ztmpk;

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
    public String id;
    public String name;
    public LatLng position;
    public ArrayList<UnderStop> underStops;
    private Marker stopMarker;

    public SuperStop(JSONObject json) throws JSONException {
        id = json.getString("id");
        name = json.getString("name").replace("_", " ");
        double lat = json.getDouble("lat");
        double lon = json.getDouble("lon");
        position = new LatLng(lat, lon);
        underStops = new ArrayList<>();
        JSONArray us =  json.getJSONArray("underStops");
        for (int i = 0; i < us.length(); ++i) {
            try {
                UnderStop element = new UnderStop(us.getJSONObject(i));
                underStops.add(element);
            } catch(JSONException e) {

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
    }

    public Marker getStopMarker() {
        return stopMarker;
    }
}
