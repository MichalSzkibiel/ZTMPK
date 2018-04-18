package com.apackage.ztmpk;

import android.os.Bundle;
import android.app.Activity;

public class StopActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
