package com.example.sih;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class gov_adapter extends RecyclerView.Adapter<gov_adapter.MyViewHolder> {
    Context context;
    ArrayList<data_in_cardview> details;
    Translate translate;
    String check;

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

        try {
            holder.Job_Post.setText(details.get(position).getJob_Post());
            holder.Company_Name.setText(details.get(position).getCompany_Name());
            holder.Location.setText(details.get(position).getLocation());
            holder.Salary_PA_in_Rs.setText(details.get(position).getSalary_PA_in_Rs());
            Picasso.get().load(details.get(position).getCompany_logo()).into(holder.company_logo);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(new Intent(context, Job_Details.class));
                }
            });
            if(check.equals("Hin")) {
                getTranslateService();
                translateToHin(holder.Job_Post.getText().toString(), holder.Job_Post);
                translateToHin(holder.Location.getText().toString(), holder.Location);
                translateToHin(holder.Salary_PA_in_Rs.getText().toString(), holder.Salary_PA_in_Rs);
                translateToHin(holder.Company_Name.getText().toString(), holder.Company_Name);
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

        TextView Job_Post, Company_Name, Location, Salary_PA_in_Rs;
        ImageView company_logo;
        String M;
        int j;
        SharedPreferences preferences = context.getSharedPreferences(M,j);

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            Job_Post = itemView.findViewById(R.id.job_post);
            Company_Name = itemView.findViewById(R.id.company_name);
            Location = itemView.findViewById(R.id.company_location);
            Salary_PA_in_Rs = itemView.findViewById(R.id.salary);
            company_logo = itemView.findViewById(R.id.company_logo);
            check = preferences.getString("Lang","Eng");
        }
    }

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
