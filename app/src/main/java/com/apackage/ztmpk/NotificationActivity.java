package com.apackage.ztmpk;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class NotificationActivity extends Activity {
    private Spinner spinner;
    private DatabaseReference dRef;
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
        FirebaseDatabase FD = FirebaseDatabase.getInstance();
        spinner = findViewById(R.id.notification_spinner);
        ArrayAdapter<String> adapter;
        if (type.equals("stop")){
            adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.stop_problems));
            dRef = FD.getReference("PrzystankiZgl");
            String id = MyMap.currentSuperStop.id + MyMap.currentUnderStop.id;
            dRef = dRef.getDatabase().getReference(id);
        }
        else{
            adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.bus_problems));
            dRef = FD.getReference("PojazdyZgl");
            String id = MyMap.currentBus.line + ";" + MyMap.currentBus.brigade;
            dRef = dRef.getDatabase().getReference(id);
        }
        final String typeCom = spinner.getSelectedItem().toString().replace(" ", "_").replace(",", "$");
        dRef = dRef.getDatabase().getReference(typeCom);
        spinner.setAdapter(adapter);

        Button commit = findViewById(R.id.send_notification);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.extra_edit);
                final String desc = editText.getText().toString().replace(" ", "_").replace(",", "$").replace("\"", "^");
                final Date currentTime = Calendar.getInstance().getTime();
                new AlertDialog.Builder(NotificationActivity.this)
                        .setMessage("Czy na pewno chcesz dokonać zgłoszenia o następującej treści:\n" + typeCom)
                        .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String text = dRef.toString();
                                        if (text.equals("")) {
                                            dRef.setValue("user", MainActivity.login.getEmail());
                                            dRef.setValue("desc", desc);
                                            dRef.setValue("yes", 1);
                                            dRef.setValue("no", 0);
                                            dRef.setValue("time", currentTime.toString());
                                        }
                                        else {
                                            try {
                                                JSONObject json = new JSONObject(text);
                                                int yes = json.getInt("yes");
                                                dRef.setValue("yes", yes + 1);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                NotificationActivity.this.finish();
            }
        });
    }

}
