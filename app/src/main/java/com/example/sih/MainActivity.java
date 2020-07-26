package com.example.sih;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sih.Jobs.Dream_jobs;
import com.example.sih.Jobs.Government;
import com.example.sih.Jobs.Non_Government;
import com.example.sih.Jobs.topJobsFragment;
import com.example.sih.Profile.Premium;
import com.example.sih.Profile.Rating;
import com.example.sih.PublishJob.CreateYourJob;
import com.example.sih.PublishJob.jobsPublished;
import com.example.sih.Registration.Login;
import com.example.sih.chatApp.User_List;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ImageView bgapp;
    Animation bganim;
    TextView GovText, NonText, TenderText, FreeText;
    Animation frombotton;
    ImageButton Gov, Non_Gov, Tenders, Free_Lancing;
    RelativeLayout menus;
    DatabaseReference reff, reff1, reff2;
    String phone, S, M, J, user_name, check, lang, X, premium_date, currentDate, remainingDays = "0", isPremium;
    Menu menu1;
    Boolean English = true;
    int i, j, y, x;
    ProgressDialog pd;
    FirebaseUser currentFirebaseUser;
    Boolean isRegistered = false;
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        SharedPreferences preferences = getSharedPreferences(S,i);
        phone = preferences.getString("Phone","");
        isPremium = preferences.getString("isPremium", "No");
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        Firebase.setAndroidContext(this);
        frombotton = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        GovText = findViewById(R.id.govText);
        NonText = findViewById(R.id.nonText);
        TenderText = findViewById(R.id.tenderText);
        FreeText = findViewById(R.id.freeText);
        bgapp = findViewById(R.id.bgapp);
        menus = findViewById(R.id.menus);
        Gov = findViewById(R.id.gov);
        Non_Gov = findViewById(R.id.non);
        Tenders = findViewById(R.id.tenders);
        Free_Lancing = findViewById(R.id.free);
        viewFlipper = findViewById(R.id.viewFlipper);

        pd = new ProgressDialog(MainActivity.this);
        if(check.equals("Hin")){
            pd.setMessage("लोड हो रहा है...");
        } else {
            pd.setMessage("Loading...");
        }
        pd.show();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view =getSupportActionBar().getCustomView();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        currentDate = df.format(c);

        ImageView crown = view.findViewById(R.id.premium);
        crown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent premiumIntent = new Intent(MainActivity.this, Premium.class);
                    startActivity(premiumIntent);

            }
        });

        ImageView dream= view.findViewById(R.id.dream_jobs);
        dream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPremium.equals("Yes")) {
                    SharedPreferences.Editor editor = getSharedPreferences(J, x).edit();
                    editor.putString("Activity", "Main");
                    editor.apply();
                    Intent dreamIntent = new Intent(MainActivity.this, Dream_jobs.class);
                    startActivity(dreamIntent);
                } else{
                    Intent dreamIntent = new Intent(MainActivity.this, Premium.class);
                    startActivity(dreamIntent);
                }
            }
        });

