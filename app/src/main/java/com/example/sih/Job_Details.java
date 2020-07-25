package com.example.sih;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.firebase.client.annotations.NotNull;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;

public class Job_Details extends AppCompatActivity {

    private TextView job_post, company_name, company_location, job_details, salaryLabel, salary, sectorLabel, sector, jobDescriptionLabel, jobDescription;
    DatabaseReference reff, reff1;
    int k, i, x, y, size;
    String M, J, check, phone, activity, S, jobReference, jobCategory, domainType, post, name, location, Salary, Sector, jobDesc;
    Translate translate;
    Firebase firebase;
    Button FavButton, roadmap, apply;
    Intent intent;
    ProgressDialog pd;
    Boolean isStored = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(S,i);
        phone = preferences.getString("Phone","");

        SharedPreferences preferences1 = getSharedPreferences(M, k);
        check = preferences1.getString("Lang","Eng");

        SharedPreferences preferences2 = getSharedPreferences(J,x);
        activity = preferences2.getString("Activity","");

        setContentView(R.layout.activity_job__details);

        intent = getIntent();
        jobReference = intent.getStringExtra("jobReference");
        jobCategory = intent.getStringExtra("jobCategory");
        domainType = intent.getStringExtra("domainType");
        job_post = findViewById(R.id.job_post);
        FavButton = findViewById(R.id.favButton);
        roadmap = findViewById(R.id.roadmapButton);
        apply = findViewById(R.id.apply);
        company_name = findViewById(R.id.company_name);
        company_location = findViewById(R.id.company_location);
        job_details = findViewById(R.id.job_details);
        salaryLabel = findViewById(R.id.salaryLabel);
        salary = findViewById(R.id.salary);
        sectorLabel = findViewById(R.id.sectorLabel);
        sector = findViewById(R.id.sector);
        jobDescriptionLabel = findViewById(R.id.jobDescriptionLabel);
        jobDescription = findViewById(R.id.jobDescription);

        Firebase.setAndroidContext(getApplicationContext());
        firebase = new Firebase("https://smart-e60d6.firebaseio.com/Users");
        pd = new ProgressDialog(Job_Details.this);
        pd.setMessage("Getting Job Details");
        pd.show();
        Toast.makeText(this, check, Toast.LENGTH_SHORT).show();
        if(!check.equals("Eng")){
            FavButton.setText("ड्रीमजॉब में जोड़ें");
        }

        FavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    reff1 = FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("Dream Jobs");
                    reff1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long childCount = snapshot.getChildrenCount();
                                firebase.child(phone).child("Dream Jobs").child(domainType + " " + jobCategory + jobReference).setValue(domainType + "-" +jobCategory + "-" + jobReference);
                                isStored = true;
                                if(check.equals("Eng")) {
                                    Toast.makeText(Job_Details.this, "Saved to Dream Jobs", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Job_Details.this, "ड्रीमजॉब में जोड़ा गया", Toast.LENGTH_SHORT).show();
                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } catch (Exception e) {

                    firebase.child(phone).child("Dream Jobs").child("Government").child(jobReference).setValue("Government" + jobReference);
                    isStored = true;
                    if(check.equals("Eng")) {
                        Toast.makeText(Job_Details.this, "Saved to Dream Jobs", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Job_Details.this, "ड्रीमजॉब में जोड़ा गया", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (check.equals("Hin")){
            getTranslateService();
            try {
                translateToHin(job_details.getText().toString(), job_details);
                translateToHin(salaryLabel.getText().toString(), salaryLabel);
                translateToHin(sectorLabel.getText().toString(), sectorLabel);
                translateToHin(jobDescriptionLabel.getText().toString(), jobDescriptionLabel);
            } catch (Exception e) {
                Toast.makeText(Job_Details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        roadmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent roadmapIntent = new Intent(Job_Details.this, progressTracker.class);
                startActivity(roadmapIntent);
                SharedPreferences.Editor editor = getSharedPreferences(J, x).edit();
                editor.putString("jobCategory", jobCategory);
                editor.putString("jobReference", jobReference);
                editor.apply();

            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent applyIntent = new Intent(Job_Details.this, jobApply.class);
                startActivity(applyIntent);
                SharedPreferences.Editor editor = getSharedPreferences(J, x).edit();
                editor.putString("jobCategory", jobCategory);
                editor.putString("jobReference", jobReference);
                editor.putString("domainType", domainType);
                editor.apply();

            }
        });

        if (activity.equals("Main")){

            roadmap.setVisibility(View.VISIBLE);

        }

        else{

            FavButton.setVisibility(View.VISIBLE);

        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Job Details");

        getJobPost(jobReference);
        getCompanyName(jobReference);
        getLocation(jobReference);
        getSalary(jobReference);
        getSector(jobReference);
        getJobDescription(jobReference);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
            }
        }, 3000);

    }


