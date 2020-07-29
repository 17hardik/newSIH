package com.example.sih.PublishJob;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.Jobs.jobPost;
import com.example.sih.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class jobsPublished extends AppCompatActivity {


    private TextView companyName, companyLocation, status;
    Button createJob;
    DatabaseReference reff;
    RecyclerView recyclerView;
    ArrayList<jobPost> list;
    jAdapter adapter;
    int i, j;
    String phone, S, M, check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_published);
        SharedPreferences preferences1 = getSharedPreferences(S,i);
        phone = preferences1.getString("Phone","");
        SharedPreferences preferences2 = getSharedPreferences(M,j);
        check = preferences2.getString("Lang","Eng");
        companyName = findViewById(R.id.companyName);
        companyLocation = findViewById(R.id.companyLocation);
        createJob = findViewById(R.id.createJob);
        status = findViewById(R.id.status);
        recyclerView = findViewById(R.id.jobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<jobPost>();

        if(!check.equals(getResources().getString(R.string.english))){
            createJob.setText("अपनी नोकरी बनाओ");
        }

        reff = FirebaseDatabase.getInstance().getReference().child("Job Post");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    dataSnapshot.child(phone).getValue().toString();
                    if(check.equals(getResources().getString(R.string.english))) {
                        status.setText("You have created the following jobs:");
                    } else {
                        status.setText("आपने निम्नलिखित नौकारिया बनाई है:");
                    }
                } catch (Exception e){
                    if(check.equals(getResources().getString(R.string.english))) {
                        status.setText("You have not created any job yet");
                    } else {
                        status.setText("आपने अभी तक कोई नौकरी नहीं बनाई है");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals(getResources().getString(R.string.english))) {
                    Toast.makeText(jobsPublished.this, "Something went wrong", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(jobsPublished.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                }
            }
        });

        reff = FirebaseDatabase.getInstance().getReference().child("Company Representative Details").child(phone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {

                    String name = dataSnapshot.child("cname").getValue().toString();
                    String location = dataSnapshot.child("cloc").getValue().toString();
                    companyName.setText(name);
                    companyLocation.setText(location);

                } catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals(getResources().getString(R.string.english))) {
                    Toast.makeText(jobsPublished.this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(jobsPublished.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                }
            }
        });

        reff = FirebaseDatabase.getInstance().getReference().child("Job Post").child(phone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                list = new ArrayList<jobPost>();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    jobPost j = dataSnapshot1.getValue(jobPost.class);
                    list.add(j);
                }
                adapter = new jAdapter(jobsPublished.this,list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals(getResources().getString(R.string.english))) {
                    Toast.makeText(jobsPublished.this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(jobsPublished.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                }
            }
        });

        createJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(jobsPublished.this, jobDetails.class);
                startActivity(intent);
            }
        });

    }
}