//        ImageView profile = view.findViewById(R.id.image_of_user);
//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
//                startActivity(profileIntent);
//
//            }
//        });

        viewFlipper.setOnTouchListener(new onFlingListener(this) {
            @Override
            public void onRightToLeft() {

                viewFlipper.setInAnimation(getApplicationContext(), R.anim.right_to_left);

                viewFlipper.showPrevious();

            }

            @Override
            public void onLeftToRight() {

                viewFlipper.setInAnimation(getApplicationContext(), R.anim.left_to_right);

                viewFlipper.showNext();

            }

            @Override
            public void onBottomToTop() {

            }

            @Override
            public void onTopToBottom() {

            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return super.onTouch(v, event);
            }

        });

        viewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        reff1 = FirebaseDatabase.getInstance().getReference().child("Company Representative Details").child(phone);
        reff1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    dataSnapshot.child("Post").getValue().toString();
                    isRegistered = true;
                } catch (Exception e){
                    isRegistered = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Something went wrong",Toast.LENGTH_LONG).show();
            }
        });

        reff2 = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    premium_date = snapshot.child("Premium Date").getValue().toString();
                    String[] date = premium_date.split("-");
                    remainingDays = calculateDays(date[0], date[1], date[2]);
                    if(remainingDays.equals("0")){
                        reff2.child("Premium Date").removeValue();
                    } else{
                        SharedPreferences.Editor editor = getSharedPreferences(S,i).edit();
                        editor.putString("remainingDays", remainingDays);
                        editor.apply();
                    }
                } catch (Exception e){
                    SharedPreferences.Editor editor = getSharedPreferences(S,i).edit();
                    editor.putString("isPremium", "No");
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(check.equals("Hin")){
            English = false;
            toHin();
        } else{
            English = true;
            toEng();
        }

        Gov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(J,x).edit();
                editor.putString("Activity", "Government");
                editor.apply();
                Intent govIntent = new Intent(MainActivity.this, Government.class);
                startActivity(govIntent);
            }
        });
        Non_Gov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(J,x).edit();
                editor.putString("Activity", "Private");
                editor.apply();
                Intent nonIntent = new Intent(MainActivity.this, Non_Government.class);
                startActivity(nonIntent);
            }
        });
        Tenders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(J,x).edit();
                editor.putString("Activity", "Tender");
                editor.apply();
                Intent tenderIntent = new Intent(MainActivity.this, com.example.sih.Jobs.Tenders.class);
                startActivity(tenderIntent);
            }
        });
        Free_Lancing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(J,x).edit();
                editor.putString("Activity", "Freelancing");
                editor.apply();
                Intent freeIntent = new Intent(MainActivity.this, com.example.sih.Jobs.Free_Lancing.class);
                startActivity(freeIntent);
            }
        });
        //An animation for 2 seconds
        bganim = AnimationUtils.loadAnimation(this, R.anim.anim);
        bgapp.animate().translationY(-3000).setDuration(800).setStartDelay(900);
        menus.startAnimation(frombotton);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //currently I am writing nothing here, you can write whatever you want but just inform me.
                            return;
                        } else {
                            //token will be saved in database
                            String token = task.getResult().getToken();
                            String user_token = getString(R.string.msg_token_fmt, token);
                            Firebase reference = new Firebase("https://smart-e60d6.firebaseio.com/Users");
                            reference.child(phone).child("Message Token").setValue(user_token);
                        }
                    }
                });
                pd.dismiss();
            }
        }, 3000);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String uname = dataSnapshot.child("Username").getValue().toString();
                    user_name = decryptUsername(uname).toString();
                    SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                    editor.putString("UName", user_name);
                    editor.apply();
                    SharedPreferences.Editor editor2 = getSharedPreferences(X, y).edit();
                    editor2.putString("isDeleted", "No");
                    editor2.apply();
                } catch(Exception e){
                    if(check.equals("Eng") && English){
                        Toast.makeText(MainActivity.this, "Your account has been deleted", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.account_deleted1), Toast.LENGTH_SHORT).show();
                    }
                    SharedPreferences.Editor editor = getSharedPreferences(X, y).edit();
                    editor.putString("isDeleted", "Yes");
                    editor.apply();
                    Intent logIntent = new Intent(MainActivity.this, Login.class);
                    startActivity(logIntent);
                    finishAffinity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals("Hin")) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(MainActivity.this, "There is some error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu1 = menu;
        getMenuInflater().inflate(R.menu.option_menu,menu);
        if(check.equals("Hin")){
            optionHin();
        }else {
            optionEng();
        }
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.switch1:
                if(English){
                    toHin();
                    optionHin();
                    English = false;
                }else{
                    toEng();
                    optionEng();
                    English = true;
                }
                return true;
            case R.id.logout: {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                editor.putString("Status", "Null");
                editor.apply();
                finishAffinity();
                break;
            }
            case R.id.rate_us:
                Intent rateIntent = new Intent(MainActivity.this, Rating.class);
                startActivity(rateIntent);
                return true;
            case R.id.contact_us:
                String recipient = "firstloveyourself1999@gmail.com";
                Intent intent4 = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                intent4.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
                startActivity(intent4);
                return true;

            case R.id.create_your_job:
                if (!isRegistered) {
                    Intent jCreateIntent = new Intent(MainActivity.this, CreateYourJob.class);
                    startActivity(jCreateIntent);
                }
                else{
                    Intent viewIntent = new Intent(MainActivity.this, jobsPublished.class);
                    startActivity(viewIntent);
                }
                return true;

            case R.id.topJobs:
                Intent topJobsIntent = new Intent(MainActivity.this, topJobsFragment.class);
                startActivity(topJobsIntent);
                return true;

            case R.id.chat:
                Intent chatIntent = new Intent(MainActivity.this, User_List.class);
                startActivity(chatIntent);
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
    public void toHin(){
        GovText.setText(R.string.government_jobs1);
        NonText.setText(R.string.non_government_jobs1);
        TenderText.setText(R.string.tenders1);
        FreeText.setText(R.string.freelancing1);
        lang = "Hin";
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
    }
    public void toEng(){
        GovText.setText("Government Jobs");
        NonText.setText("Non-Government Jobs");
        TenderText.setText("Tenders");
        FreeText.setText("Freelancing");
        lang = "Eng";
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
    }

    private void setOptionTitle(int id, String title)
    {
        MenuItem item = menu1.findItem(id);
        item.setTitle(title);
    }

    public void optionHin(){
        setOptionTitle(R.id.switch1, getResources().getString(R.string.switch1));
        setOptionTitle(R.id.rate_us, getResources().getString(R.string.rate1));
        setOptionTitle(R.id.logout, getResources().getString(R.string.logout1));
        setOptionTitle(R.id.contact_us, getResources().getString(R.string.contact_us1));
        setOptionTitle(R.id.create_your_job, getResources().getString(R.string.publish_your_job1));
        setOptionTitle(R.id.topJobs, getResources().getString(R.string.top_jobs1));
        setOptionTitle(R.id.chat, "बातचीत");
    }
    public void optionEng(){
        setOptionTitle(R.id.switch1, "Change Language");
        setOptionTitle(R.id.rate_us, "Rate Us");
        setOptionTitle(R.id.logout, "Logout");
        setOptionTitle(R.id.contact_us, "Contact Us");
        setOptionTitle(R.id.create_your_job, "Publish Your Job");
        setOptionTitle(R.id.topJobs, "Top Jobs");
        setOptionTitle(R.id.chat, "Chat");
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        if(check.equals("Hin")){
            English = false;
            toHin();
            try{
                optionHin();
            } catch(Exception e){
            }
        } else{
            English = true;
            toEng();
            try{
                optionEng();
            } catch(Exception e){

            }
        }
    }
    public StringBuilder decryptUsername(String uname) {
        int pllen;
        StringBuilder sb = new StringBuilder();
        int ciplen = uname.length();

        String temp = Character.toString(uname.charAt(ciplen - 2));
        if (temp.matches("[a-z]+")) {
            pllen = Character.getNumericValue(uname.charAt(ciplen - 1));
            pllen -= 2;
        } else {
            String templen = uname.charAt(ciplen - 2) + Character.toString(uname.charAt(ciplen - 1));
            pllen = Integer.parseInt(templen);
            pllen -= 2;
        }
        String[] separated = uname.split("[a-zA-Z]");
        for (int i = 0; i < pllen; i++) {
            String splitted = separated[i];
            int split = Integer.parseInt(splitted);
            split = split + pllen + (2 * i);
            char pln = (char) split;
            sb.append(pln);
        }
        return sb;
    }

    public String calculateDays(String day, String month, String year){
        String current[] = currentDate.split("-");
        String remaining;
        if(!month.equals("Feb")){
            if(month.equals("Apr") || month.equals("Jun") || month.equals("Sep") || month.equals("Nov")){
                if(Integer.parseInt(current[0]) > Integer.parseInt(day)){
                    remaining = Integer.toString(30 - (Integer.parseInt(current[0]) - Integer.parseInt(day)));
                } else if(Integer.parseInt(current[0]) < Integer.parseInt(day)){
                    remaining = Integer.toString(30 - ((30-Integer.parseInt(day)) + Integer.parseInt(current[0])));
                } else if(current[1].equals(month) && current[0].equals(day)){
                    remaining = "30";
                } else{
                    remaining = "0";
                }
            } else {
                if (Integer.parseInt(current[0]) > Integer.parseInt(day) && month.equals(current[1])) {
                    remaining = Integer.toString(30 - (Integer.parseInt(current[0]) - Integer.parseInt(day)));
                } else if (Integer.parseInt(current[0]) < Integer.parseInt(day) && !(current[1].equals("Mar"))) {
                    remaining = Integer.toString(30 - ((31 - Integer.parseInt(day)) + Integer.parseInt(current[0])));
                } else if (Integer.parseInt(current[0]) < Integer.parseInt(day) && !(Integer.parseInt(year) % 4 == 0) && (current[1].equals("Mar"))) {
                    if (day.equals("30") && month.equals("Jan")) {
                        remaining = "1";
                    } else {
                        remaining = "2";
                    }

                }
                else if (Integer.parseInt(current[0]) < Integer.parseInt(day) && (Integer.parseInt(year) % 4 == 0) && (current[1].equals("Mar"))) {
                    if (day.equals("30") && month.equals("Jan")) {
                        remaining = "0";
                    } else {
                        remaining = "1";
                    }
                }else {
                    remaining = "30";
                }
            }
        } else{
            if(Integer.parseInt(year)%4 != 0 && Integer.parseInt(year)%400 != 0){
                if(Integer.parseInt(current[0]) > Integer.parseInt(day)){
                    remaining = Integer.toString(30 - (Integer.parseInt(current[0]) - Integer.parseInt(day)));
                } else if(Integer.parseInt(current[0]) < Integer.parseInt(day)){
                    remaining = Integer.toString(30 - ((29-Integer.parseInt(day)) + Integer.parseInt(current[0])));
                } else if(current[1].equals(month) && current[0].equals(day)){
                    remaining = "30";
                } else if(!(current[1].equals(month)) && current[0].equals(day)){
                    remaining = "1";
                } else {
                    remaining = "0";
                }
            } else{
                if(Integer.parseInt(current[0]) > Integer.parseInt(day) && month.equals(current[1])){
                    remaining = Integer.toString(30 - (Integer.parseInt(current[0]) - Integer.parseInt(day)));
                } else if(Integer.parseInt(current[0]) < Integer.parseInt(day)){
                    remaining = Integer.toString(30 - ((28-Integer.parseInt(day)) + Integer.parseInt(current[0])));
                } else if(current[1].equals(month) && current[0].equals(day)){
                    remaining = "30";
                } else if(!(current[1].equals(month)) && current[0].equals(day)){
                    remaining = "2";
                }else if(Integer.parseInt(current[0]) > Integer.parseInt(day) && month.equals(current[1])){
                    remaining = "1";
                }else {
                    remaining = "0";
                }
            }
        }
        return remaining;
    }
}