package com.example.sih.Jobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.sih.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class jobApply extends AppCompatActivity {

    private WebView webView;
    DatabaseReference reff1;
    String J, jobCategory, jobReference, domainType, applyLink;
    int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences1 = getSharedPreferences(J,x);
        jobCategory = preferences1.getString("jobCategory", "");
        jobReference = preferences1.getString("jobReference", "");
        domainType = preferences1.getString("domainType", "");

        setContentView(R.layout.activity_job_apply);

        webView = findViewById(R.id.webView);

        reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child(domainType).child(jobCategory).child(jobReference);
        reff1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                     applyLink = dataSnapshot.child("Apply Link").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(applyLink);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(jobApply.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    @Override
    public void onBackPressed() {

        if (webView.canGoBack()){

            webView.goBack();

        }
        else {
            super.onBackPressed();
        }
    }

}