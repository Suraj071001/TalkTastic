package com.example.android.talktastic.auth;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.talktastic.MainActivity;
import com.example.android.talktastic.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyNumber extends AppCompatActivity {
    public static final String NUMBER_OTP = "otp";
    public static final String USER_NUMBER = "number";
    FirebaseAuth mAuth;
    EditText number_editText;
    Button generate_otp;
    ProgressBar progressBar;


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_number);

        initView();

        mAuth = FirebaseAuth.getInstance();


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                showGenerateOtp();
                SignInWithCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if(e instanceof FirebaseAuthInvalidUserException){
                    Toast.makeText(VerifyNumber.this, "Invalid Number: "+ number_editText, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(VerifyNumber.this, "Error occurred("+e+")", Toast.LENGTH_SHORT).show();
                }
                showGenerateOtp();
            }

            @Override
            public void onCodeSent(@NonNull String otp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(otp, forceResendingToken);
                showGenerateOtp();
                Intent intent = new Intent(VerifyNumber.this,VerifyOtp.class);
                intent.putExtra(NUMBER_OTP,otp);
                intent.putExtra(USER_NUMBER,number_editText.getText().toString());
                startActivity(intent);
            }
        };

        generate_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                showProgressbar();
                
                String number = number_editText.getText().toString();
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                        .setActivity(VerifyNumber.this)
                        .setPhoneNumber("+91"+number)
                        .setCallbacks(mCallbacks)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .build();
                
                PhoneAuthProvider.verifyPhoneNumber(options);

            }
        });

    }

    public void SignInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult user) {
                Log.d("myTag", "onSuccess: "+user.getUser().getPhoneNumber());
                Intent intent = new Intent(VerifyNumber.this, MainActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VerifyNumber.this, ""+e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        number_editText = findViewById(R.id.number_editText);
        generate_otp = findViewById(R.id.generate_otp_button);
        progressBar = findViewById(R.id.numberProgressBar);
    }

    private void showProgressbar(){
        progressBar.setVisibility(View.VISIBLE);
        generate_otp.setVisibility(View.INVISIBLE);
    }
    private void showGenerateOtp(){
        progressBar.setVisibility(View.INVISIBLE);
        generate_otp.setVisibility(View.VISIBLE);
    }
}