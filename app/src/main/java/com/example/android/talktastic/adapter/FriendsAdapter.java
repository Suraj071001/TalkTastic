package com.example.android.talktastic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.talktastic.pojo.Friend;
import com.example.android.talktastic.R;
import com.example.android.talktastic.Utilities.FriendUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    List<Friend> friendList = new ArrayList<>();
    Context context;
    OnItemClickListener mListener;

    public FriendsAdapter(Context context,List<Friend> friendList,OnItemClickListener mListener){
        this.context = context;
        this.friendList = friendList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_users,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = friendList.get(position).getName();
        String id = friendList.get(position).getFriend_id();
        String number = friendList.get(position).getPhone_number();

        holder.friendName_TextView.setText(number);

        int finalPosition = position;
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(friendList.get(finalPosition));
            }
        });

        holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Task<Void> taskVoid = FriendUtils.removeFriend(id);

                taskVoid.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "friend successfully deleted", Toast.LENGTH_SHORT).show();
                        friendList.remove(finalPosition);
                        notifyDataSetChanged();
                    }
                });

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView friendName_TextView ,last_message_TextView;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName_TextView = itemView.findViewById(R.id.f_name_textView);
            last_message_TextView = itemView.findViewById(R.id.f_message_textView);
            constraintLayout = itemView.findViewById(R.id.friend_constraint);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Friend friend);
    }
}
