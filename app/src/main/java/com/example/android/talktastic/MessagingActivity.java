package com.example.android.talktastic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.android.talktastic.adapter.MessagingAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class MessagingActivity extends AppCompatActivity{
    EditText message_editText;
    ImageView send_button;
    RecyclerView recyclerView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    ChildEventListener childEventListener;

    ArrayList<String> message_list = new ArrayList<>();
    String friend_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        initView();

        Intent intent = getIntent();

        friend_id = intent.getStringExtra(MainActivity.FRIEND_ID);

        MessagingAdapter messagingAdapter = new MessagingAdapter(this,message_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(messagingAdapter);

        String uid = Objects.requireNonNull(mAuth.getUid());

        childEventListener = new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String message = snapshot.getValue(String.class);

                message_list.add(message);
                messagingAdapter.notifyDataSetChanged();

                //scroll to last position
                recyclerView.smoothScrollToPosition(message_list.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference.child(uid).child(friend_id).child("message").addChildEventListener(childEventListener);


        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = message_editText.getText().toString();

                databaseReference.child(uid).child(friend_id).child("message").push().setValue(message)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("myTag", "onSuccess: message sent");
                        message_editText.setText(null);
                    }
                });

                databaseReference.child(friend_id).child(uid).child("message").push().setValue(message)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("myTag","onSuccess : message sent");
                                message_editText.setText(null);
                            }
                        });
            }
        });
    }

    private void initView() {
        message_editText = findViewById(R.id.f_message);
        send_button = findViewById(R.id.imageView);
        recyclerView = findViewById(R.id.message_recyclerView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("user");
        mAuth = FirebaseAuth.getInstance();

    }
}