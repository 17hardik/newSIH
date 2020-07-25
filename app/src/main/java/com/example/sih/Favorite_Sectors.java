package com.example.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

public class Favorite_Sectors extends AppCompatActivity {

    CheckBox cbScience, cbBusiness, cbFarming, cbCommunity, cbLabors, cbHealth, cbCommunications, cbArts, cbEducation, cbInstallation, cbOthers;
    Button submitButton;
    String check, M, S, A;
    int i, j, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");

        setContentView(R.layout.activity_favorite__sectors);

        cbScience = findViewById(R.id.cbscience);
        cbBusiness = findViewById(R.id.cbbusiness);
        cbFarming = findViewById(R.id.cbfarming);
        cbCommunity = findViewById(R.id.cbcommunity);
        cbLabors = findViewById(R.id.cblabors);
        cbHealth = findViewById(R.id.cbhealth);
        cbCommunications = findViewById(R.id.cbcommunications);
        cbArts = findViewById(R.id.cbarts);
        cbEducation = findViewById(R.id.cbeducation);
        cbInstallation = findViewById(R.id.cbinstallation);
        cbOthers = findViewById(R.id.cbothers);
        submitButton = findViewById(R.id.buttonsubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                if (!(cbScience.isChecked()) && !(cbBusiness.isChecked()) && !(cbFarming.isChecked()) && !(cbCommunity.isChecked()) && !(cbLabors.isChecked()) && !(cbHealth.isChecked()) && !(cbCommunications.isChecked()) && !(cbArts.isChecked()) && !(cbEducation.isChecked()) && !(cbInstallation.isChecked()) && !(cbOthers.isChecked())) {
                    if (check.equals("Hin")) {
                        Toast.makeText(Favorite_Sectors.this, R.string.atleast_one_option, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Favorite_Sectors.this, "Please select atleast one option", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (cbScience.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Science");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        startActivity(new Intent(Favorite_Sectors.this, MainActivity.class));
                        finishAffinity();
                    }
                    if (cbBusiness.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Business");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        startActivity(new Intent(Favorite_Sectors.this, MainActivity.class));
                        finishAffinity();
                    }
                    if (cbFarming.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Farming");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        startActivity(new Intent(Favorite_Sectors.this, MainActivity.class));
                        finishAffinity();
                    }
                    if (cbCommunity.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Community");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        startActivity(new Intent(Favorite_Sectors.this, MainActivity.class));
                        finishAffinity();
                    }
                    if (cbLabors.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Labors");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        startActivity(new Intent(Favorite_Sectors.this, MainActivity.class));
                        finishAffinity();
                    }
                    if (cbHealth.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Health");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        startActivity(new Intent(Favorite_Sectors.this, MainActivity.class));
                        finishAffinity();
                    }
                    if (cbCommunications.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Communications");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        startActivity(new Intent(Favorite_Sectors.this, MainActivity.class));
                        finishAffinity();
                    }
                    if (cbArts.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Arts");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        startActivity(new Intent(Favorite_Sectors.this, MainActivity.class));
                        finishAffinity();
                    }
                    if (cbEducation.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Education");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        startActivity(new Intent(Favorite_Sectors.this, MainActivity.class));
                        finishAffinity();
                    }
                    if (cbInstallation.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Installation");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        startActivity(new Intent(Favorite_Sectors.this, MainActivity.class));
                        finishAffinity();
                    }
                    if (cbOthers.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Others");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        startActivity(new Intent(Favorite_Sectors.this, MainActivity.class));
                        finishAffinity();
                    }

                    if(count > 1){
                        if (check.equals("Hin")) {
                            Toast.makeText(Favorite_Sectors.this, R.string.atmost_three_sectors, Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(Favorite_Sectors.this, "You can select atmost one sector", Toast.LENGTH_SHORT).show();
                        }
                        SharedPreferences.Editor editor = getSharedPreferences(S,i).edit();
                        editor.putString("Domain", "No");
                        editor.apply();
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst", "notFirst");
                        editor1.apply();

                    }
                }
            }
        });

    }
}

