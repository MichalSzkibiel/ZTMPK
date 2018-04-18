package com.apackage.ztmpk;

import android.os.Bundle;
import android.app.Activity;

public class BusActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
