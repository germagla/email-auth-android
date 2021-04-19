package com.gmagla.emailauthapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;

public class LoginMethods extends AppCompatActivity {
    ConstraintLayout smsCard, passwordCard, googleCard, loginRow1, loginRow2;
    Switch passModeSwitch;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_methods);

        firebaseAuth = FirebaseAuth.getInstance();
        smsCard = findViewById(R.id.login_sms_card);
        passwordCard = findViewById(R.id.login_email_card);
        googleCard = findViewById(R.id.login_google_card);
        passModeSwitch = findViewById(R.id.password_mode_switch);
        loginRow1 = findViewById(R.id.login_table_row_1);
        loginRow2 = findViewById(R.id.login_table_row_2);

        passModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (passModeSwitch.isChecked()) {
                    loginRow2.setVisibility(View.GONE);
                    loginRow1.setVisibility(View.VISIBLE);
                } else {
                    loginRow2.setVisibility(View.VISIBLE);
                    loginRow1.setVisibility(View.GONE);

                }
            }
        });

        passwordCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PasswordLogin.class));
            }
        });

        smsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PhoneSignIn.class));
                finish();
            }
        });

        googleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}