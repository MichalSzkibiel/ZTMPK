package com.apackage.ztmpk;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class NotificationActivity extends Activity implements BusFragment.OnFragmentInteractionListener, StopFragment.OnFragmentInteractionListener{
    private Spinner spinner;
    private DatabaseReference dRef;
    public SuperStop superStop;
    public UnderStop underStop;
    public Bus bus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String id = intent.getStringExtra("id");
        if (savedInstanceState == null) {
            if (type.equals("bus")){
                String idx1 = intent.getStringExtra("idx1");
                String idx2 = intent.getStringExtra("idx2");
                Pair<String, String> pair = new Pair<>(idx1, idx2);
                if(!MyMap.bh.buses.containsKey(pair.toString())){
                    Toast.makeText(this, "Nie znaleziono autobusu", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                bus = MyMap.bh.buses.get(pair.toString());
                BusFragment fragment = BusFragment.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.info_fragment, fragment).commit();
            }
            else{
                int idx1 = intent.getIntExtra("idx1", -1);
                int idx2 = intent.getIntExtra("idx2", -1);
                superStop = MyMap.sh.stops.get(idx1);
                underStop = MyMap.sh.stops.get(idx1).underStops.get(idx2);
                StopFragment fragment = StopFragment.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.info_fragment, fragment).commit();
            }

        }
        FirebaseDatabase FD = FirebaseDatabase.getInstance();
        spinner = findViewById(R.id.notification_spinner);
        ArrayAdapter<String> adapter;
        if (type.equals("stop")){
            adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.stop_problems));
            dRef = FD.getReference("PrzystankiZgl");
            dRef = dRef.child(id);
        }
        else{
            adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.bus_problems));
            dRef = FD.getReference("PojazdyZgl");
            dRef = dRef.child(id);
        }
        final String typeCom = spinner.getSelectedItem().toString().replace(" ", "_").replace(",", "$");
        dRef = dRef.child(typeCom);
        spinner.setAdapter(adapter);

        Button commit = findViewById(R.id.send_notification);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.extra_edit);
                final String desc = editText.getText().toString().replace(" ", "_").replace(",", "$").replace("\"", "^");
                new AlertDialog.Builder(NotificationActivity.this)
                        .setMessage("Czy na pewno chcesz dokonać zgłoszenia o następującej treści:\n" + typeCom)
                        .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Map<String, Object> objectMap = createNotificationObject(desc, "yes");
                                dRef.setValue(MainActivity.login.getEmail(), objectMap);
                                dialogInterface.dismiss();
                                Toast.makeText(NotificationActivity.this, "Dokonano Zgłoszenia", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_notification);
    }

    static public Map<String, Object> createNotificationObject(String desc, String yesNo){
        Map<String, Object> toRet = new HashMap<>();
        toRet.put("user", MainActivity.login.getEmail());
        toRet.put("desc", desc);
        toRet.put("yesNo", yesNo);
        Date currentTime = Calendar.getInstance().getTime();
        toRet.put("time", currentTime.toString());
        return toRet;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
