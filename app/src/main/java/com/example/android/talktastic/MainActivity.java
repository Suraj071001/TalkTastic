package com.example.android.talktastic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.talktastic.adapter.FriendsAdapter;
import com.example.android.talktastic.pojo.Friend;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FriendsAdapter.OnItemClickListener {
    public static final String FRIEND_PUSH_ID = "friend_push_id";
    RecyclerView recyclerView;
    FloatingActionButton fab;

    //Firebase variables
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;

    List<Friend> friendList = new ArrayList<>();
    FriendsAdapter mAdapter;

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

        //set Recycler View

        mAdapter = new FriendsAdapter(this,friendList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(mAdapter);


        databaseReference.child("user").addChildEventListener(new ChildEventListener() {
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
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycleView);
        fab = findViewById(R.id.fab);
    }

    @Override
    public void onItemClick(Friend friend) {
        Intent intent = new Intent(MainActivity.this,MessagingActivity.class);
        intent.putExtra(FRIEND_PUSH_ID,friend.getFriend_id());
        startActivity(intent);
    }
}