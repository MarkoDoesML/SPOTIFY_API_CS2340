package com.example.spotify_api_app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WrappedAdapter extends RecyclerView.Adapter<WrappedAdapter.WrappedViewHolder> {
    private Context context;
    private List<WrappedItem> wrappedList;
    private OnClickListener onClickListener;

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
        WrappedItem item = wrappedList.get(holder.getAdapterPosition());
        holder.usernameTextView.setText(item.getUsername());
        holder.dateTextView.setText(item.getDate());
        Map<String, Object> tracks = (Map<String, Object>) (item.getWrapInfo().get("tracks"));
        Map<String, Object> topTrack = (Map<String, Object>) tracks.get("track1");
        String topTrackImage = topTrack.get("image").toString();
        Picasso.get().load(topTrackImage).into(holder.topTrackImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                if (onClickListener != null && clickedPosition != RecyclerView.NO_POSITION) {
                    onClickListener.onClick(clickedPosition, wrappedList.get(clickedPosition));
                };
                Intent i = new Intent(context, WrappedActivity.class);
                i.putExtra("wrapped", item.getWrapInfo());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wrappedList.size();
    }

    public void addItem(String username, String date, HashMap<String, Object> wrapInfo) {
        wrappedList.add(0, new WrappedItem(username, date, wrapInfo));
        notifyItemInserted(0);
    }

    public static class WrappedViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView dateTextView;
        ImageView topTrackImage;

        public WrappedViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username);
            dateTextView = itemView.findViewById(R.id.date);
            topTrackImage = itemView.findViewById(R.id.profileImage);
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, WrappedItem wrappedItem);
    }

}

