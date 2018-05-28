package com.apackage.ztmpk;

import android.util.Log;
import android.util.Pair;

import com.google.android.gms.maps.GoogleMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BusHandler {
    private static String link_base = "https://api.um.warszawa.pl/api/action/busestrams_get/?resource_id=f2e5503e-927d-4ad3-9500-4ab9e55deb59&apikey=41877efd-2c98-432e-bbad-0c93fb56caff&type=";
    public Buses buses;
    private static String TAG = "tram";

    class UpdatePositions extends TimerTask {
        @Override
        public void run() {
            (new Thread() {

                @Override
                public void run() {
                    Log.d(TAG, "wątek");
                    asyncDownload ad = new asyncDownload();
                    ad.start();
                    try {
                        ad.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private class asyncDownload extends Thread {

        public asyncDownload(){
        }

        @Override
        public void run() {
            //while (!Thread.interrupted()) {
                for (int i = 1; i < 3; ++i) {
                    try {
                        boolean is_good;
                        do {
                            URL line_url = new URL(link_base + String.valueOf(i));
                            Log.d(TAG, link_base + String.valueOf(i));
                            HttpURLConnection connection = (HttpURLConnection) line_url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.connect();
                            InputStream is = connection.getInputStream();

                            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = br.readLine()) != null) {
                                sb.append(line).append("\n");
                            }
                            br.close();
                            String text = sb.toString();
                            connection.disconnect();
                            try{
                                buses.addBuses(text, i);
                                is_good = true;
                            }
                            catch(NumberFormatException e){
                                is_good = false;
                            }
                        } while (!is_good);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public BusHandler(final MyMap map_reference){
        buses = new Buses();
        asyncDownload ad = new asyncDownload();
        ad.start();
        try {
            ad.join();
            draw(map_reference, "");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Timer time = new Timer();
        time.schedule(new UpdatePositions(), 30000,30000);
    }

    public void refresh(final MyMap map_reference, String currentBus){
        map_reference.detachBusMarkers();
        draw(map_reference, currentBus);
    }

    private void draw(final MyMap map_reference, String currentBus){
        for (String key : buses.keySet()){
            if (currentBus != null && key.equals(currentBus)){
                buses.get(key).drawActive(map_reference);
            }
            else{
                buses.get(key).draw(map_reference);
            }
        }
    }

    public Bus find(String line, String brigade){
        String pair = line + ";" + brigade;
        if (buses.containsKey(pair)){
            return buses.get(pair);
        }
        return null;
    }
}
