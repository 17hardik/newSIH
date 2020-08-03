package com.example.sih.Atmanirbhar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.cloud.translate.Translate;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class serviceAdapter extends RecyclerView.Adapter<serviceAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<serviceCardView> details;
    ArrayList<serviceCardView> fullDetails;
    Translate translate;
    String check;


    public serviceAdapter(ArrayList<serviceCardView> d, Context c) {
        context = c;
        details = d;
        fullDetails = new ArrayList<>(d);
    }

    public serviceAdapter(ClientService c, ArrayList<String> content) {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.service_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        try {
            holder.JobName.setText(details.get(position).getJobName());
            holder.Description.setText(details.get(position).getDescription());
            holder.Days.setText(details.get(position).getDays());
            holder.Number.setText(details.get(position).getPhone());


//            Picasso.get().load(details.get(position).getCompany_logo()).into(holder.company_logo);
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View view) {
//
//                    Intent intent = new Intent(context, com.example.sih.chatApp.Chat.class);
//                    String phone = details.get(position).getPhone();
//                    String jobName = details.get(position).getJobName();
//                    intent.putExtra("Phone", phone);
//                    intent.putExtra("jobName", jobName);
//                    view.getContext().startActivity(intent);
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return details ==null ? 0 : details.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView JobName, Description, Number, Days;
        ImageView Profile_Picture;
        String M, J;
        int j;
        SharedPreferences preferences = context.getSharedPreferences(M,j);

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            JobName = itemView.findViewById(R.id.jobname);
            Description = itemView.findViewById(R.id.description);
            Number = itemView.findViewById(R.id.phone);
            Days = itemView.findViewById(R.id.days);
            check = preferences.getString("Lang","Eng");
        }
    }

    @Override
    public Filter getFilter() {
        return detailsFilter;
    }

    private Filter detailsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<serviceCardView> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(fullDetails);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (serviceCardView item : fullDetails){
                    try {
                        if (item.getJobName().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            details.clear();
            details.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };


}
