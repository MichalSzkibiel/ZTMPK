package com.apackage.ztmpk;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.Math.min;

class SuperStopAdapter extends RecyclerView.Adapter {

    private final RecyclerView mRecyclerView;
    private ArrayList<Pair<SuperStop, Double>> data;

    private class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView lines;
        private TextView distance;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.stop_cardView_name);
            lines = itemView.findViewById(R.id.stop_cardView_lines);
            distance = itemView.findViewById(R.id.stop_cardView_distance);
        }
    }

    public static double distance(double lat_a, double lng_a, double lat_b, double lng_b)
    {
        //Funkcja licząca odległość pomiędzy punktami na sferze
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        double meterConversion = 1609.0;

        return distance * meterConversion;
    }


    public SuperStopAdapter(RecyclerView mRecyclerView, LatLng position) {
        this.mRecyclerView = mRecyclerView;
        data = new ArrayList<>();
        for (SuperStop stop : MyMap.sh.stops) {
            double distance = distance(position.latitude, position.longitude, stop.position.latitude, stop.position.longitude);
            data.add(new Pair<>(stop, distance));
        }
        Collections.sort(data, new Comparator<Pair<SuperStop, Double>>() {
            @Override
            public int compare(Pair<SuperStop, Double> o1, Pair<SuperStop, Double> o2) {
                if (o1.second > o2.second){
                    return 1;
                }
                else if(o1.second < o2.second){
                    return -1;
                }
                return 0;
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.stop_card_view, viewGroup, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idx = mRecyclerView.getChildAdapterPosition(v);
                ((FindStopsActivity)mRecyclerView.getContext()).stopFromUpperRecyclerView(data.get(idx).first.toString());
            }
        });
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        ((MyViewHolder) viewHolder).name.setText(data.get(i).first.toString());
        ((MyViewHolder) viewHolder).lines.setText(data.get(i).first.getLines());
        String distText = String.valueOf(Math.round(data.get(i).second)) + " m";
        ((MyViewHolder) viewHolder).distance.setText(distText);

    }

    @Override
    public int getItemCount() {
        return Math.min(10, data.size());
    }
}
