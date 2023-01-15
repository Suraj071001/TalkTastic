package com.example.android.talktastic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.talktastic.pojo.Friend;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddFriend extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    FirebaseAuth mAuth;

    EditText name_editText,message_editText;
    String friend_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        name_editText = findViewById(R.id.name_editText);
        message_editText = findViewById(R.id.message_editText);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String friend_number = message_editText.getText().toString();
                databaseReference.child("all_user").child("+91"+friend_number).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        friend_id = snapshot.getValue(String.class);
                        Log.d("myTag", "onDataChange: "+friend_id);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("myTag", "onCancelled: "+error);
                    }
                });
            }
        });


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = name_editText.getText().toString();
                String friend_number = message_editText.getText().toString();

                Friend friend = new Friend(name,"+91"+friend_number);
                String uid = mAuth.getUid();
                friend.setFriend_id(friend_id);
//                String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                assert uid != null;
                databaseReference.child("user").child(uid).child(friend_id).setValue(friend).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });

                String current_user_name = Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName();
                String current_user_number = mAuth.getCurrentUser().getPhoneNumber();
                Friend add_friend = new Friend(current_user_name,current_user_number);
                add_friend.setFriend_id(uid);
                databaseReference.child("user").child(friend_id).child(uid).setValue(add_friend).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(AddFriend.this,MainActivity.class);
                        startActivity(intent);
                    }
                });

                Map<String,String> map = new HashMap<>();

            }
        });
    }
}