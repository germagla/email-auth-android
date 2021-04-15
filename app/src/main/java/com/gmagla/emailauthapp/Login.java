package com.gmagla.emailauthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    Button createAccountBtn, loginBtn, resetPassBtn;
    EditText loginUsername, loginPassword;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder resetAlert;
    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createAccountBtn = findViewById(R.id.loginCreateAccountButton);
        loginUsername = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginButton);
        resetPassBtn = findViewById(R.id.loginResetPasswordBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        resetAlert = new AlertDialog.Builder(this);
        layoutInflater = this.getLayoutInflater();

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = layoutInflater.inflate(R.layout.reset_dialog, null);
                resetAlert.setTitle("Reset Forgotten Password?").setMessage("Enter your email address to receive a password reset link.")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText email = view1.findViewById(R.id.resetDialogField);
                                if (email.getText().toString().isEmpty()) {
                                    email.setError("Required Field");
                                    return;
                                }
                                firebaseAuth.sendPasswordResetEmail(email.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Login.this, "Reset Email Sent", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }).setNegativeButton("Cancel", null)
                        .setView(view1).create().show();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signInWithEmailAndPassword(loginUsername.getText().toString(), loginPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        switch (e.getClass().toString()) {
                            case "class com.google.firebase.auth.FirebaseAuthInvalidUserException":
                                loginUsername.setError(e.getMessage());
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                break;

                            case "class com.google.firebase.auth.FirebaseAuthInvalidCredentialsException":
                                loginPassword.setError(e.getMessage());
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                break;

                            default:
                                break;
                        }
                    }
                });
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}