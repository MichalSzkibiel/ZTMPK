package com.apackage.ztmpk;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class Locator{
    private LatLng position;
    private GoogleMap mMap;
    private LocationCallback callback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            //Po każdej lokalizacji należy:
            if (locationResult.getLastLocation() != null) {
                //Pobrać szerokość i długość geograficzną oraz dokładność lokalizacji
                double lat = locationResult.getLastLocation().getLatitude();
                double lon = locationResult.getLastLocation().getLongitude();
                double accuracy = locationResult.getLastLocation().getAccuracy();
                //Przypisanie współrzędnych do pozycji
                position = new LatLng(lat, lon);
                LocIndicator locIndicator = new LocIndicator(position);
                locIndicator.draw(mMap);
            }
        }
    };

    @SuppressLint("MissingPermission")
    public Locator(GoogleMap map, Activity activity){
        mMap = map;
        FusedLocationProviderClient locator = LocationServices.getFusedLocationProviderClient(activity);
        @SuppressLint("RestrictedApi") LocationRequest request = new LocationRequest();
        request.setInterval(1000);

        locator.requestLocationUpdates(request, callback, null);
    }

    public  LatLng getPosition(){
        return position;
    }
}
