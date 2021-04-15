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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    EditText newPassword, newPasswordConf, oldPassword;
    Button resetPassBtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password2);

        newPassword = findViewById(R.id.newPassword);
        newPasswordConf = findViewById(R.id.newPasswordConf);
        oldPassword = findViewById(R.id.oldPassword);
        resetPassBtn = findViewById(R.id.resetPasswordBtn);
        firebaseAuth = FirebaseAuth.getInstance();

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!newPassword.getText().toString().equals(newPasswordConf.getText().toString())){
                    newPassword.setError("Passwords do not match");
                    newPasswordConf.setError("Passwords do not match");
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(firebaseAuth.getCurrentUser().getEmail(),oldPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        firebaseAuth.getCurrentUser().updatePassword(newPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), Login.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        switch (e.getClass().toString()){

                            case "class com.google.firebase.auth.FirebaseAuthInvalidCredentialsException":
                                oldPassword.setError(e.getMessage());
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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