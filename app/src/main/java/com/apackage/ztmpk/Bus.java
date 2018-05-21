package com.apackage.ztmpk;

import android.util.Pair;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Bus {
    public String line;
    public String brigade;
    public LatLng position;
    public int type;
    private Marker marker;

    public Bus(String toParse, int type){
        String[] split = toParse.split(",");
        double lat = Double.valueOf(split[0].split(":")[1]);
        double lon = Double.valueOf(split[1].split(":")[1]);
        position = new LatLng(lat, lon);
        line = split[3].split("\"")[3];
        brigade = split[4].split("\"")[3];
        this.type = type;
    }

    public void detach(){
        marker.remove();
    }

    public void draw(GoogleMap mMap){
        MarkerOptions mo = new MarkerOptions();
        mo.position(position);
        mo.title("bus;" + line + ";" + brigade);
        mo.draggable(false);
        if (type == 1){
            mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }
        else{
            mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        marker = mMap.addMarker(mo);
    }
}
