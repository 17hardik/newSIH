package com.example.sih;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class Intro extends AppCompatActivity {
    int time = 2000, i, g;
    String S, isLogged, L, status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        SharedPreferences preferences = getSharedPreferences(S,i);
        isLogged = preferences.getString("Status","Null");
        SharedPreferences preferences1 = getSharedPreferences(L,g);
        status = preferences1.getString("Status","Not Opened");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        new Handler().postDelayed((new Runnable() {
            @Override
            public void run() {
                if(status.equals("Not Opened")){
                    Intent lang = new Intent(Intro.this, Language.class);
                    startActivity(lang);
                }
                else if(isLogged.equals("Null")) {
                    Intent intro = new Intent(Intro.this, Login.class);
                    startActivity(intro);
                }
                else{
                    Intent intent = new Intent(Intro.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }),time);
    }
}