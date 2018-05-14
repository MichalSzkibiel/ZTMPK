package com.apackage.ztmpk;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Created by trole_000 on 2018-04-09.
 * Klasa opisująca RecyclerView przechowującego listę zadań
 */

public class Timetable extends RecyclerView.Adapter {

    private Departures departures;

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView time;
        private TextView direction;
        private TextView delay;
        private TextView line;

        MyViewHolder(View pItem) {
            super(pItem);
            time = pItem.findViewById(R.id.bus_cardView_time);
            direction = pItem.findViewById(R.id.bus_cardView_direction);
            delay = pItem.findViewById(R.id.bus_cardView_delay);
            line = pItem.findViewById(R.id.bus_cardView_line);
        }
    }

    private class asyncDownload extends Thread {

        @Override
        public void run() {
            departures = new Departures();
            for (int i = 0; i < MyMap.currentUnderStop.lines.size();++i) {
                String busstopId = "busstopId=" + String.valueOf(MyMap.currentSuperStop.id);
                String busstopNm = "busstopNr=" + MyMap.currentUnderStop.id;
                String line = "line=" + MyMap.currentUnderStop.lines.get(i);
                String apikey = "apikey=41877efd-2c98-432e-bbad-0c93fb56caff";
                String link_base = "https://api.um.warszawa.pl/api/action/dbtimetable_get/?id=e923fa0e-d96c-43f9-ae6e-60518c9f3238";
                String link = link_base + "&" + busstopId + "&" + busstopNm + "&" + line + "&" + apikey;
                try {
                    URL line_url = new URL(link);
                    HttpURLConnection connection = (HttpURLConnection) line_url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    InputStream is = connection.getInputStream();

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
                    String text = sb.toString();
                    departures.addLine(MyMap.currentUnderStop.lines.get(i), text);
                    connection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            departures.sortByCurrentTime();
        }
    }

    // konstruktor adaptera
    public Timetable(RecyclerView pRecyclerView){
        asyncDownload ad = new asyncDownload();
        ad.start();
        try {
            ad.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bus_card_view, viewGroup, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String line = departures.get(i).line;
                String brigade = departures.get(i).brigade;
                int idx = MyMap.bh.find(line, brigade);
                if (idx == -1){
                    new AlertDialog.Builder(MainActivity.map_reference.getCurrent_activity())
                            .setMessage("Nie znaleziono pojazdu")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                    return;
                }
                MainActivity.map_reference.updateBus(idx);
            }
        });

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        ((MyViewHolder) viewHolder).time.setText(departures.get(i).time);
        ((MyViewHolder) viewHolder).direction.setText(departures.get(i).headsign);
        ((MyViewHolder) viewHolder).delay.setText("0");
        ((MyViewHolder) viewHolder).line.setText(departures.get(i).line);
    }

    @Override
    public int getItemCount() {
        return min(6, departures.size());
    }
}