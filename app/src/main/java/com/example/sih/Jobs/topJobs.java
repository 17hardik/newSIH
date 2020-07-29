package com.example.sih.Jobs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sih.R;
import com.firebase.client.Firebase;
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

public class topJobs extends AppCompatActivity {

    Intent intent;
    int j;
    String CategoryNumber, Category, description, URL, check, M;
    TextView CategoryTV, descriptionTV;
    Button rankingButton;
    DatabaseReference reff;
    Translate translate;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M, j);
        check = preferences1.getString("Lang", "Eng");
        setContentView(R.layout.activity_top_jobs);
        intent = getIntent();
        CategoryNumber = intent.getStringExtra("CategoryNumber");
        CategoryTV = findViewById(R.id.category);
        descriptionTV = findViewById(R.id.description);
        rankingButton = findViewById(R.id.viewRank);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(check.equals(getResources().getString(R.string.english))) {
            actionBar.setTitle("Top Jobs");
        } else {
            actionBar.setTitle("शीर्ष नौकरियां");
            rankingButton.setText("रैंकिंग दिखाएँ");
        }
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
                if(!check.equals(getResources().getString(R.string.english))){
                    getTranslateService();
                    translateToHin(CategoryTV.getText().toString(), CategoryTV);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals(getResources().getString(R.string.english))){
                    Toast.makeText(topJobs.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(topJobs.this, getResources().getString(R.string.check_internet1), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void getDescription(final String CategoryNumber){

        Firebase.setAndroidContext(this);
        pd = new ProgressDialog(topJobs.this);
        if(check.equals(getResources().getString(R.string.english))){
            pd.setMessage(getResources().getString(R.string.loading));
        } else {
            pd.setMessage(getResources().getString(R.string.loading1));
        }
        pd.show();

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                description = dataSnapshot.child("Category"+CategoryNumber).child("Description").getValue().toString();
                descriptionTV.setText(description);
                if(!check.equals(getResources().getString(R.string.english))){
                    getTranslateService();
                    translateToHin(descriptionTV.getText().toString(), descriptionTV);
                }
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals(getResources().getString(R.string.english))){
                    Toast.makeText(topJobs.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(topJobs.this, getResources().getString(R.string.check_internet1), Toast.LENGTH_SHORT).show();
                }

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
                if(check.equals(getResources().getString(R.string.english))){
                    Toast.makeText(topJobs.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(topJobs.this, getResources().getString(R.string.check_internet1), Toast.LENGTH_SHORT).show();
                }
            }
        });
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




