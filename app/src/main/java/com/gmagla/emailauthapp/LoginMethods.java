package com.gmagla.emailauthapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class LoginMethods extends AppCompatActivity {
    ConstraintLayout smsCard, passwordCard, googleCard;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_methods);

        firebaseAuth = FirebaseAuth.getInstance();
        smsCard = findViewById(R.id.login_sms_card);
        passwordCard = findViewById(R.id.login_email_card);
        googleCard = findViewById(R.id.login_google_card);

        passwordCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PasswordLogin.class));
            }
        });

        smsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        googleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }
}