    public void getJobPost(final String jobReference) {

        reff = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child(domainType).child(jobCategory).child(jobReference);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    post = dataSnapshot.child("Job_Post").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                job_post.setText(post);
                if (check.equals("Hin")) {
                    getTranslateService();
                    try {
                        translateToHin(job_post.getText().toString(), job_post);
                    } catch (Exception e) {
                        Toast.makeText(Job_Details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals("Eng")) {
                    Toast.makeText(Job_Details.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Job_Details.this, "कृपया अपने इंटरनेट कनेक्शन की जाँच करें", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void getCompanyName(final String jobReference) {

        reff = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child(domainType).child(jobCategory).child(jobReference);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    name = dataSnapshot.child("Company_Name").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                company_name.setText(name);
                if (check.equals("Hin")) {
                    getTranslateService();
                    try {
                        translateToHin(company_name.getText().toString(), company_name);
                    } catch (Exception e) {
                        Toast.makeText(Job_Details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals("Eng")) {
                    Toast.makeText(Job_Details.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Job_Details.this, "कृपया अपने इंटरनेट कनेक्शन की जाँच करें", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getLocation(final String jobReference) {

        reff = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child(domainType).child(jobCategory).child(jobReference);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    location = snapshot.child("Location").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                company_location.setText(location);

                if (check.equals("Hin")) {
                    getTranslateService();
                    try {
                        translateToHin(company_location.getText().toString(), company_location);
                    } catch (Exception e) {
                        Toast.makeText(Job_Details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(check.equals("Eng")) {
                    Toast.makeText(Job_Details.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Job_Details.this, "कृपया अपने इंटरनेट कनेक्शन की जाँच करें", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getSalary(final String jobReference) {

        reff = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child(domainType).child(jobCategory).child(jobReference);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Salary = dataSnapshot.child("Salary_PA_in_Rs").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                salary.setText(Salary);
                if (check.equals("Hin")) {
                    try {
                        getTranslateService();
                        translateToHin(salary.getText().toString(), salary);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals("Eng")) {
                    Toast.makeText(Job_Details.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Job_Details.this, "कृपया अपने इंटरनेट कनेक्शन की जाँच करें", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getSector(final String jobReference){

        reff = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child(domainType).child(jobCategory).child(jobReference);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Sector = dataSnapshot.child("Sector").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sector.setText(Sector);
                if (check.equals("Hin")) {
                    getTranslateService();
                    try {
                        translateToHin(sector.getText().toString(), sector);
                    } catch (Exception e) {
                        Toast.makeText(Job_Details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals("Eng")) {
                    Toast.makeText(Job_Details.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Job_Details.this, "कृपया अपने इंटरनेट कनेक्शन की जाँच करें", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getJobDescription(final String jobReference){

        reff = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child(domainType).child(jobCategory).child(jobReference);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    jobDesc = dataSnapshot.child("Job_Description").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jobDescription.setText(jobDesc);
                if (check.equals("Hin")) {
                    getTranslateService();
                    try {
                        translateToHin(jobDescription.getText().toString(), jobDescription);
                    } catch (Exception e) {
                        Toast.makeText(Job_Details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals("Eng")) {
                    Toast.makeText(Job_Details.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Job_Details.this, "कृपया अपने इंटरनेट कनेक्शन की जाँच करें", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item){

        if (item.getItemId() == android.R.id.home){

            onBackPressed();

        }

        return super.onOptionsItemSelected(item);

    }
    public void getTranslateService() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = getResources().openRawResource(R.raw.translate)) {

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