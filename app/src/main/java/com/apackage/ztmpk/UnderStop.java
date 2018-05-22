package com.apackage.ztmpk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UnderStop {
    public String id;
    public ArrayList<String> lines;
    public LatLng position;
    private Marker stopMarker;
    private static String TAG = "UnderStop";

    public UnderStop(JSONObject json) throws JSONException {
            int intId = json.getInt("id");
            id = String.valueOf(intId);
            if (id.length() == 1){
                id = "0" + id;
            }
            double lat = json.getDouble("lat");
            double lon = json.getDouble("lon");
            position = new LatLng(lat, lon);
            lines = new ArrayList<>();
            try {
                JSONArray constant = (JSONArray) json.get("constant");
                for (int i = 0; i < constant.length(); ++i) {
                    lines.add(constant.getString(i));
                }
            } catch(JSONException e){
            }
            try {
                JSONArray request = (JSONArray) json.get("request");
                for (int i = 0; i < request.length(); ++i) {
                    lines.add(request.getString(i));
                }
            } catch(JSONException e){
            }
            try {
                JSONArray sit_in = (JSONArray) json.get("sit_in");
                for (int i = 0; i < sit_in.length(); ++i) {
                    lines.add(sit_in.getString(i));
                }
            } catch(JSONException e){
            }
            try {
                JSONArray sit_out = (JSONArray) json.get("sit_out");
                for (int i = 0; i < sit_out.length(); ++i) {
                    lines.add(sit_out.getString(i));
                }
            } catch(JSONException e){
            }
            try {
                JSONArray end = (JSONArray) json.get("end");
                for (int i = 0; i < end.length(); ++i) {
                    lines.add(end.getString(i));
                }
            } catch(JSONException e){
            }

    }

    public void draw(GoogleMap mMap, int idx1, int idx2){
        //slup przystankowy
        MarkerOptions underMarker = new MarkerOptions();
        underMarker.position(position);
        underMarker.draggable(false);
        underMarker.icon(MyMap.getBitmap("under"));
        underMarker.title(String.valueOf(idx1) + ";" + String.valueOf(idx2));
        stopMarker = mMap.addMarker(underMarker);
    }

    public void detach() {
        if (stopMarker != null)
            stopMarker.remove();
    }
}
