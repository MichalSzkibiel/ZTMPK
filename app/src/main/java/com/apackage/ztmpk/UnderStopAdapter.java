package com.apackage.ztmpk;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static java.lang.Math.min;

class UnderStopAdapter extends RecyclerView.Adapter {

    private final RecyclerView mRecyclerView;

    private class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView lines;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.stop_cardView_name);
            lines = itemView.findViewById(R.id.stop_cardView_lines);
        }
    }

    private int idx;
    private SuperStop stop;

    public UnderStopAdapter(RecyclerView mRecyclerView,int idx, SuperStop stop) {
        this.idx = idx;
        this.stop = stop;
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.stop_card_view, viewGroup, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idx2 = mRecyclerView.getChildAdapterPosition(v);
                Intent intent = new Intent(mRecyclerView.getContext(), StopActivity.class);
                intent.putExtra("superId", idx);
                intent.putExtra("underId", idx2);
                mRecyclerView.getContext().startActivity(intent);
            }
        });
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        String stopDesc = stop.name + " " + stop.underStops.get(i).id;
        if (!stop.borough.equals("WARSZAWA")){
            stopDesc += "(" + stop.borough + ")";
        }
        ((MyViewHolder) viewHolder).name.setText(stopDesc);
        ((MyViewHolder) viewHolder).lines.setText(stop.underStops.get(i).lines.toString().replace("[", "").replace("]", ""));

    }

    @Override
    public int getItemCount() {
        return stop.underStops.size();
    }
}
