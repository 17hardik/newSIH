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

    public gov_adapter(Government c, ArrayList<String> content) {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.gov_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.Job_Post.setText(details.get(position).getJob_Post().toString());
        holder.Company_Name.setText(details.get(position).getCompany_Name().toString());
        holder.Location.setText(details.get(position).getLocation().toString());
        holder.Salary_PA_in_Rs.setText(details.get(position).getSalary_PA_in_Rs().toString());
        holder.Jobs_Types.setText(details.get(position).getJobs_Types().toString());
        holder.Jobs_Description.setText(details.get(position).getJobs_Description().toString());
        holder.Key_Skills.setText(details.get(position).getKey_Skills().toString());
        holder.Qualification.setText(details.get(position).getQualification().toString());
        holder.Rating.setText(details.get(position).getRating().toString());
        holder.Sector.setText(details.get(position).getSector().toString());
        try {
            Picasso.get().load(details.get(position).getCompany_logo().toString()).into(holder.company_logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return details ==null ? 0 : details.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Job_Post, Company_Name, Location, Salary_PA_in_Rs, Jobs_Types, Jobs_Description, Key_Skills, Qualification, Rating, Sector;
        ImageView company_logo;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            Job_Post = itemView.findViewById(R.id.job_post);
            Company_Name = itemView.findViewById(R.id.company_name);
            Location = itemView.findViewById(R.id.company_location);
            Salary_PA_in_Rs = itemView.findViewById(R.id.salary);
            company_logo = itemView.findViewById(R.id.company_logo);
            Jobs_Types = itemView.findViewById(R.id.Job_Types);
            Jobs_Description = itemView.findViewById(R.id.Jobs_Description);
            Key_Skills = itemView.findViewById(R.id.Key_Skills);
            Qualification = itemView.findViewById(R.id.Qualification);
            Rating = itemView.findViewById(R.id.Rating);
            Sector = itemView.findViewById(R.id.Sector);
        }
    }

}
