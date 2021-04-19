package com.gmagla.emailauthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gmagla.emailauthapp.databinding.ActivityPhoneSignInBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.SplittableRandom;
import java.util.concurrent.TimeUnit;

public class PhoneSignIn extends AppCompatActivity {
    private ActivityPhoneSignInBinding binding;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private final String TAG = "MAIN TAG";
    private FirebaseAuth firebaseAuth;
    String phone_Number,code, signTextView1, signTextView2, verifTextView1, verifTextView2, verifTextView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneSignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        //

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(PhoneSignIn.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(s, forceResendingToken);

                Log.d(TAG, "onCodeSent: "+ s);

                mVerificationId = s;
                forceResendingToken = token;

                binding.signinTextview1.setVisibility(View.GONE);
                binding.signinTextview2.setVisibility(View.GONE);
                binding.signinGetcode.setVisibility(View.GONE);
                binding.phoneSigninPhoneNum.setVisibility(View.GONE);

                binding.verifCodeSubmitCode.setVisibility(View.VISIBLE);
                binding.verifCodeTextview2.setVisibility(View.VISIBLE);
                binding.verifCodeTextview2.setVisibility(View.VISIBLE);
                binding.verifCodeTextview3Resend.setVisibility(View.VISIBLE);

                Toast.makeText(PhoneSignIn.this, "Verification code sent...", Toast.LENGTH_SHORT).show();



            }
        };

        binding.signinGetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_Number = binding.phoneSigninPhoneNum.getText().toString().trim();
                if(TextUtils.isEmpty(phone_Number)){
                    Toast.makeText(PhoneSignIn.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(PhoneSignIn.this, "PROCESSING\nPLEASE WAIT", Toast.LENGTH_SHORT).show();
                    startPhoneNumberVerification(phone_Number);
                }
            }
        });

        binding.verifCodeTextview3Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_Number = binding.phoneSigninPhoneNum.getText().toString().trim();
                if(TextUtils.isEmpty(phone_Number)){
                    Toast.makeText(PhoneSignIn.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                }else {
                    resendVerificationCode(phone_Number, forceResendingToken);
                }

            }
        });

        binding.verifCodeSubmitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = binding.verifCodeCode.getText().toString();
                if(code.isEmpty()){
                    Toast.makeText(PhoneSignIn.this, "Please enter code", Toast.LENGTH_SHORT).show();
                }else {
                    verifyPhoneNumberWithCode(phone_Number, code);
                }
            }
        });

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                Toast.makeText(PhoneSignIn.this, "Logged in as "+phone, Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PhoneSignIn.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resendVerificationCode(String phone_number, PhoneAuthProvider.ForceResendingToken token) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone_number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private void startPhoneNumberVerification(String phone_number) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone_number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
}