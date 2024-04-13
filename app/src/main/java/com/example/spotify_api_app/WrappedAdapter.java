package com.example.spotify_api_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WrappedAdapter extends RecyclerView.Adapter<WrappedAdapter.WrappedViewHolder> {
    private Context context;
    private List<WrappedItem> wrappedList;

    public WrappedAdapter(Context context, List<WrappedItem> wrappedList) {
        this.context = context;
        this.wrappedList = wrappedList;
    }

    @NonNull
    @Override
    public WrappedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.past_wrapped_item, parent, false);
        return new WrappedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WrappedViewHolder holder, int position) {
        WrappedItem item = wrappedList.get(position);
        holder.usernameTextView.setText(item.getUsername());
        holder.dateTextView.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return wrappedList.size();
    }

    public void addItem(String username, String date, List<Artist> artists, List<Track> tracks) {
        wrappedList.add(new WrappedItem(username, date, artists, tracks));
        notifyItemInserted(wrappedList.size() - 1);
    }

    public static class WrappedViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView dateTextView;

        public WrappedViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username);
            dateTextView = itemView.findViewById(R.id.date);
        }
    }
}

