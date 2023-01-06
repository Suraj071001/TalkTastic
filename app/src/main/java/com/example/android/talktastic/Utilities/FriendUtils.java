package com.example.android.talktastic.Utilities;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FriendUtils {
    static FirebaseDatabase firebaseDatabase;
    static DatabaseReference databaseReference;

    public static Task<Void> removeFriend(String friend_id) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("user");
        Log.d("myTag", "removeFriend: "+friend_id);

        return databaseReference.child(friend_id).removeValue();
    }

}
