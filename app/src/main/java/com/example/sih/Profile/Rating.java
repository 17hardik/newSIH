package com.example.sih.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sih.MainActivity;
import com.example.sih.R;
import com.example.sih.Registration.Login;
import com.example.sih.chatApp.ContactUs;
import com.firebase.client.Firebase;

/** Activity through which users can rate this app
 * Feedback provided by users are stored in database associated with users's phone number
 */

public class Rating extends AppCompatActivity {

    ImageView charPlace;
    RatingBar rateStars;
    TextView ResultRate, TitleRate;
    Animation charanim;
    Button RatingButton;
    Boolean English = true;
    String phone, S, Rating, answerValue, M, check, lang;
    Menu menu1;
    int i, j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Rate Us");
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        SharedPreferences preferences = getSharedPreferences(S, i);
        phone = preferences.getString("Phone", "Null");
        setContentView(R.layout.activity_rating);
        ResultRate = findViewById(R.id.titleresult);
        TitleRate = findViewById(R.id.titlerate);
        RatingButton = findViewById(R.id.ratingButton);
        charPlace = findViewById(R.id.imageView);
        rateStars = findViewById(R.id.ratingstar);
        charanim = AnimationUtils.loadAnimation(this, R.anim.charanim);
        charPlace.startAnimation(charanim);
        if(!check.equals(getResources().getString(R.string.english))){
            toHin();
            English = false;
        }
        Rating = "Null";
        Firebase.setAndroidContext(this);
        rateStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                answerValue = String.valueOf((int) (rateStars.getRating()));
                if (answerValue.equals("1")) {
                    charPlace.setImageResource(R.drawable.cryemo);
                    charPlace.startAnimation(charanim);
                    if(!English) {
                        ResultRate.setText(R.string.poor1);
                    } else{
                        ResultRate.setText("Poor!");
                    }
                    Rating = "Poor (1 Star)";
                } else if (answerValue.equals("2")) {
                    charPlace.setImageResource(R.drawable.sademo);
                    charPlace.startAnimation(charanim);
                    if(!English) {
                        ResultRate.setText(R.string.just_so_so1);
                    } else{
                        ResultRate.setText("Just So So!");
                    }
                    Rating = "Just So So (2 Stars)";
                } else if (answerValue.equals("3")) {
                    charPlace.setImageResource(R.drawable.okemo);
                    charPlace.startAnimation(charanim);
                    if(!English) {
                        ResultRate.setText(R.string.not_bad1);
                    } else{
                        ResultRate.setText("Not Bad!");
                    }
                    Rating = "Not Bad (3 Stars)";
                } else if (answerValue.equals("4")) {
                    charPlace.setImageResource(R.drawable.happyemo);
                    charPlace.startAnimation(charanim);
                    if(!English) {
                        ResultRate.setText(R.string.good_job1);
                    } else{
                        ResultRate.setText("Good Job!");
                    }
                    Rating = "Good Job (4 Stars)";
                } else if (answerValue.equals("5")) {
                    charPlace.setImageResource(R.drawable.ic_awsomeemo);
                    charPlace.startAnimation(charanim);
                    if(!English) {
                        ResultRate.setText(R.string.amazing1);
                    } else{
                        ResultRate.setText("Amazing!");
                    }
                    Rating = "Amazing (5 Stars)";
                } else {
                    if(!check.equals(getResources().getString(R.string.english)) || !English){
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_points1) , Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "No Point", Toast.LENGTH_SHORT).show();
                    }
                    Rating = "Null";
                }
            }
        });

        RatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Rating.equals("Null")){
                    if(!check.equals(getResources().getString(R.string.english))){
                        Toast.makeText(Rating.this, getResources().getString(R.string.not_rated1), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Rating.this, "You have not rated yet", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Firebase reference = new Firebase("https://smart-e60d6.firebaseio.com/Ratings");
                    reference.child(phone).child("Rating").setValue(Rating);
                    if(!check.equals(getResources().getString(R.string.english))){
                        Toast.makeText(Rating.this, getResources().getString(R.string.thanks_for_feedback1) , Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Rating.this, "Thanks for your feedback", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu1 = menu;
        getMenuInflater().inflate(R.menu.option_menu,menu);
        if(!check.equals(getResources().getString(R.string.english))){
            optionHin();
        }else {
            optionEng();
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.switch1:
                if (English) {
                    toHin();
                    optionHin();
                } else {
                    toEng();
                    optionEng();
                }
                return true;

            case R.id.logout: {
                Intent intent = new Intent(Rating.this, Login.class);
                startActivity(intent);
                SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                editor.putString("Status", "Null");
                editor.apply();
                finishAffinity();
                break;
            }

            case R.id.contact_us:
                Intent intent = new Intent(Rating.this, ContactUs.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
    public void optionHin(){
        setOptionTitle(R.id.switch1, getResources().getString(R.string.switch1));
        setOptionTitle(R.id.rate_us, getResources().getString(R.string.rate1));
        setOptionTitle(R.id.logout, getResources().getString(R.string.logout1));
        setOptionTitle(R.id.contact_us, getResources().getString(R.string.contact_us1));
    }
    public void optionEng(){
        setOptionTitle(R.id.switch1, "Change Language");
        setOptionTitle(R.id.rate_us, "Rate Us");
        setOptionTitle(R.id.logout, "Logout");
        setOptionTitle(R.id.contact_us, "Contact Us");
    }
    private void setOptionTitle(int id, String title)
    {
        MenuItem item = menu1.findItem(id);
        item.setTitle(title);
    }
    public void toEng(){
        TitleRate.setText("Rate This App");
        RatingButton.setText("Give Feedback");
        getSupportActionBar().setTitle("Rate Us");
        switch(ResultRate.getText().toString()){
            case "अनुपयुक्त":
                ResultRate.setText("Poor!");
                break;
            case "ठीक ठाक":
                ResultRate.setText("Just So So!");
                break;
            case "संतोषजनक":
                ResultRate.setText("Not Bad!");
                break;
            case "बढ़िया":
                ResultRate.setText("Good Job!");
                break;
            case "अति उत्कृष्ट":
                ResultRate.setText("Amazing!");
                break;
        }
        English = true;
        lang = "Eng";
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
    }

    public void toHin(){
        TitleRate.setText(R.string.rate_title1);
        RatingButton.setText(R.string.give_feedback1);
        switch(ResultRate.getText().toString()){
            case "Poor!":
                ResultRate.setText(R.string.poor1);
                break;
            case "Just So So!":
                ResultRate.setText(R.string.just_so_so1);
                break;
            case "Not Bad!":
                ResultRate.setText(R.string.not_bad1);
                break;
            case "Good Job!":
                ResultRate.setText(R.string.good_job1);
                break;
            case "Amazing!":
                ResultRate.setText(R.string.amazing1);
                break;
        }
        getSupportActionBar().setTitle(R.string.rate1);
        English = false;
        lang = "Hin";
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
    }
}