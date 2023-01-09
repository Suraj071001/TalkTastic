package com.example.android.talktastic.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.talktastic.MainActivity;
import com.example.android.talktastic.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifyOtp extends AppCompatActivity {
    TextView resend_otp,verify_otp_textView;
    Button verify_otp;
    EditText num1,num2,num3,num4,num5,num6;

    String smsOtp;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        initView();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String number = intent.getStringExtra(VerifyNumber.USER_NUMBER);
        verify_otp_textView.setText("Enter OTP sent to +91"+number);

        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!num1.getText().toString().isEmpty() && !num2.getText().toString().isEmpty() &&
                        !num3.getText().toString().isEmpty() && !num4.getText().toString().isEmpty() &&
                        !num5.getText().toString().isEmpty() && !num6.getText().toString().isEmpty() ){
                    smsOtp = num1.getText().toString() + num2.getText().toString() + num3.getText().toString() +
                            num4.getText().toString() + num5.getText().toString() + num6.getText().toString();

                    String otp = intent.getStringExtra(VerifyNumber.NUMBER_OTP);

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp,smsOtp);
                    signInWithCredential(credential);
                }else{
                    Toast.makeText(VerifyOtp.this, "Enter 6 number otp", Toast.LENGTH_SHORT).show();
                }
            }
        });

        moveToNext();
    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult user) {
                Log.d("myTag", "onSuccess: "+user.getUser().getPhoneNumber());
                Intent intent = new Intent(VerifyOtp.this, MainActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VerifyOtp.this, "Error occurred "+e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        resend_otp = findViewById(R.id.resent_otp_textView);
        verify_otp = findViewById(R.id.verify_otp_button);
        verify_otp_textView = findViewById(R.id.verify_otp_description_textview);
        num1 = findViewById(R.id.otp_num1);
        num2 = findViewById(R.id.otp_num2);
        num3 = findViewById(R.id.otp_num3);
        num4 = findViewById(R.id.otp_num4);
        num5 = findViewById(R.id.otp_num5);
        num6 = findViewById(R.id.otp_num6);
    }

    private void moveToNext(){
        num1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    num2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        num2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    num3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        num3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    num4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        num4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    num5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        num5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    num6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });



    }
}