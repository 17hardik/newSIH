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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButtonEng = findViewById(R.id.buttonEnglish);
        ButtonHin = findViewById(R.id.buttonHindi);
        SharedPreferences.Editor editor = getSharedPreferences(L,g).edit();
        editor.putString("Status","Opened");
        editor.apply();
        ButtonEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang = "Eng";
                SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
                editor1.putString("Lang",lang);
                editor1.apply();
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
                Intent intent = new Intent(Language.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}