package com.example.sih;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class introRoadmap extends AppCompatActivity {
    Button start;
    TextView Title, Description;
    String M, check;
    int j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        setContentView(R.layout.activity_intro_roadmap);

        start = findViewById(R.id.start);
        Title = findViewById(R.id.mt1);
        Description = findViewById(R.id.mt2);

        if(check.equals("Hin")){
            Title.setText(R.string.roadmap);
            Description.setText(R.string.roadmap1);
            start.setText(R.string.get_started1);
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(introRoadmap.this, careerFields.class);
                startActivity(intent);
            }
        });
    }
}