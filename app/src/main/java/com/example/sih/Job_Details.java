package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.annotations.NotNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Job_Details extends AppCompatActivity {

    private TextView job_post, company_name, company_location, job_details, salaryLabel, salary, sectorLabel, sector, jobDescriptionLabel, jobDescription;
    DatabaseReference reff, reff1;
    int size, j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job__details);

        job_post = findViewById(R.id.job_post);
        company_name = findViewById(R.id.company_name);
        company_location = findViewById(R.id.company_location);
        job_details = findViewById(R.id.job_details);
        salaryLabel = findViewById(R.id.salaryLabel);
        salary = findViewById(R.id.salary);
        sectorLabel = findViewById(R.id.sectorLabel);
        sector = findViewById(R.id.sector);
        jobDescriptionLabel = findViewById(R.id.jobDescriptionLabel);
        jobDescription = findViewById(R.id.jobDescription);

        reff = FirebaseDatabase.getInstance().getReference().child("Jobs").child("Government").child("0");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String post = null;
                String name = null;
                String location = null;
                String Salary = null;
                String Sector = null;
                String job_description = null;
                try {
                    post = snapshot.child("Job_Post").getValue().toString();
                    name = snapshot.child("Company_Name").getValue().toString();
                    location = snapshot.child("Location").getValue().toString();
                    Salary = snapshot.child("Salary_PA_in_Rs").getValue().toString();
                    Sector = snapshot.child("Sector").getValue().toString();
                    job_description = snapshot.child("Jobs_Description").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                job_post.setText(post);
                company_name.setText(name);
                company_location.setText(location);
                salary.setText(Salary);
                sector.setText(Sector);
                jobDescription.setText(job_description);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Job_Details.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

//        reff = FirebaseDatabase.getInstance().getReference().child("Jobs").child("Government");
//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                size = (int) dataSnapshot.getChildrenCount();
//
//                for (j = 0; j < size; j++) {
//
//                    String i = Integer.toString(j);
//                    reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs").child("Government").child(i);
//                    reff1.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                            String post = null;
//                            String name = null;
//                            String location = null;
//                            String Salary = null;
//                            String Sector = null;
//                            String job_description = null;
//                            try {
//                                post = snapshot.child("Job_Post").getValue().toString();
//                                name = snapshot.child("Company_Name").getValue().toString();
//                                location = snapshot.child("Location").getValue().toString();
//                                Salary = snapshot.child("Salary_PA_in_Rs").getValue().toString();
//                                Sector = snapshot.child("Sector").getValue().toString();
//                                job_description = snapshot.child("Jobs_Description").getValue().toString();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                            job_post.setText(post);
//                            company_name.setText(name);
//                            company_location.setText(location);
//                            salary.setText(Salary);
//                            sector.setText(Sector);
//                            jobDescription.setText(job_description);
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                            Toast.makeText(Job_Details.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(Job_Details.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
//            }
//        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item){

        if (item.getItemId() == android.R.id.home){

            onBackPressed();

        }

        return super.onOptionsItemSelected(item);

    }

}