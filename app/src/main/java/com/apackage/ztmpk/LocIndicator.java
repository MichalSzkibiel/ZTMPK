package com.apackage.ztmpk;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

public class LocIndicator {
    LatLng pos;
    public LocIndicator(LatLng position){
        pos = position;
    }
    public void draw(GoogleMap mMap){
        mMap.addMarker(new MarkerOptions().title("Tu jesteś").position(pos).icon(defaultMarker(HUE_ORANGE)));
    }
}
