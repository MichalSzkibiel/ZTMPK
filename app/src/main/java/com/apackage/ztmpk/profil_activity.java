package com.apackage.ztmpk;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;

public class profil_activity extends AppCompatActivity {

    private EditText pass1;
    private EditText pass2;
    private EditText pass3;
    private Button confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_activity);
        TextView email = findViewById(R.id.loginView);
        email.setText(MainActivity.login.getEmail());
        Button change = findViewById(R.id.button);
        pass1 = findViewById(R.id.current_Password);
        pass2 = findViewById(R.id.new_Password);
        pass3 = findViewById(R.id.new_replay_Password);
        confirm = findViewById(R.id.check_button);
        Button ret=findViewById(R.id.retButton);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass1.setVisibility(View.VISIBLE);
                pass2.setVisibility(View.VISIBLE);
                pass3.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.VISIBLE);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password1 = pass1.getText().toString();
                final String password2 = pass2.getText().toString();
                final String password3 = pass3.getText().toString();
                changePassword(password1, password2, password3);
            }
        });

        Button log_out = findViewById(R.id.log_out_button);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.mAuth.signOut();
                startActivity(new Intent(profil_activity.this, LoginActivity.class));
                finish();
            }
        });
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changePassword(String pass_pr, final String pass, String pass_repeat){
        if (pass.length() < 5){
            dialog("Hasło powinno zawierać co najmniej 5 znaków");
            return;
        }
        else if (! pass.equals(pass_repeat)){
            dialog("Powtórzenie hasła się nie zgadza");
            return;
        }
        AuthCredential credential = EmailAuthProvider
                .getCredential(MainActivity.login.getEmail(), pass_pr);

        MainActivity.login.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            MainActivity.login.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(profil_activity.this, "Hasło zostało zmienione.",
                                                Toast.LENGTH_SHORT).show();
                                        pass1.setVisibility(View.INVISIBLE);
                                        pass2.setVisibility(View.INVISIBLE);
                                        pass3.setVisibility(View.INVISIBLE);
                                        confirm.setVisibility(View.INVISIBLE);
                                    } else {
                                        Toast.makeText(profil_activity.this, "Nie zmieniono hasła.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(profil_activity.this, "Wpisano niepoprawne hasło.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void dialog(String text){
        new AlertDialog.Builder(this)
                .setMessage(text)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }
}
