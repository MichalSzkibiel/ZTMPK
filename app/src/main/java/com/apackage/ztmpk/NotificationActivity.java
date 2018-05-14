package com.apackage.ztmpk;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        Fragment fragment;
        if (savedInstanceState == null) {
            if (type.equals("bus")){
                fragment = BusFragment.newInstance();
            }
            else{
                fragment = StopFragment.newInstance();
            }
            getFragmentManager().beginTransaction().replace(R.id.info_fragment, fragment).commit();
        }
        Spinner spinner = findViewById(R.id.notification_spinner);
        ArrayAdapter<String> adapter;
        if (type.equals("stop")){
            adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.stop_problems));
        }
        else{
            adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.bus_problems));
        }
        spinner.setAdapter(adapter);
    }

}
