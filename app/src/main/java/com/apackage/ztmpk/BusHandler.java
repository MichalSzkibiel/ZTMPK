package com.apackage.ztmpk;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BusHandler {
    private static String link_base = "https://api.um.warszawa.pl/api/action/busestrams_get/?resource_id=f2e5503e-927d-4ad3-9500-4ab9e55deb59&apikey=41877efd-2c98-432e-bbad-0c93fb56caff&type=";
    public Buses buses;
    private static String TAG = "tram";

    private class asyncDownload extends Thread {

        public asyncDownload(){
        }

        @Override
        public void run() {
            //while (!Thread.interrupted()) {
                for (int i = 1; i < 3; ++i) {
                    try {
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
                            sb.append(line + "\n");
                        }
                        br.close();
                        String text = sb.toString();
                        buses.addBuses(text, i);
                        connection.disconnect();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
           //     try {
           //         Thread.sleep(30000);
           //     } catch (InterruptedException e) {
           //         e.printStackTrace();
           //     }
           // }
        }
    }

    public BusHandler(){
        buses = new Buses();
        refresh();
    }

    public void refresh(){
        buses.detach();
        buses = new Buses();
        asyncDownload ad = new asyncDownload();
        ad.start();
        try {
            ad.join();
            draw();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void draw(){
        for (int i = 0; i < buses.size(); ++i){
            buses.get(i).draw(MainActivity.map_reference.getMap(), i);
        }
    }
}
