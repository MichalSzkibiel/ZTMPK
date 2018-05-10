package com.apackage.ztmpk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
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

    public UnderStop(JSONObject json) throws JSONException {
            double lat = json.getDouble("lat");
            double lon = json.getDouble("lon");
            position = new LatLng(lat, lon);
            lines = new ArrayList<>();
            JSONArray constant = (JSONArray)json.get("constant");
            for (int i=0; i < constant.length(); ++i){
                lines.add(constant.getString(i));
            }
            JSONArray request = (JSONArray)json.get("request");
            for (int i=0; i < request.length(); ++i){
                lines.add(request.getString(i));
            }
            JSONArray sit_in = (JSONArray)json.get("sit_in");
            for (int i=0; i < sit_in.length(); ++i){
                lines.add(sit_in.getString(i));
            }
            JSONArray sit_out = (JSONArray)json.get("sit_out");
            for (int i=0; i < sit_out.length(); ++i){
                lines.add(sit_out.getString(i));
            }
            JSONArray end = (JSONArray)json.get("end");
            for (int i=0; i < end.length(); ++i){
                lines.add(end.getString(i));
            }

    }

    public void draw(GoogleMap mMap, Activity activity, int idx1, int idx2){
        //slup przystankowy
        MarkerOptions underMarker = new MarkerOptions();
        underMarker.position(position);
        underMarker.draggable(false);
        underMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        underMarker.title(String.valueOf(idx1) + ";" + String.valueOf(idx2));
        stopMarker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .draggable(false)
                .title(String.valueOf(idx1) + ";" + String.valueOf(idx2))
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.bus_stop, activity)))
        );
    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId, Activity activity) {

        View customMarkerView = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_stop, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public Marker getStopMarker() {
        return stopMarker;
    }
}
