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


    public SuperStopAdapter(RecyclerView mRecyclerView, LatLng position) {
        this.mRecyclerView = mRecyclerView;
        data = new ArrayList<>();
        for (SuperStop stop : MyMap.sh.stops) {
            double distance = Math.sqrt(Math.pow(stop.position.latitude - position.latitude, 2) + Math.pow(stop.position.longitude - position.longitude, 2));
            data.add(new Pair<>(stop, distance));
        }

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
        ((MyViewHolder) viewHolder).distance.setText(String.valueOf(data.get(i).second));

    }

    @Override
    public int getItemCount() {
        return Math.min(10, data.size());
    }
}
