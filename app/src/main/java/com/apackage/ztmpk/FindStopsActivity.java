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

public class FindStopsActivity extends AppCompatActivity {

    private RecyclerView underStopsView;
    private AutoCompleteTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_stops);

        underStopsView = findViewById(R.id.found_cardView_stop);
        underStopsView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        underStopsView.setLayoutManager(mLayoutManager);

        textView = findViewById(R.id.find_stops_view);
        final String[] stop_names = MyMap.sh.stop_names();
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
        Button show = findViewById(R.id.search_button);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("znajdz", "Callback");
                int position = -1;
                for (int i = 0; i < stop_names.length; ++i){
                    if (stop_names[i].equals(textView.getText().toString())){
                        position = i;
                        break;
                    }
                }
                SuperStop stop = MyMap.sh.stops.get(position);
                underStopsView.setAdapter(new UnderStopAdapter(underStopsView, position, stop));
            }
        });
    }

}
