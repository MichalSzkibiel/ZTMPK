package com.apackage.ztmpk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

public class Locator {
    private static int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    private static LatLng position;
    private GoogleMap mMap;
    private LocIndicator locIndicator;
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
                locIndicator.draw(mMap, position);
            }
        }
    };

    public Locator(GoogleMap map, Activity activity, BitmapDescriptor bitmap) {
        mMap = map;
        locIndicator = new LocIndicator(bitmap);
        FusedLocationProviderClient locator = LocationServices.getFusedLocationProviderClient(activity);
        @SuppressLint("RestrictedApi") LocationRequest request = new LocationRequest();
        request.setInterval(1000);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        locator.requestLocationUpdates(request, callback, null);
    }

    public static LatLng getPosition(){
        return position;
    }
}
