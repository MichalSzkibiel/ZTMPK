package com.apackage.ztmpk;

import android.util.Log;
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
    public LatLng prevPosition;
    public int type;
    public Marker marker;
    public Marker textMarker;

    public Bus(String toParse, int type) throws NumberFormatException{
        String[] split = toParse.split(",");
        double lat = Double.valueOf(split[0].split(":")[1]);
        double lon = Double.valueOf(split[1].split(":")[1]);
        position = new LatLng(lat, lon);
        line = split[3].split("\"")[3];
        brigade = split[4].split("\"")[3];
        this.type = type;
    }

    public void detach() {
        if (marker != null) {
            marker.remove();
        }
        if (textMarker != null){
            textMarker.remove();
        }
    }

    public void draw(GoogleMap mMap){
        MarkerOptions mo = new MarkerOptions();
        mo.position(position);
        mo.title("bus;" + line + ";" + brigade);
        mo.draggable(false);
        if (prevPosition != null){
            double diffX = position.latitude - prevPosition.latitude;
            double diffY = position.longitude - prevPosition.longitude;
            mo.rotation((float)(Math.atan2(diffY, diffX)*180.0/Math.PI));
        }
        if (type == 1){
            mo.icon(MyMap.getBitmap("bus"));
        }
        else{
            mo.icon(MyMap.getBitmap("tram"));
        }
        marker = mMap.addMarker(mo);
        MyMap.addBusMarker(marker);
    }

    public Marker drawText(GoogleMap mMap){
        MarkerOptions label = new MarkerOptions();
        label.position(position);
        label.icon(SuperStop.createPureTextIcon(line));
        label.anchor(0.5f, 0.0f);
        if (prevPosition != null){
            double diffX = position.latitude - prevPosition.latitude;
            double diffY = position.longitude - prevPosition.longitude;
            Log.d("rotacja", String.valueOf(diffX) + " " + String.valueOf(diffY));
          label.rotation((float)(Math.atan2(diffY, diffX)*180.0/Math.PI));
        }
        textMarker = mMap.addMarker(label);
        return textMarker;
    }

    public boolean setPrevPosition(LatLng prevPosition){
        double diffX = position.latitude - prevPosition.latitude;
        double diffY = position.longitude - prevPosition.longitude;
        if (diffX == 0 && diffY == 0){
            return false;
        }
        this.prevPosition = prevPosition;
        return true;
    }
}
