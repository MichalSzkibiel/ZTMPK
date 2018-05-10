package com.apackage.ztmpk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.Log;
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

public class SuperStop {
    public String id;
    public String name;
    public LatLng position;
    public ArrayList<UnderStop> underStops;
    private Marker stopMarker;
    private static String TAG = "SuperStop";

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
                Log.d(TAG, "Coś poszło nie tak");
            }
        }
    }

    public void draw(GoogleMap mMap, Activity activity, int idx){
        //zespoly
        MarkerOptions superMarker = new MarkerOptions();
        superMarker.position(position);
        superMarker.draggable(false);
        superMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        superMarker.title(String.valueOf(idx));
        for (int i = 0; i < underStops.size(); ++i){
            underStops.get(i).draw(mMap, activity, idx,  i);
        }
        stopMarker = mMap.addMarker(new MarkerOptions()
        .position(position)
        .draggable(false)
        .title(String.valueOf(idx))
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.bus_stop, activity)))
        );


        Log.d(TAG, "Przystanek");

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
