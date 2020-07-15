package com.example.sih;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class domainAdapter extends RecyclerView.Adapter<domainAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<data_in_cardview> details;
    ArrayList<data_in_cardview> fullDetails;
    Translate translate;
    String check;

    public domainAdapter(Context c, ArrayList<data_in_cardview> d){

        context = c;
        details = d;
        fullDetails = new ArrayList<>(d);

    }

    public domainAdapter(Government c, ArrayList<String> content) {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.gov_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        try {
            holder.Job_Post.setText(details.get(position).getJob_Post());
            holder.Company_Name.setText(details.get(position).getCompany_Name());
            holder.Location.setText(details.get(position).getLocation());
            holder.Job_Type.setText(details.get(position).getJob_Type());

//            Picasso.get().load(details.get(position).getCompany_logo()).into(holder.company_logo);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, Job_Details1.class);
                    String pos = Integer.toString(position);
                    intent.putExtra("jobReference", pos);
                    view.getContext().startActivity(intent);

                }
            });
            if(check.equals("Hin")) {
                getTranslateService();
                translateToHin(holder.Job_Post.getText().toString(), holder.Job_Post);
                translateToHin(holder.Location.getText().toString(), holder.Location);
                translateToHin(holder.Company_Name.getText().toString(), holder.Company_Name);
                translateToHin(holder.Job_Type.getText().toString(), holder.Job_Type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return details ==null ? 0 : details.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Job_Post, Company_Name, Location, Job_Type;
        ImageView company_logo;
        String M;
        int j;
        SharedPreferences preferences = context.getSharedPreferences(M,j);

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            Job_Post = itemView.findViewById(R.id.job_post);
            Company_Name = itemView.findViewById(R.id.company_name);
            Location = itemView.findViewById(R.id.company_location);
            company_logo = itemView.findViewById(R.id.company_logo);
            Job_Type = itemView.findViewById(R.id.job_type);
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
            ArrayList<data_in_cardview> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(fullDetails);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (data_in_cardview item : fullDetails){
                    try {
                        if (item.getJob_Type().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                        else if (item.getJob_Post().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                        else if (item.getLocation().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                        else if (item.getCompany_Name().toLowerCase().contains(filterPattern)){
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

    public void getTranslateService() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = context.getResources().openRawResource(R.raw.translate)) {

            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }
    }


    public void translateToHin (String originalText, TextView target) {
        Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage("hin"), Translate.TranslateOption.model("base"));
        target.setText(translation.getTranslatedText());
    }

}

