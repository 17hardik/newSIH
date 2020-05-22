package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class jobsPublished extends AppCompatActivity {

    DatabaseReference reff;
    RecyclerView recyclerView;
    ArrayList<jobPost> list;
    jAdapter adapter;
    int i;
    String phone, newPhone, S;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_published);

        SharedPreferences preferences1 = getSharedPreferences(S,i);
        phone = preferences1.getString("Phone","");

        recyclerView = (RecyclerView) findViewById(R.id.jobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<jobPost>();

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

    }
}
