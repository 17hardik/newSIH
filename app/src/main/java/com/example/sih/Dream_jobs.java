package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
    String S, category, position;
    String phone;
    int  i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(S,i);
        phone= preferences.getString("Phone","");

        setContentView(R.layout.activity_dream_jobs);
        dream_jobs = findViewById(R.id.dream_jobs);
        dream_jobs.setLayoutManager(new LinearLayoutManager(this));
        details = new ArrayList<>();

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("Dream Jobs");

        pd = new ProgressDialog(Dream_jobs.this);
        pd.setMessage("Getting your Dream Jobs");
        pd.show();

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    try {
                        String dreamJob = dataSnapshot.getValue().toString();
                        String[] arrOfStr = dreamJob.split("-", 2);
                        category = arrOfStr[0];
                        position = arrOfStr[1];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs").child(category).child(position);
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
                            Toast.makeText(Dream_jobs.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dream_jobs.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
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
        actionBar.setTitle("Your Dream Jobs");

    }
}