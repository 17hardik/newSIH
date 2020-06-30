package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Testing extends AppCompatActivity {

    DatabaseReference reff;
    RecyclerView job_s;
    ArrayList<data_in_cardview> details;
    gov_adapter govAdapter;
    int j, size;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        job_s = findViewById(R.id.job_s);
        job_s.setLayoutManager(new LinearLayoutManager(this));
        details = new ArrayList<>();

        reff = FirebaseDatabase.getInstance().getReference().child("Jobs").child("Government");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                data_in_cardview d = dataSnapshot.getValue(data_in_cardview.class);
                details.add(d);
                size = (int) dataSnapshot.getChildrenCount();

                if (dataSnapshot.exists()) {

                    for (j=0;j<=size;j++){

                        String i = Integer.toString(j);
//                        details.add()

                    }


                    govAdapter = new gov_adapter(Testing.this, details);
                    job_s.setAdapter(govAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Testing.this, "This is Inevitable", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
