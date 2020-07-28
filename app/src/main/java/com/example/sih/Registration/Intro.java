package com.example.sih.Registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sih.MainActivity;
import com.example.sih.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/** Introduction activity which will execute first on every device
 * A kind of flash activity which will execute for two seconds that include app's logo
 */

public class Intro extends AppCompatActivity {

    int time = 3000, i, g, y, m;
    String S, isLogged, L, status, accountInfo, X, premium_date, phone, E;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(S,i);
        phone = preferences.getString("Phone","");
        getSupportActionBar().hide();
        //checking whether the user has logged in already by using SharedPreferences
        isLogged = preferences.getString("Status","Null");
        SharedPreferences preferences1 = getSharedPreferences(L,g);
        //checking whether the app is running first time on a particular device
        status = preferences1.getString("isOpened","Not Opened");
        SharedPreferences preferences2 = getSharedPreferences(X,y);
        //checking whether the app is running first time on a particular device
        accountInfo = preferences2.getString("isDeleted","No");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        Intent intent = getIntent();
        try {
            String emailLink = intent.getData().toString();
            SharedPreferences.Editor editor = getSharedPreferences(E, m).edit();
            editor.putString("isVerified", "Yes");
            editor.apply();
            Toast.makeText(this, "Your email id has been successfully verified", Toast.LENGTH_SHORT).show();
        } catch(Exception e) {

        }
        try {
            reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        premium_date = snapshot.child("Premium Date").getValue().toString();
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("isPremium", "Yes");
                        editor.apply();
                    } catch (Exception e) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("isPremium", "No");
                        editor.apply();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch(Exception e) {

        }
        new Handler().postDelayed((new Runnable() {
            @Override
            public void run() {
                // If the user is opening app for the first time then user will be redirected to Language activity for language selection
                if(status.equals("Not Opened")){
                    Intent lang = new Intent(Intro.this, Language.class);
                    startActivity(lang);
                }
                // If the user has not logged in then user will be redirected to Login activity
                else if(!isLogged.equals("Yes") || accountInfo.equals("Yes")) {
                    Intent intro = new Intent(Intro.this, Login.class);
                    startActivity(intro);
                }
                else {
                    Intent intent = new Intent(Intro.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }),time);
    }
}