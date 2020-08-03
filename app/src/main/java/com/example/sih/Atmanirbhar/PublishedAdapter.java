package com.example.sih.Atmanirbhar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.Atmanirbhar.PublishedCardView;
import com.example.sih.R;

import java.util.ArrayList;

public class PublishedAdapter extends RecyclerView.Adapter<PublishedAdapter.MyViewHolder> {

    Context context;
    ArrayList<PublishedCardView> posts;

    public PublishedAdapter(Context c, ArrayList<PublishedCardView> j){
        context = c;
        posts = j;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.publishedcardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(posts.get(position).getJobName());
        holder.description.setText(posts.get(position).getDescription());
        holder.duration.setText(posts.get(position).getLocation());
        holder.contact.setText(posts.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, description, duration, contact;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.desc);
            duration = itemView.findViewById(R.id.time);
            contact = itemView.findViewById(R.id.contact);

        }
    }

}