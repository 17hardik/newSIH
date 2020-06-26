package com.example.sih;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Language extends AppCompatActivity {
    String L, lang, M;
    int g, j;
    Button ButtonEng, ButtonHin;

//This is the activity from where users can select their desired language just after opening the app for first time
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButtonEng = findViewById(R.id.buttonEnglish);
        ButtonHin = findViewById(R.id.buttonHindi);
        ButtonEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang = "Eng";
                SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
                editor1.putString("Lang",lang);
                editor1.apply();
                SharedPreferences.Editor editor = getSharedPreferences(L,g).edit();
                editor.putString("isOpened","Opened");
                editor.apply();
                Intent intent = new Intent(Language.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        ButtonHin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang = "Hin";
                SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
                editor1.putString("Lang",lang);
                editor1.apply();
                SharedPreferences.Editor editor = getSharedPreferences(L,g).edit();
                editor.putString("isOpened","Opened");
                editor.apply();
                Intent intent = new Intent(Language.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}