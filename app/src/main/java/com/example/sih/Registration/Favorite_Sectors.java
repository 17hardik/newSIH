package com.example.sih.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sih.Jobs.Free_Lancing;
import com.example.sih.Jobs.Government;
import com.example.sih.Jobs.Non_Government;
import com.example.sih.Jobs.Tenders;
import com.example.sih.R;
import com.example.sih.chatApp.User_List;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Favorite_Sectors extends AppCompatActivity {

    RadioButton cbScience, cbBusiness, cbFarming, cbCommunity, cbLabors, cbHealth, cbCommunications, cbArts, cbEducation, cbInstallation, cbOthers;
    Button submitButton;
    String check, activity, M, S, A, J;
    TextView Title;
    int i, j, b, x;
    Translate translate;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");

        SharedPreferences preferences2 = getSharedPreferences(J,x);
        activity = preferences2.getString("Activity","");

        setContentView(R.layout.activity_favorite__sectors);

        setTitle("Job Sector");

        cbScience = findViewById(R.id.cbscience);
        cbBusiness = findViewById(R.id.cbbusiness);
        cbFarming = findViewById(R.id.cbfarming);
        cbCommunity = findViewById(R.id.cbcommunity);
        cbLabors = findViewById(R.id.cblabors);
        cbHealth = findViewById(R.id.cbhealth);
        Title = findViewById(R.id.header);
        cbCommunications = findViewById(R.id.cbcommunications);
        cbArts = findViewById(R.id.cbarts);
        cbEducation = findViewById(R.id.cbeducation);
        cbInstallation = findViewById(R.id.cbinstallation);
        cbOthers = findViewById(R.id.cbothers);
        submitButton = findViewById(R.id.buttonsubmit);
        pd = new ProgressDialog(Favorite_Sectors.this);

        if(!check.equals(getResources().getString(R.string.english))) {
            pd.setMessage(getResources().getString(R.string.loading1));
            pd.show();
        }

        if(!check.equals(getResources().getString(R.string.english))){
           Title.setText("अपने नौकरी क्षेत्र का चयन करें");
           submitButton.setText("चुनाव करें");
           Objects.requireNonNull(getSupportActionBar()).setTitle("नौकरी क्षेत्र");
           getTranslateService();
           translateToHin(cbScience.getText().toString(), cbScience);
           translateToHin(cbBusiness.getText().toString(), cbBusiness);
           translateToHin(cbFarming.getText().toString(), cbFarming);
           translateToHin(cbCommunity.getText().toString(), cbCommunity);
           translateToHin(cbLabors.getText().toString(), cbLabors);
           translateToHin(cbHealth.getText().toString(), cbHealth);
           translateToHin(cbCommunications.getText().toString(), cbCommunications);
           translateToHin(cbArts.getText().toString(), cbArts);
           translateToHin(cbEducation.getText().toString(), cbEducation);
           translateToHin(cbInstallation.getText().toString(), cbInstallation);
           translateToHin(cbOthers.getText().toString(), cbOthers);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                }
            }, 3000);
        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                if (!(cbScience.isChecked()) && !(cbBusiness.isChecked()) && !(cbFarming.isChecked()) && !(cbCommunity.isChecked()) && !(cbLabors.isChecked()) && !(cbHealth.isChecked()) && !(cbCommunications.isChecked()) && !(cbArts.isChecked()) && !(cbEducation.isChecked()) && !(cbInstallation.isChecked()) && !(cbOthers.isChecked())) {
                    if (!check.equals(getResources().getString(R.string.english))) {
                        Toast.makeText(Favorite_Sectors.this, R.string.atleast_one_option, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Favorite_Sectors.this, "Please select at least one option", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (cbScience.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Science and Technology");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        if (activity.equals("Government")) {
                            startActivity(new Intent(Favorite_Sectors.this, Government.class));
                            finish();
                        }
                        else if (activity.equals("Private")){
                            startActivity(new Intent(Favorite_Sectors.this, Non_Government.class));
                            finish();
                        }
                        else if (activity.equals("Freelancing")){
                            startActivity(new Intent(Favorite_Sectors.this, Free_Lancing.class));
                            finish();
                        }
                        else if (activity.equals("Tender")){
                            startActivity(new Intent(Favorite_Sectors.this, Tenders.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(Favorite_Sectors.this, User_List.class));
                            finish();
                        }
                    }
                    if (cbBusiness.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Business, Management and Administration");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        if (activity.equals("Government")) {
                            startActivity(new Intent(Favorite_Sectors.this, Government.class));
                            finish();
                        }
                        else if (activity.equals("Private")){
                            startActivity(new Intent(Favorite_Sectors.this, Non_Government.class));
                            finish();
                        }
                        else if (activity.equals("Freelancing")){
                            startActivity(new Intent(Favorite_Sectors.this, Free_Lancing.class));
                            finish();
                        }
                        else if (activity.equals("Tender")){
                            startActivity(new Intent(Favorite_Sectors.this, Tenders.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(Favorite_Sectors.this, User_List.class));
                            finish();
                        }
                    }
                    if (cbFarming.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Farming, Fishing and Forestry");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        if (activity.equals("Government")) {
                            startActivity(new Intent(Favorite_Sectors.this, Government.class));
                            finish();
                        }
                        else if (activity.equals("Private")){
                            startActivity(new Intent(Favorite_Sectors.this, Non_Government.class));
                            finish();
                        }
                        else if (activity.equals("Freelancing")){
                            startActivity(new Intent(Favorite_Sectors.this, Free_Lancing.class));
                            finish();
                        }
                        else if (activity.equals("Tender")){
                            startActivity(new Intent(Favorite_Sectors.this, Tenders.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(Favorite_Sectors.this, User_List.class));
                            finish();
                        }
                    }
                    if (cbCommunity.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Community and Social Services");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        if (activity.equals("Government")) {
                            startActivity(new Intent(Favorite_Sectors.this, Government.class));
                            finish();
                        }
                        else if (activity.equals("Private")){
                            startActivity(new Intent(Favorite_Sectors.this, Non_Government.class));
                            finish();
                        }
                        else if (activity.equals("Freelancing")){
                            startActivity(new Intent(Favorite_Sectors.this, Free_Lancing.class));
                            finish();
                        }
                        else if (activity.equals("Tender")){
                            startActivity(new Intent(Favorite_Sectors.this, Tenders.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(Favorite_Sectors.this, User_List.class));
                            finish();
                        }
                    }
                    if (cbLabors.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Labour");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        if (activity.equals("Government")) {
                            startActivity(new Intent(Favorite_Sectors.this, Government.class));
                            finish();
                        }
                        else if (activity.equals("Private")){
                            startActivity(new Intent(Favorite_Sectors.this, Non_Government.class));
                            finish();
                        }
                        else if (activity.equals("Freelancing")){
                            startActivity(new Intent(Favorite_Sectors.this, Free_Lancing.class));
                            finish();
                        }
                        else if (activity.equals("Tender")){
                            startActivity(new Intent(Favorite_Sectors.this, Tenders.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(Favorite_Sectors.this, User_List.class));
                            finish();
                        }
                    }
                    if (cbHealth.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Healthcare and Medical");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        if (activity.equals("Government")) {
                            startActivity(new Intent(Favorite_Sectors.this, Government.class));
                            finish();
                        }
                        else if (activity.equals("Private")){
                            startActivity(new Intent(Favorite_Sectors.this, Non_Government.class));
                            finish();
                        }
                        else if (activity.equals("Freelancing")){
                            startActivity(new Intent(Favorite_Sectors.this, Free_Lancing.class));
                            finish();
                        }
                        else if (activity.equals("Tender")){
                            startActivity(new Intent(Favorite_Sectors.this, Tenders.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(Favorite_Sectors.this, User_List.class));
                            finish();
                        }
                    }
                    if (cbCommunications.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Communication");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        if (activity.equals("Government")) {
                            startActivity(new Intent(Favorite_Sectors.this, Government.class));
                            finish();
                        }
                        else if (activity.equals("Private")){
                            startActivity(new Intent(Favorite_Sectors.this, Non_Government.class));
                            finish();
                        }
                        else if (activity.equals("Freelancing")){
                            startActivity(new Intent(Favorite_Sectors.this, Free_Lancing.class));
                            finish();
                        }
                        else if (activity.equals("Tender")){
                            startActivity(new Intent(Favorite_Sectors.this, Tenders.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(Favorite_Sectors.this, User_List.class));
                            finish();
                        }
                    }
                    if (cbArts.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Arts, Culture and Entertainment");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        if (activity.equals("Government")) {
                            startActivity(new Intent(Favorite_Sectors.this, Government.class));
                            finish();
                        }
                        else if (activity.equals("Private")){
                            startActivity(new Intent(Favorite_Sectors.this, Non_Government.class));
                            finish();
                        }
                        else if (activity.equals("Freelancing")){
                            startActivity(new Intent(Favorite_Sectors.this, Free_Lancing.class));
                            finish();
                        }
                        else if (activity.equals("Tender")){
                            startActivity(new Intent(Favorite_Sectors.this, Tenders.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(Favorite_Sectors.this, User_List.class));
                            finish();
                        }
                    }
                    if (cbEducation.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Education");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        if (activity.equals("Government")) {
                            startActivity(new Intent(Favorite_Sectors.this, Government.class));
                            finish();
                        }
                        else if (activity.equals("Private")){
                            startActivity(new Intent(Favorite_Sectors.this, Non_Government.class));
                            finish();
                        }
                        else if (activity.equals("Freelancing")){
                            startActivity(new Intent(Favorite_Sectors.this, Free_Lancing.class));
                            finish();
                        }
                        else if (activity.equals("Tender")){
                            startActivity(new Intent(Favorite_Sectors.this, Tenders.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(Favorite_Sectors.this, User_List.class));
                            finish();
                        }
                    }
                    if (cbInstallation.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Installation, Repair and Maintenance");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        if (activity.equals("Government")) {
                            startActivity(new Intent(Favorite_Sectors.this, Government.class));
                            finish();
                        }
                        else if (activity.equals("Private")){
                            startActivity(new Intent(Favorite_Sectors.this, Non_Government.class));
                            finish();
                        }
                        else if (activity.equals("Freelancing")){
                            startActivity(new Intent(Favorite_Sectors.this, Free_Lancing.class));
                            finish();
                        }
                        else if (activity.equals("Tender")){
                            startActivity(new Intent(Favorite_Sectors.this, Tenders.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(Favorite_Sectors.this, User_List.class));
                            finish();
                        }
                    }
                    if (cbOthers.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                        editor.putString("Domain", "Others");
                        editor.apply();
                        count++;
                        SharedPreferences.Editor editor1 = getSharedPreferences(A,b).edit();
                        editor1.putString("isFirst","First");
                        editor1.apply();
                        if (activity.equals("Government")) {
                            startActivity(new Intent(Favorite_Sectors.this, Government.class));
                            finish();
                        }
                        else if (activity.equals("Private")){
                            startActivity(new Intent(Favorite_Sectors.this, Non_Government.class));
                            finish();
                        }
                        else if (activity.equals("Freelancing")){
                            startActivity(new Intent(Favorite_Sectors.this, Free_Lancing.class));
                            finish();
                        }
                        else if (activity.equals("Tender")){
                            startActivity(new Intent(Favorite_Sectors.this, Tenders.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(Favorite_Sectors.this, User_List.class));
                            finish();
                        }
                    }
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


    public void translateToHin (String originalText, RadioButton target) {
        Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage("hin"), Translate.TranslateOption.model("base"));
        target.setText(translation.getTranslatedText());
    }

}

