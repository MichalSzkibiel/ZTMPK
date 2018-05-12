package com.apackage.ztmpk;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

public class LocIndicator {
    private Marker marker;
    private BitmapDescriptor bitmap;
    public LocIndicator(BitmapDescriptor bitmap){
        this.bitmap = bitmap;
    }
    public void draw(GoogleMap mMap, LatLng pos){
        if (marker != null){
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().title("Tu jesteś").position(pos).icon(bitmap));
    }
}
