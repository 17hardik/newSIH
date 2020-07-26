package com.example.sih.Jobs;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dream_jobs extends AppCompatActivity {
    DatabaseReference reff, reff1;
    RecyclerView dream_jobs;
    ArrayList<data_in_cardview> details;
    gov_adapter govAdapter;
    ProgressDialog pd;
    String S, domainType, category, position, check, M;
    String phone;
    int  i, j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M, j);
        check = preferences1.getString("Lang", "Eng");
        SharedPreferences preferences = getSharedPreferences(S,i);
        phone= preferences.getString("Phone","");

        setContentView(R.layout.activity_dream_jobs);
        dream_jobs = findViewById(R.id.dream_jobs);
        dream_jobs.setLayoutManager(new LinearLayoutManager(this));
        details = new ArrayList<>();

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("Dream Jobs");

        pd = new ProgressDialog(Dream_jobs.this);
        if(check.equals("Eng")) {
            pd.setMessage("Getting your Dream Jobs");
        } else {
            pd.setMessage("नौकरियां एकत्रित की जा रही हैं");
        }
        pd.show();

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    try {
                        String dreamJob = dataSnapshot.getValue().toString();
                        String[] arrOfStr = dreamJob.split("-", 3);
                        domainType = arrOfStr[0];
                        category = arrOfStr[1];
                        position = arrOfStr[2];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child(domainType).child(category).child(position);
                    reff1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                data_in_cardview d = snapshot.getValue(data_in_cardview.class);
                                details.add(d);
                                govAdapter = new gov_adapter(Dream_jobs.this, details);
                                dream_jobs.setAdapter(govAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            if(check.equals("Eng")) {
                                Toast.makeText(Dream_jobs.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Dream_jobs.this, "कृपया अपने इंटरनेट कनेक्शन की जाँच करें", Toast.LENGTH_SHORT).show();
                            }                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(check.equals("Eng")) {
                    Toast.makeText(Dream_jobs.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Dream_jobs.this, "कृपया अपने इंटरनेट कनेक्शन की जाँच करें", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
            }
        }, 3000);
        ActionBar actionBar = getSupportActionBar();
        if(check.equals("Eng")) {
            actionBar.setTitle("Dream Jobs");
        } else {
            actionBar.setTitle("ड्रीम जॉब्स");
        }
    }
}