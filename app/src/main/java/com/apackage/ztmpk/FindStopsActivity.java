package com.apackage.ztmpk;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class FindStopsActivity extends AppCompatActivity {

    private RecyclerView underStopsView;
    private RecyclerView superStopsView;
    private AutoCompleteTextView textView;
    private Button show;
    private String[] stop_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_stops);

        superStopsView = findViewById(R.id.closest_cardView_stop);
        superStopsView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        superStopsView.setLayoutManager(mLayoutManager);
        LatLng pos = Locator.getPosition();
        if (pos == null){
            pos = new LatLng(52.25, 21.0);
        }
        superStopsView.setAdapter(new SuperStopAdapter(superStopsView, pos));

        underStopsView = findViewById(R.id.found_cardView_stop);
        underStopsView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        underStopsView.setLayoutManager(mLayoutManager2);

        textView = findViewById(R.id.find_stops_view);
        stop_names = MyMap.sh.stop_names();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stop_names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        textView.setAdapter(adapter);
        textView.setThreshold(1);
        textView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("znajdz", "Callback");
                SuperStop stop = MyMap.sh.stops.get(position);
                underStopsView.setAdapter(new UnderStopAdapter(underStopsView, position, stop));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        show = findViewById(R.id.search_button);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateLowerRecyclerView(textView.getText().toString());
            }
        });
    }

    private void populateLowerRecyclerView(String stopStr){
        Log.d("znajdz", "Callback");
        int position = -1;
        for (int i = 0; i < stop_names.length; ++i){
            if (stop_names[i].equals(stopStr)){
                position = i;
                break;
            }
        }
        if (position == -1){
            Toast.makeText(FindStopsActivity.this, "Nie znaleziono przystanku", Toast.LENGTH_SHORT);
            return;
        }
        SuperStop stop = MyMap.sh.stops.get(position);
        underStopsView.setAdapter(new UnderStopAdapter(underStopsView, position, stop));
    }

    public void stopFromUpperRecyclerView(String stop){
        populateLowerRecyclerView(stop);
    }

}
