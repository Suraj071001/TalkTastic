package com.example.android.talktastic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.talktastic.R;

import java.util.ArrayList;

public class MessagingAdapter extends RecyclerView.Adapter<MessagingAdapter.ViewHolder> {
    Context context;
    ArrayList<String> message_list = new ArrayList<>();

    public MessagingAdapter(Context context, ArrayList<String> message_list) {
        this.context = context;
        this.message_list = message_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String message = message_list.get(position);
        holder.message_textView.setText(message);
    }

    @Override
    public int getItemCount() {
        return message_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message_textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            message_textView = itemView.findViewById(R.id.m_textView);
        }
    }
}
