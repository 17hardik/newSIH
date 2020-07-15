package com.example.sih;

import androidx.appcompat.app.AppCompatActivity;

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
    String check, M, S;
    int i, j;

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
                        editor.putString("Science", "Yes");
                        editor.apply();
                        count++;
                    }
                    if (cbBusiness.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Business", "Yes");
                        editor.apply();
                        count++;
                    }
                    if (cbFarming.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Farming", "Yes");
                        editor.apply();
                        count++;
                    }
                    if (cbCommunity.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Community", "Yes");
                        editor.apply();
                        count++;
                    }
                    if (cbLabors.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Labors", "Yes");
                        editor.apply();
                        count++;
                    }
                    if (cbHealth.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Health", "Yes");
                        editor.apply();
                        count++;
                    }
                    if (cbCommunications.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Communications", "Yes");
                        editor.apply();
                        count++;
                    }
                    if (cbArts.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Arts", "Yes");
                        editor.apply();
                        count++;
                    }
                    if (cbEducation.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Education", "Yes");
                        editor.apply();
                        count++;
                    }
                    if (cbInstallation.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Installation", "Yes");
                        editor.apply();
                        count++;
                    }
                    if (cbOthers.isChecked()) {
                        count++;
                    }

                    if(count > 1){
                        if (check.equals("Hin")) {
                            Toast.makeText(Favorite_Sectors.this, R.string.atmost_three_sectors, Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(Favorite_Sectors.this, "You can select atmost one sector", Toast.LENGTH_SHORT).show();
                        }
                        SharedPreferences.Editor editor = getSharedPreferences(S,i).edit();
                        editor.putString("Science", "No");
                        editor.putString("Business", "No");
                        editor.putString("Farming", "No");
                        editor.putString("Community", "No");
                        editor.putString("Labors", "No");
                        editor.putString("Health", "No");
                        editor.putString("Communications", "No");
                        editor.putString("Arts", "No");
                        editor.putString("Education", "No");
                        editor.putString("Installation", "No");
                        editor.apply();
                    }
                }
            }
        });

    }
}

