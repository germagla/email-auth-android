package com.gmagla.emailauthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button mainLogoutBtn, verifyBtn;
    FirebaseAuth firebaseAuth;
    TextView verifyMsg;
    AlertDialog.Builder resetAlert;
    LayoutInflater layoutInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLogoutBtn = findViewById(R.id.mainLogOutBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        verifyMsg = findViewById(R.id.mainVerifyWarningTxtView);
        verifyBtn = findViewById(R.id.mainVerifyBtn);
        resetAlert = new AlertDialog.Builder(this);
        layoutInflater = this.getLayoutInflater();


        if (!firebaseAuth.getCurrentUser().isEmailVerified()) {
            verifyBtn.setVisibility(View.VISIBLE);
            verifyMsg.setVisibility(View.VISIBLE);
        }

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Verification Email Sent.", Toast.LENGTH_SHORT).show();
                        verifyBtn.setVisibility(View.GONE);
                        verifyMsg.setVisibility(View.GONE);

                    }
                });
            }
        });

        mainLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.resetPasswordMenuItem:
                startActivity(new Intent(getApplicationContext(), ResetPassword.class));
                break;

            case R.id.resetEmailMenuItem:
                View view1 = layoutInflater.inflate(R.layout.reset_dialog, null);
                resetAlert.setTitle("Update Email Address?").setMessage("Enter new email address.")
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText email = view1.findViewById(R.id.resetDialogField);
                                if (email.getText().toString().isEmpty()) {
                                    email.setError("Required Field");
                                    return;
                                }
                                firebaseAuth.getCurrentUser().updateEmail(email.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(MainActivity.this, "Email Updated", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }).setNegativeButton("Cancel", null)
                        .setView(view1).create().show();

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}