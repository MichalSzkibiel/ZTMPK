package com.apackage.ztmpk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

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
import java.util.HashSet;
import java.util.Set;

public class SuperStop {
    public int id;
    public String name;
    public String borough;
    public LatLng position;
    public ArrayList<UnderStop> underStops;
    private Marker stopMarker;
    private static String TAG = "SuperStop";

    public SuperStop(JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
        name = name.replace("_", " ");
        borough = json.getString("borough");
        borough = borough.replace("_", " ");
        Double lat = json.getDouble("lat");
        Double lon = json.getDouble("lon");
        position = new LatLng(lat, lon);
        underStops = new ArrayList<>();
        JSONArray us =  json.getJSONArray("under_stops");
        for (int i = 0; i < us.length(); ++i) {
            try {
                UnderStop element = new UnderStop(us.getJSONObject(i));
                underStops.add(element);
            } catch(JSONException e) {
                Log.d(TAG, "Coś poszło nie tak");
            }
        }
    }

    public SuperStop(SuperStop orig){
        id = orig.id;
        name = orig.name;
        borough = orig.borough;
        position = orig.position;
        underStops = new ArrayList<>();
        for (UnderStop stop : orig.underStops){
            underStops.add(new UnderStop(stop));
        }
    }

    public void draw(GoogleMap mMap, int idx){
        //zespoly
        MarkerOptions superMarker = new MarkerOptions();
        superMarker.position(position);
        superMarker.draggable(false);
        superMarker.icon(MyMap.getBitmap("super"));
        superMarker.title("stop" + String.valueOf(idx));
        stopMarker = mMap.addMarker(superMarker);
    }

    public void drawUnderStops(GoogleMap mMap, int idx){
        for (int i = 0; i < underStops.size(); ++i){
            underStops.get(i).draw(mMap, idx,  i);
        }
        if (this.stopMarker != null)
            this.stopMarker.remove();
        MarkerOptions label = new MarkerOptions();
        label.position(position);
        label.icon(createPureTextIcon(name));
        label.anchor(0.5f, 0.5f);
        stopMarker = mMap.addMarker(label);
    }

    public static BitmapDescriptor createPureTextIcon(String text) {

        Paint textPaint = new Paint(); // Adapt to your needs

        textPaint.setTextSize(50.0f);

        float textWidth = textPaint.measureText(text);
        float textHeight = textPaint.getTextSize();
        int width = (int) (textWidth);
        int height = (int) (textHeight);

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        canvas.translate(0, height);

        // For development only:
        // Set a background in order to see the
        // full size and positioning of the bitmap.
        // Remove that for a fully transparent icon.
        canvas.drawColor(Color.LTGRAY);

        canvas.drawText(text, 0, 0, textPaint);
        return BitmapDescriptorFactory.fromBitmap(image);
    }

    public void detachUnderStops(GoogleMap mMap, int idx){
        if (stopMarker != null) {
            stopMarker.remove();
        }
        for(int i = 0; i < underStops.size(); ++i){
            underStops.get(i).detach();
        }
        draw(mMap, idx);
    }

    public void setActiveMarker(GoogleMap mMap, int idx){
        if (stopMarker != null) {
            stopMarker.remove();
        }
        MarkerOptions superMarker = new MarkerOptions();
        superMarker.position(position);
        superMarker.draggable(false);
        superMarker.icon(MyMap.getActiveBitmap("super"));
        superMarker.title("stop" + String.valueOf(idx));
        stopMarker = mMap.addMarker(superMarker);
    }

    @Override
    public String toString(){
        String toRet = name;
        if (!borough.equals("WARSZAWA")){
            toRet += " (" + borough + ")";
        }
        return toRet;
    }

    public Marker getStopMarker() {
        return stopMarker;
    }

    public String getLines(){
        Set<String> lines = new HashSet<>();
        for (UnderStop stop : underStops){
            lines.addAll(stop.lines);
        }
        return lines.toString().replace("[", "").replace("]", "");
    }
}
