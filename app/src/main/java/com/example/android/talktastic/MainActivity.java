package com.example.android.talktastic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.talktastic.adapter.FriendsAdapter;
import com.example.android.talktastic.auth.VerifyNumber;
import com.example.android.talktastic.pojo.Friend;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements FriendsAdapter.OnItemClickListener {
    public static final String FRIEND_ID = "friend_push_id";
    RecyclerView recyclerView;
    FloatingActionButton fab;

    //Firebase variables
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    List<Friend> friendList = new ArrayList<>();
    FriendsAdapter mAdapter;

    String userId;

    Boolean addAuthUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String TAG = "myTag";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialise all views
        initView();

        //get database reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    Intent intent = new Intent(MainActivity.this, VerifyNumber.class);
                    startActivity(intent);
                }else{
                    userId = user.getUid();
                    Log.d(TAG, "onAuthStateChanged: "+userId);
                }
            }
        };

        //set Recycler View

        mAdapter = new FriendsAdapter(this,friendList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(mAdapter);

        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        String number = mAuth.getCurrentUser().getPhoneNumber();
        addUser(number,uid);
        databaseReference.child("user").child(uid).addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildAdded: "+snapshot.getKey());
                Friend friend_data = snapshot.getValue(Friend.class);;

                friendList.add(friend_data);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddFriend.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.signOut:
                mAuth.signOut();
                return true;
            case android.R.id.home:
                finish();
                return true;
            case R.id.main_search:
                Toast.makeText(this, "don't implemented yet", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycleView);
        fab = findViewById(R.id.fab);
    }

    @Override
    public void onItemClick(Friend friend) {
        Intent intent = new Intent(MainActivity.this,MessagingActivity.class);
        intent.putExtra(FRIEND_ID,friend.getFriend_id());
        intent.putExtra("friend",friend);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(authStateListener);
    }

    private void addUser(String phone,String userId){
        if(!addAuthUser){
            Task<Void> uploadTask = databaseReference.child("all_user").child(phone).setValue(userId);

            uploadTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("myTag", "onSuccess: user added successfully");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("myTag", "onFailure: "+e);
                }
            });
            addAuthUser = true;
        }
    }
}