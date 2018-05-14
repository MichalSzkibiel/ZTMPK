package com.apackage.ztmpk;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    static public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editEmail = findViewById(R.id.email);
                EditText editPassword = findViewById(R.id.password);
                EditText editPasswordRepeat = findViewById(R.id.password_return);
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                String passwordRepeat = editPasswordRepeat.getText().toString();
                if (password.length() < 5){
                    dialog("Hasło powinno zawierać co najmniej 5 znaków");
                    return;
                }
                else if (! password.equals(passwordRepeat)){
                    dialog("Powtórzenie hasła się nie zgadza");
                    return;
                }
                register(email, password);

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

    public void register(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            MainActivity.current.UpdateUI(user);
                            RegisterActivity.this.finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            MainActivity.login = null;
                        }

                    }
                });
    }

    @Override
    public void onBackPressed(){
    }

}
