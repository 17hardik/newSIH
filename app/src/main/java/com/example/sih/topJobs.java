package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class topJobs extends AppCompatActivity {

    Intent intent;
    String CategoryNumber, Category, description, URL;
    TextView CategoryTV, descriptionTV;
    Button rankingButton;
    DatabaseReference reff;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_jobs);

        intent = getIntent();
        CategoryNumber = intent.getStringExtra("CategoryNumber");
        CategoryTV = findViewById(R.id.category);
        descriptionTV = findViewById(R.id.description);
        rankingButton = findViewById(R.id.viewRank);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Top Jobs");

        getCategory(CategoryNumber);
        getDescription(CategoryNumber);
        getURL(CategoryNumber);

    }

    public void getCategory(final String CategoryNumber){
        reff = FirebaseDatabase.getInstance().getReference().child("Top Jobs");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Category = dataSnapshot.child("Category"+CategoryNumber).child("Category").getValue().toString();
                CategoryTV.setText(Category);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(topJobs.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void getDescription(final String CategoryNumber){

        Firebase.setAndroidContext(this);
        pd = new ProgressDialog(topJobs.this);
        pd.setMessage("Loading...");
        pd.show();

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                description = dataSnapshot.child("Category"+CategoryNumber).child("Description").getValue().toString();
                descriptionTV.setText(description);
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(topJobs.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getURL(final String CategoryNumber) {
        reff = FirebaseDatabase.getInstance().getReference().child("Top Jobs");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                URL = dataSnapshot.child("Category"+CategoryNumber).child("URL").getValue().toString();
                rankingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent redirectIntent = new Intent(Intent.ACTION_VIEW);
                        redirectIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                        redirectIntent.setData(Uri.parse(URL));
                        startActivity(redirectIntent);
                    }

                });
                rankingButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(topJobs.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

            }
        });
    }
}




