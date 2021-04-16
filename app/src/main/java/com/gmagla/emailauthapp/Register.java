package com.gmagla.emailauthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText registerFullName, registerEmail, registerPassword, registerConfirmPassword;
    Button registerBtn, registerLoginBtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        registerFullName = findViewById(R.id.registerFullName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfirmPassword = findViewById(R.id.registerConfirmPassword);
        registerBtn = findViewById(R.id.registerButton);
        registerLoginBtn = findViewById(R.id.registerLoginBtn);

        registerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PasswordLogin.class));
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!registerPassword.getText().toString().equals(registerConfirmPassword.getText().toString())){
                    registerPassword.setError("Passwords do not match");
                    registerConfirmPassword.setError("Passwords do not match");
                    return;
                }

                //Form is valid
                //Register user with firebase
                Toast.makeText(Register.this, "Data Validated", Toast.LENGTH_SHORT).show();
                firebaseAuth.createUserWithEmailAndPassword(registerEmail.getText().toString(),registerPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        switch (e.getClass().toString()){
                            case "class com.google.firebase.auth.FirebaseAuthWeakPasswordException":
                                registerPassword.setError(e.getMessage());
                                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                break;

                            case "class com.google.firebase.auth.FirebaseAuthInvalidCredentialsException":

                            case "class com.google.firebase.auth.FirebaseAuthUserCollisionException":
                                registerEmail.setError(e.getMessage());
                                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                break;

                            default:
                                break;
                        }
                    }
                });
            }
        });
    }
}