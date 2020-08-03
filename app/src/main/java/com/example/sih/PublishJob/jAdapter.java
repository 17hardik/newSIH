package com.example.sih.PublishJob;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.R;

import java.util.ArrayList;

public class jAdapter extends RecyclerView.Adapter<jAdapter.MyViewHolder> {

    Context context;
    ArrayList<jobPost> posts;

    public jAdapter(Context c, ArrayList<jobPost> j){
        context = c;
        posts = j;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.title.setText(posts.get(position).getTitle());
        holder.description.setText(posts.get(position).getDescription());
        holder.experience.setText(posts.get(position).getExperience());
        holder.city.setText(posts.get(position).getCity());
        holder.email.setText(posts.get(position).getEmail());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, editJobPost.class);
                String jobPost = posts.get(position).getTitle();
                intent.putExtra("jobPost", jobPost);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, description, experience, city, email;
        Button edit, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.desc);
            experience = itemView.findViewById(R.id.exp);
            city = itemView.findViewById(R.id.city);
            email = itemView.findViewById(R.id.email);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }

}