package com.apackage.ztmpk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public static boolean first_run = true;
    private MyMap map_reference;
    private static String TAG = "Mejn";
    public static FirebaseUser login;
    public static MainActivity current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        current = this;
        RegisterActivity.mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = RegisterActivity.mAuth.getCurrentUser();
        if (currentUser == null){
            startActivity(new Intent(this, RegisterActivity.class));
        }
        else {
            UpdateUI(currentUser);
        }

        if (first_run){
            first_run = false;
            map_reference = new MyMap(this);
        }
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mainMap);
        mapFragment.getMapAsync(map_reference);

        Button lokal = findViewById(R.id.goTo_location);
        lokal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Locator.getPosition() == null)
                    return;
                map_reference.move(Locator.getPosition(), 15);
            }
        });

        Button profil = findViewById(R.id.profile_button);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, profil_activity.class));
            }
        });

    }

    public void UpdateUI(FirebaseUser user){
        login = user;
    }

}
