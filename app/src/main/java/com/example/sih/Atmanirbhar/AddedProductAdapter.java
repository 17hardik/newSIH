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

public class AddedProductAdapter extends RecyclerView.Adapter<AddedProductAdapter.MyViewHolder> {

    Context context;
    ArrayList<AddedProductCardView> posts;

    public AddedProductAdapter(Context c, ArrayList<AddedProductCardView> j){
        context = c;
        posts = j;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.added_product_cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(posts.get(position).getProductName());
        holder.description.setText(posts.get(position).getDescription());
        holder.price.setText(posts.get(position).getLocation());
        holder.contact.setText(posts.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, description, price, contact;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.desc);
            price = itemView.findViewById(R.id.price);
            contact = itemView.findViewById(R.id.contact);

        }
    }

}