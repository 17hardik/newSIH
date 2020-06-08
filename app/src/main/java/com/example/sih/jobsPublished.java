package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class jobsPublished extends AppCompatActivity {


    private TextView companyName, companyLocation, status;
    Button createJob;
    DatabaseReference reff;
    RecyclerView recyclerView;
    ArrayList<jobPost> list;
    jAdapter adapter;
    int i;
    String phone, S;
    Boolean isCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_published);

        SharedPreferences preferences1 = getSharedPreferences(S,i);
        phone = preferences1.getString("Phone","");

        companyName = findViewById(R.id.companyName);
        companyLocation = findViewById(R.id.companyLocation);
        createJob = findViewById(R.id.createJob);
        status = findViewById(R.id.status);
        recyclerView = findViewById(R.id.jobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<jobPost>();

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child("Job Post");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    String num = dataSnapshot.child(phone).getValue().toString();
                    isCreated = true;
                } catch (Exception e){
                    isCreated = false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(jobsPublished.this, "Something went wrong",Toast.LENGTH_LONG).show();
            }
        });

        if (!isCreated){
            status.setText("No jobs created yet");
        } else{
            status.setText("You created the following jobs:");
        }

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child("Company Representative Details").child(phone);
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
                Toast.makeText(jobsPublished.this, "Something went wrong",Toast.LENGTH_LONG).show();
            }
        });

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child("Job Post").child(phone);
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
                Toast.makeText(jobsPublished.this, "Something went wrong",Toast.LENGTH_LONG).show();
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
