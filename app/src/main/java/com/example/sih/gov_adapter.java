package com.example.sih;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class gov_adapter extends RecyclerView.Adapter<gov_adapter.MyViewHolder> {

    Context context;
    ArrayList<data_in_cardview> details;

    public gov_adapter(Context c, ArrayList<data_in_cardview> d){

        context = c;
        details = d;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.gov_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.job_post.setText(details.get(position).getJob_Post());
        holder.company_name.setText(details.get(position).getCompany_name());
        holder.company_location.setText(details.get(position).getCompany_location());
        holder.salary.setText(details.get(position).getSalary());
        Picasso.get().load(details.get(position).getCompany_logo()).into(holder.company_logo);

    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView job_post, company_name, company_location, salary;
        ImageView company_logo;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            job_post = itemView.findViewById(R.id.job_post);
            company_name = itemView.findViewById(R.id.company_name);
            company_location = itemView.findViewById(R.id.company_location);
            salary = itemView.findViewById(R.id.salary);
            company_logo = itemView.findViewById(R.id.company_logo);

        }
    }

}
