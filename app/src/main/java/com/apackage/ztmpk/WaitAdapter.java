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
import android.widget.Toast;

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

public class WaitAdapter extends RecyclerView.Adapter {

    private final static String text = "Poczekaj, aż załaduje się rozkład jazdy z przystanku.";

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

    // konstruktor adaptera
    public WaitAdapter(){
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bus_card_view, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        ((MyViewHolder) viewHolder).direction.setText(text);
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
