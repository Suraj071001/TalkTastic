package com.example.android.talktastic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.talktastic.pojo.Friend;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddFriend extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText name_editText,message_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        name_editText = findViewById(R.id.name_editText);
        message_editText = findViewById(R.id.message_editText);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = name_editText.getText().toString();
                String message = message_editText.getText().toString();
                Friend friend = new Friend(name,message);
                String key = databaseReference.push().getKey();
                friend.setFriend_id(key);
                assert key != null;
                databaseReference.child("user").child(key).setValue(friend).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                });

                Map<String,String> map = new HashMap<>();

                databaseReference.child("user_message").child(key).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddFriend.this, "friend added successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddFriend.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}