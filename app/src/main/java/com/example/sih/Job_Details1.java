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
import java.util.ArrayList;

public class Job_Details1 extends AppCompatActivity {

    private TextView job_post, company_name, company_location, job_details, salaryLabel, salary, sectorLabel, sector, jobDescriptionLabel, jobDescription;
    DatabaseReference reff, reff1, reff2, reff3, reff4, reff5, reff6, reff7, reff8, reff9, reff10;
    int j, k, i, x, y, size;
    String M, J, check, phone, activity, S, jobReference, post, name, location, Salary, Sector, jobDesc, Science, Business, Farming, Community, Labors, Health, Communications, Arts, Education, Installation;
    Translate translate;
    Firebase firebase;
    Button FavButton;
    Intent intent;
    ProgressDialog pd;
    Boolean isStored = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(S, i);
        Science = preferences.getString("Science", "");
        Business = preferences.getString("Business", "");
        Farming = preferences.getString("Farming", "");
        Community = preferences.getString("Community", "");
        Labors = preferences.getString("Labors", "");
        Health = preferences.getString("Health", "");
        Communications = preferences.getString("Communications", "");
        Arts = preferences.getString("Arts", "");
        Education = preferences.getString("Education", "");
        Installation = preferences.getString("Installation", "");
        phone = preferences.getString("Phone", "");

        SharedPreferences preferences1 = getSharedPreferences(M, j);
        check = preferences1.getString("Lang", "Eng");

        SharedPreferences preferences2 = getSharedPreferences(J, x);
        activity = preferences2.getString("Activity", "");

        setContentView(R.layout.activity_job__details);

        intent = getIntent();
        jobReference = intent.getStringExtra("jobReference");
        job_post = findViewById(R.id.job_post);
        FavButton = findViewById(R.id.favButton);
        company_name = findViewById(R.id.company_name);
        company_location = findViewById(R.id.company_location);
        job_details = findViewById(R.id.job_details);
        salaryLabel = findViewById(R.id.salaryLabel);
        salary = findViewById(R.id.salary);
        sectorLabel = findViewById(R.id.sectorLabel);
        sector = findViewById(R.id.sector);
        jobDescriptionLabel = findViewById(R.id.jobDescriptionLabel);
        jobDescription = findViewById(R.id.jobDescription);

        firebase = new Firebase("https://smart-e60d6.firebaseio.com/Users");
        pd = new ProgressDialog(Job_Details1.this);
        pd.setMessage("Getting Job Details");
        pd.show();

        FavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    reff1 = FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("Dream Jobs");
                    reff1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long childCount = snapshot.getChildrenCount();
                            firebase.child(phone).child("Dream Jobs").child(activity + jobReference).setValue(activity + "-" + jobReference);
                            isStored = true;
                            Toast.makeText(Job_Details1.this, "Saved to Dream Jobs", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } catch (Exception e) {

                    firebase.child(phone).child("Dream Jobs").child("Government").child(jobReference).setValue("Government" + jobReference);
                    isStored = true;
                    Toast.makeText(Job_Details1.this, "Saved to Dream Jobs", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (check.equals("Hin")) {
            getTranslateService();
            try {
                translateToHin(job_details.getText().toString(), job_details);
                translateToHin(salaryLabel.getText().toString(), salaryLabel);
                translateToHin(sectorLabel.getText().toString(), sectorLabel);
                translateToHin(jobDescriptionLabel.getText().toString(), jobDescriptionLabel);
            } catch (Exception e) {
                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
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

        if (activity.equals("Main")) {
            reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("Dream Jobs");
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    ArrayList<String> list = new ArrayList<>();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        list.add(String.valueOf(dataSnapshot.getValue()));
                        for (y = 0; y < list.size(); y++) {
                            String dreamJob = list.get(y);
                            String[] arrOfStr = dreamJob.split("-", 2);
                            String category = arrOfStr[0];
                            String position = arrOfStr[1];

                            reff = FirebaseDatabase.getInstance().getReference().child("Jobs").child(category).child(position);
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
                                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (activity.equals("Government")) {

            if (Science.equals("Yes")) {

                reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Science and Technology").child("Government");
                reff1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Business.equals("Yes")) {

                reff2 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Business, Management and Administration").child("Government");
                reff2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Farming.equals("Yes")) {

                reff3 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Farming, Fishing and Forestry").child("Government");
                reff3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Community.equals("Yes")) {

                reff4 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Community and Social Services").child("Government");
                reff4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Labors.equals("Yes")) {

                reff5 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Labour").child("Government");
                reff5.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Health.equals("Yes")) {

                reff6 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Healthcare and Medical").child("Government");
                reff6.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Communications.equals("Yes")) {

                reff7 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Communication").child("Government");
                reff7.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Arts.equals("Yes")) {

                reff8 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Arts, Culture and Entertainment").child("Government");
                reff8.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Education.equals("Yes")) {

                reff9 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Education").child("Government");
                reff9.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Installation.equals("Yes")) {

                reff10 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Installation, Repair and Maintenance").child("Government");
                reff10.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            }

        } else if (activity.equals("Private")) {

            if (Science.equals("Yes")) {

                reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Science and Technology").child("Private");
                reff1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Business.equals("Yes")) {

                reff2 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Business, Management and Administration").child("Private");
                reff2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Farming.equals("Yes")) {

                reff3 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Farming, Fishing and Forestry").child("Private");
                reff3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Community.equals("Yes")) {

                reff4 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Community and Social Services").child("Private");
                reff4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Labors.equals("Yes")) {

                reff5 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Labour").child("Private");
                reff5.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Health.equals("Yes")) {

                reff6 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Healthcare and Medical").child("Private");
                reff6.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Communications.equals("Yes")) {

                reff7 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Communication").child("Private");
                reff7.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Arts.equals("Yes")) {

                reff8 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Arts, Culture and Entertainment").child("Private");
                reff8.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Education.equals("Yes")) {

                reff9 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Education").child("Private");
                reff9.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Installation.equals("Yes")) {

                reff10 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Installation, Repair and Maintenance").child("Private");
                reff10.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            }

        } else if (activity.equals("Freelancing")) {

            if (Science.equals("Yes")) {

                reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Science and Technology").child("Freelancing");
                reff1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Business.equals("Yes")) {

                reff2 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Business, Management and Administration").child("Freelancing");
                reff2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Farming.equals("Yes")) {

                reff3 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Farming, Fishing and Forestry").child("Freelancing");
                reff3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Community.equals("Yes")) {

                reff4 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Community and Social Services").child("Freelancing");
                reff4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Labors.equals("Yes")) {

                reff5 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Labour").child("Freelancing");
                reff5.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Health.equals("Yes")) {

                reff6 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Healthcare and Medical").child("Freelancing");
                reff6.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Communications.equals("Yes")) {

                reff7 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Communication").child("Freelancing");
                reff7.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Arts.equals("Yes")) {

                reff8 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Arts, Culture and Entertainment").child("Freelancing");
                reff8.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Education.equals("Yes")) {

                reff9 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Education").child("Freelancing");
                reff9.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Installation.equals("Yes")) {

                reff10 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Installation, Repair and Maintenance").child("Freelancing");
                reff10.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            }

        } else {

            if (Science.equals("Yes")) {

                reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Science and Technology").child("Private");
                reff1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Business.equals("Yes")) {

                reff2 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Business, Management and Administration").child("Private");
                reff2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Farming.equals("Yes")) {

                reff3 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Farming, Fishing and Forestry").child("Private");
                reff3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Community.equals("Yes")) {

                reff4 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Community and Social Services").child("Private");
                reff4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Labors.equals("Yes")) {

                reff5 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Labour").child("Private");
                reff5.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Health.equals("Yes")) {

                reff6 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Healthcare and Medical").child("Private");
                reff6.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Communications.equals("Yes")) {

                reff7 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Communication").child("Private");
                reff7.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Arts.equals("Yes")) {

                reff8 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Arts, Culture and Entertainment").child("Private");
                reff8.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Education.equals("Yes")) {

                reff9 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Education").child("Private");
                reff9.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (Installation.equals("Yes")) {

                reff10 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Installation, Repair and Maintenance").child("Private");
                reff10.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            post = dataSnapshot.child(jobReference).child("Job_Post").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        job_post.setText(post);
                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(job_post.getText().toString(), job_post);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                    }
                });

            }

        }

    }

    public void getCompanyName(final String jobReference) {

        if (Science.equals("Yes")) {

            reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Science and Technology").child(activity);
            reff1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        name = dataSnapshot.child(jobReference).child("Company_Name").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    company_name.setText(name);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(company_name.getText().toString(), company_name);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Business.equals("Yes")) {

            reff2 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Business, Management and Administration").child(activity);
            reff2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        name = dataSnapshot.child(jobReference).child("Company_Name").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    company_name.setText(name);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(company_name.getText().toString(), company_name);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Farming.equals("Yes")) {

            reff3 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Farming, Fishing and Forestry").child(activity);
            reff3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        name = dataSnapshot.child(jobReference).child("Company_Name").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    company_name.setText(name);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(company_name.getText().toString(), company_name);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Community.equals("Yes")) {

            reff4 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Community and Social Services").child(activity);
            reff4.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        name = dataSnapshot.child(jobReference).child("Company_Name").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    company_name.setText(name);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(company_name.getText().toString(), company_name);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Labors.equals("Yes")) {

            reff5 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Labour").child(activity);
            reff5.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        name = dataSnapshot.child(jobReference).child("Company_Name").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    company_name.setText(name);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(company_name.getText().toString(), company_name);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Health.equals("Yes")) {

            reff6 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Healthcare and Medical").child(activity);
            reff6.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        name = dataSnapshot.child(jobReference).child("Company_Name").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    company_name.setText(name);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(company_name.getText().toString(), company_name);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Communications.equals("Yes")) {

            reff7 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Communication").child(activity);
            reff7.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        name = dataSnapshot.child(jobReference).child("Company_Name").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    company_name.setText(name);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(company_name.getText().toString(), company_name);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Arts.equals("Yes")) {

            reff8 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Arts, Culture and Entertainment").child(activity);
            reff8.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        name = dataSnapshot.child(jobReference).child("Company_Name").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    company_name.setText(name);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(company_name.getText().toString(), company_name);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Education.equals("Yes")) {

            reff9 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Education").child(activity);
            reff9.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        name = dataSnapshot.child(jobReference).child("Company_Name").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    company_name.setText(name);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(company_name.getText().toString(), company_name);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Installation.equals("Yes")) {

            reff10 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Installation, Repair and Maintenance").child(activity);
            reff10.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        name = dataSnapshot.child(jobReference).child("Company_Name").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    company_name.setText(name);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(company_name.getText().toString(), company_name);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

    public void getLocation(final String jobReference) {

            if (Science.equals("Yes")) {

                reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Science and Technology").child(activity);
                reff1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            location = snapshot.child(jobReference).child("Location").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        company_location.setText(location);

                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(company_location.getText().toString(), company_location);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Job_Details1.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (Business.equals("Yes")) {

                reff2 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Business, Management and Administration").child(activity);
                reff2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            location = snapshot.child(jobReference).child("Location").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        company_location.setText(location);

                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(company_location.getText().toString(), company_location);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Job_Details1.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (Farming.equals("Yes")) {

                reff3 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Farming, Fishing and Forestry").child(activity);
                reff3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            location = snapshot.child(jobReference).child("Location").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        company_location.setText(location);

                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(company_location.getText().toString(), company_location);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Job_Details1.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (Community.equals("Yes")) {

                reff4 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Community and Social Services").child(activity);
                reff4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            location = snapshot.child(jobReference).child("Location").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        company_location.setText(location);

                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(company_location.getText().toString(), company_location);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Job_Details1.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (Labors.equals("Yes")) {

                reff5 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Labour").child(activity);
                reff5.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            location = snapshot.child(jobReference).child("Location").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        company_location.setText(location);

                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(company_location.getText().toString(), company_location);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Job_Details1.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (Health.equals("Yes")) {

                reff6 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Healthcare and Medical").child(activity);
                reff6.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            location = snapshot.child(jobReference).child("Location").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        company_location.setText(location);

                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(company_location.getText().toString(), company_location);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Job_Details1.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (Communications.equals("Yes")) {

                reff7 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Communication").child(activity);
                reff7.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            location = snapshot.child(jobReference).child("Location").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        company_location.setText(location);

                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(company_location.getText().toString(), company_location);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Job_Details1.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (Arts.equals("Yes")) {

                reff8 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Arts, Culture and Entertainment").child(activity);
                reff8.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            location = snapshot.child(jobReference).child("Location").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        company_location.setText(location);

                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(company_location.getText().toString(), company_location);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Job_Details1.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (Education.equals("Yes")) {

                reff9 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Education").child(activity);
                reff9.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            location = snapshot.child(jobReference).child("Location").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        company_location.setText(location);

                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(company_location.getText().toString(), company_location);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Job_Details1.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (Installation.equals("Yes")) {

                reff10 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Installation, Repair and Maintenance").child(activity);
                reff10.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            location = snapshot.child(jobReference).child("Location").getValue().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        company_location.setText(location);

                        if (check.equals("Hin")) {
                            getTranslateService();
                            try {
                                translateToHin(company_location.getText().toString(), company_location);
                            } catch (Exception e) {
                                Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Job_Details1.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }

    public void getSalary(final String jobReference){

        if (Science.equals("Yes")) {

            reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Science and Technology").child(activity);
            reff1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Salary = dataSnapshot.child(jobReference).child("Salary_PA_in_Rs").getValue().toString();
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
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Business.equals("Yes")) {

            reff2 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Business, Management and Administration").child(activity);
            reff2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Salary = dataSnapshot.child(jobReference).child("Salary_PA_in_Rs").getValue().toString();
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
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Farming.equals("Yes")) {

            reff3 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Farming, Fishing and Forestry").child(activity);
            reff3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Salary = dataSnapshot.child(jobReference).child("Salary_PA_in_Rs").getValue().toString();
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
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Community.equals("Yes")) {

            reff4 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Community and Social Services").child(activity);
            reff4.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Salary = dataSnapshot.child(jobReference).child("Salary_PA_in_Rs").getValue().toString();
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
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Labors.equals("Yes")) {

            reff5 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Labour").child(activity);
            reff5.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Salary = dataSnapshot.child(jobReference).child("Salary_PA_in_Rs").getValue().toString();
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
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Health.equals("Yes")) {

            reff6 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Healthcare and Medical").child(activity);
            reff6.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Salary = dataSnapshot.child(jobReference).child("Salary_PA_in_Rs").getValue().toString();
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
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Communications.equals("Yes")) {

            reff7 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Communication").child(activity);
            reff7.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Salary = dataSnapshot.child(jobReference).child("Salary_PA_in_Rs").getValue().toString();
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
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Arts.equals("Yes")) {

            reff8 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Arts, Culture and Entertainment").child(activity);
            reff8.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Salary = dataSnapshot.child(jobReference).child("Salary_PA_in_Rs").getValue().toString();
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
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Education.equals("Yes")) {

            reff9 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Education").child(activity);
            reff9.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Salary = dataSnapshot.child(jobReference).child("Salary_PA_in_Rs").getValue().toString();
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
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Installation.equals("Yes")) {

            reff10 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Installation, Repair and Maintenance").child(activity);
            reff10.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Salary = dataSnapshot.child(jobReference).child("Salary_PA_in_Rs").getValue().toString();
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
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

    public void getSector(final String jobReference){

        if (Science.equals("Yes")) {

            reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Science and Technology").child(activity);
            reff1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Sector = dataSnapshot.child(jobReference).child("Sector").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sector.setText(Sector);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(sector.getText().toString(), sector);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Business.equals("Yes")) {

            reff2 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Business, Management and Administration").child(activity);
            reff2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Sector = dataSnapshot.child(jobReference).child("Sector").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sector.setText(Sector);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(sector.getText().toString(), sector);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Farming.equals("Yes")) {

            reff3 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Farming, Fishing and Forestry").child(activity);
            reff3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Sector = dataSnapshot.child(jobReference).child("Sector").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sector.setText(Sector);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(sector.getText().toString(), sector);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Community.equals("Yes")) {

            reff4 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Community and Social Services").child(activity);
            reff4.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Sector = dataSnapshot.child(jobReference).child("Sector").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sector.setText(Sector);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(sector.getText().toString(), sector);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Labors.equals("Yes")) {

            reff5 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Labour").child(activity);
            reff5.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Sector = dataSnapshot.child(jobReference).child("Sector").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sector.setText(Sector);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(sector.getText().toString(), sector);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Health.equals("Yes")) {

            reff6 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Healthcare and Medical").child(activity);
            reff6.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Sector = dataSnapshot.child(jobReference).child("Sector").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sector.setText(Sector);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(sector.getText().toString(), sector);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Communications.equals("Yes")) {

            reff7 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Communication").child(activity);
            reff7.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Sector = dataSnapshot.child(jobReference).child("Sector").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sector.setText(Sector);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(sector.getText().toString(), sector);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Arts.equals("Yes")) {

            reff8 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Arts, Culture and Entertainment").child(activity);
            reff8.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Sector = dataSnapshot.child(jobReference).child("Sector").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sector.setText(Sector);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(sector.getText().toString(), sector);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Education.equals("Yes")) {

            reff9 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Education").child(activity);
            reff9.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Sector = dataSnapshot.child(jobReference).child("Sector").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sector.setText(Sector);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(sector.getText().toString(), sector);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Installation.equals("Yes")) {

            reff10 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Installation, Repair and Maintenance").child(activity);
            reff10.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Sector = dataSnapshot.child(jobReference).child("Sector").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sector.setText(Sector);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(sector.getText().toString(), sector);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

    public void getJobDescription(final String jobReference){

        if (Science.equals("Yes")) {

            reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Science and Technology").child(activity);
            reff1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        jobDesc = dataSnapshot.child(jobReference).child("Job_Description").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    jobDescription.setText(jobDesc);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(jobDescription.getText().toString(), jobDescription);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Business.equals("Yes")) {

            reff2 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Business, Management and Administration").child(activity);
            reff2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        jobDesc = dataSnapshot.child(jobReference).child("Job_Description").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    jobDescription.setText(jobDesc);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(jobDescription.getText().toString(), jobDescription);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Farming.equals("Yes")) {

            reff3 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Farming, Fishing and Forestry").child(activity);
            reff3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        jobDesc = dataSnapshot.child(jobReference).child("Job_Description").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    jobDescription.setText(jobDesc);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(jobDescription.getText().toString(), jobDescription);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Community.equals("Yes")) {

            reff4 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Community and Social Services").child(activity);
            reff4.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        jobDesc = dataSnapshot.child(jobReference).child("Job_Description").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    jobDescription.setText(jobDesc);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(jobDescription.getText().toString(), jobDescription);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Labors.equals("Yes")) {

            reff5 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Labour").child(activity);
            reff5.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        jobDesc = dataSnapshot.child(jobReference).child("Job_Description").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    jobDescription.setText(jobDesc);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(jobDescription.getText().toString(), jobDescription);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Health.equals("Yes")) {

            reff6 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Healthcare and Medical").child(activity);
            reff6.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        jobDesc = dataSnapshot.child(jobReference).child("Job_Description").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    jobDescription.setText(jobDesc);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(jobDescription.getText().toString(), jobDescription);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Communications.equals("Yes")) {

            reff7 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Communication").child(activity);
            reff7.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        jobDesc = dataSnapshot.child(jobReference).child("Job_Description").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    jobDescription.setText(jobDesc);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(jobDescription.getText().toString(), jobDescription);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Arts.equals("Yes")) {

            reff8 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Arts, Culture and Entertainment").child(activity);
            reff8.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        jobDesc = dataSnapshot.child(jobReference).child("Job_Description").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    jobDescription.setText(jobDesc);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(jobDescription.getText().toString(), jobDescription);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Education.equals("Yes")) {

            reff9 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Education").child(activity);
            reff9.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        jobDesc = dataSnapshot.child(jobReference).child("Job_Description").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    jobDescription.setText(jobDesc);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(jobDescription.getText().toString(), jobDescription);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        } else if (Installation.equals("Yes")) {

            reff10 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Installation, Repair and Maintenance").child(activity);
            reff10.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        jobDesc = dataSnapshot.child(jobReference).child("Job_Description").getValue().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    jobDescription.setText(jobDesc);
                    if (check.equals("Hin")) {
                        getTranslateService();
                        try {
                            translateToHin(jobDescription.getText().toString(), jobDescription);
                        } catch (Exception e) {
                            Toast.makeText(Job_Details1.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Job_Details1.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            });

        }

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
