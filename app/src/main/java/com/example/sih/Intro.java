package com.example.sih;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

/** Introduction activity which will execute first on every device
 * A kind of flash activity which will execute for two seconds that include app's logo
 */

public class Intro extends AppCompatActivity {
    private int time = 2000, i, g;
    String S, isLogged, L, status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        SharedPreferences preferences = getSharedPreferences(S,i);

        //checking whether the user has logged in already by using SharedPreerences
        isLogged = preferences.getString("Status","Null");
        SharedPreferences preferences1 = getSharedPreferences(L,g);
        //checking whether the app is running first time on a particular device
        status = preferences1.getString("Status","Not Opened");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        new Handler().postDelayed((new Runnable() {
            @Override
            public void run() {
                // If the user is opening app for the first time then user will be redirected to Language activity for language selection
                if(status.equals("Not Opened")){
                    Intent lang = new Intent(Intro.this, Language.class);
                    startActivity(lang);
                }
                // If the user has not logged in then user will be redirected to Login activity
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