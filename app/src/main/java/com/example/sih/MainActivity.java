package com.example.sih;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sih.Jobs.Dream_jobs;
import com.example.sih.Jobs.Government;
import com.example.sih.Jobs.Non_Government;
import com.example.sih.Jobs.Tenders;
import com.example.sih.Jobs.topJobsFragment;
import com.example.sih.Profile.Premium;
import com.example.sih.Profile.Profile;
import com.example.sih.Profile.Rating;
import com.example.sih.PublishJob.CreateYourJob;
import com.example.sih.PublishJob.jobsPublished;
import com.example.sih.Registration.Login;
import com.example.sih.chatApp.User_List;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.sih.model.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView bgapp;
    Animation bganim;
    TextView GovText, NonText, TenderText, FreeText, uphone, uname, Premium, Days;
    ImageView Profile, Crown;
    DrawerLayout drawer;
    Animation frombotton;
    ImageButton Gov, Non_Gov, Tenders, Free_Lancing;
    RelativeLayout menus;
    DatabaseReference reff, reff1, reff2, reff3;
    String phone, S, M, J, user_name, check, lang, X, premium_date, currentDate, remainingDays = "0", isPremium, u_name, days, path;
    NavigationView navigationView;
    StorageReference mStorageReference;
    ActionBarDrawerToggle t;
    Menu menu1, menu2;
    MenuItem Government, Non_Government, Tender, FreeLancing, GetPremium, chat, topJobs, publishJob, Jobs, Features, Connection, Top_Jobs, Publish;
    int i, j, y, x;
    ProgressDialog pd;
    FirebaseUser currentFirebaseUser;
    Boolean isRegistered = false, English = true;
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString(EnglishConstants.LANGUAGE , EnglishConstants.LANG_ENG);
        SharedPreferences preferences = getSharedPreferences(S,i);
        phone = preferences.getString(EnglishConstants.PHONE, EnglishConstants.EMPTY_STRING);
        isPremium = preferences.getString(EnglishConstants.IS_PREMIUM_OPTION, EnglishConstants.NO);
        days = preferences.getString(EnglishConstants.REMAINING_DAYS, EnglishConstants.STRING_ZERO);
        path = preferences.getString(EnglishConstants.PATH, EnglishConstants.EMPTY_STRING);
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
        if(check.equals(HINDI_OPTION)){
            pd.setMessage(HindiConstants.LOADING);
        } else {
            pd.setMessage(EnglishConstants.LOADING);
        }
        pd.show();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view =getSupportActionBar().getCustomView();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(EnglishConstants.DATE_FORMAT, Locale.getDefault());
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

        ImageView profile = view.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
                startActivity(profileIntent);

            }
        });

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

        if(check.equals(EnglishConstants.HINDI_OPTION)){
            English = false;
            toHin();
        } else{
            English = true;
            toEng();
        }
        drawer = findViewById(R.id.draw_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(this);
        t = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(t);
        t.syncState();
        menu2 = navigationView.getMenu();
        Government = menu2.findItem(R.id.government);
        Non_Government = menu2.findItem(R.id.non_government);
        Tender = menu2.findItem(R.id.tenders);
        FreeLancing = menu2.findItem(R.id.free_lancing);
        GetPremium = menu2.findItem(R.id.premium);
        chat = menu2.findItem(R.id.chat);
        topJobs = menu2.findItem(R.id.topJobs);
        publishJob = menu2.findItem(R.id.publish);
        Jobs = menu2.findItem(R.id.title1);
        Features = menu2.findItem(R.id.title2);
        Top_Jobs = menu2.findItem(R.id.topJobs);
        Connection = menu2.findItem(R.id.chat);
        Publish = menu2.findItem(R.id.publish);

        uname = navigationView.getHeaderView(0).findViewById(R.id.name_of_user);
        uphone = navigationView.getHeaderView(0).findViewById(R.id.phone_of_user);
        Profile = navigationView.getHeaderView(0).findViewById(R.id.image_of_user);
        Premium = navigationView.getHeaderView(0).findViewById(R.id.premium);
        Days = navigationView.getHeaderView(0).findViewById(R.id.days);
        Crown = navigationView.getHeaderView(0).findViewById(R.id.crownimage);
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

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
                startActivity(profileIntent);
            }
        });
        uphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
                startActivity(profileIntent);
            }
        });
        uname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
                startActivity(profileIntent);
            }
        });
        reff3 = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        reff3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                u_name = dataSnapshot.child("Username").getValue().toString();
                phone = dataSnapshot.child("Phone").getValue().toString();
                if (isPremium.equals("Yes")) {
                    if (check.equals("Hin")) {
                        Premium.setText("प्रीमियम");
                    }
                    Premium.setVisibility(View.VISIBLE);
                    Crown.setVisibility(View.VISIBLE);
                    if (days.equals("1")) {
                        if (check.equals("Hin")) {
                            Days.setText(days + " दिन शेष");
                        } else {
                            Days.setText(days + " day remaining");
                        }
                    } else {
                        if (check.equals("Hin")) {
                            Days.setText(days + " दिन शेष");
                        } else {
                            Days.setText(days + " days remaining");
                        }
                    }
                    Days.setVisibility(View.VISIBLE);
                }
                String username = decryptUsername(u_name).toString();
                mStorageReference = FirebaseStorage.getInstance().getReference().child(phone).child("Profile Picture");
                try {
                    final long ONE_MEGABYTE = 1024 * 1024;
                    mStorageReference.getBytes(ONE_MEGABYTE)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    DisplayMetrics dm = new DisplayMetrics();
                                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                                    Profile.setMinimumHeight(dm.heightPixels);
                                    Profile.setMinimumWidth(dm.widthPixels);
                                    Profile.setImageBitmap(bm);
                                    // path = saveToInternalStorage(bm);
                                    SharedPreferences.Editor editor1 = getSharedPreferences(S, i).edit();
                                    editor1.putString("path", path);
                                    editor1.apply();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });
                } catch (Exception e) {

                }
                uphone.setText(phone);
                uname.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (check.equals("Hin")) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "There is some error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (check.equals("Hin")) {
            NavHin();
            toHin();
        } else {
            NavEng();
            toEng();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.non_government:
                SharedPreferences.Editor editor = getSharedPreferences(J,x).edit();
                editor.putString("Activity", "Private");
                editor.apply();
                Intent intent1 = new Intent(MainActivity.this, Non_Government.class);
                startActivity(intent1);
                break;
            case R.id.free_lancing:
                SharedPreferences.Editor editor1 = getSharedPreferences(J,x).edit();
                editor1.putString("Activity", "Freelancing");
                editor1.apply();
                Intent intent = new Intent(MainActivity.this, com.example.sih.Jobs.Free_Lancing.class);
                startActivity(intent);
                break;
            case R.id.tenders:
                SharedPreferences.Editor editor2 = getSharedPreferences(J,x).edit();
                editor2.putString("Activity", "Tender");
                editor2.apply();
                Intent intent5 = new Intent(MainActivity.this, com.example.sih.Jobs.Tenders.class);
                startActivity(intent5);
                break;
            case R.id.premium:
                Intent intent2 = new Intent(MainActivity.this, com.example.sih.Profile.Premium.class);
                startActivity(intent2);
                break;
            case R.id.chat:
                Intent intent6 = new Intent(MainActivity.this, com.example.sih.chatApp.User_List.class);
                startActivity(intent6);
                break;
            case R.id.publish:
                if (!isRegistered) {
                    Intent intent7 = new Intent(MainActivity.this, com.example.sih.PublishJob.CreateYourJob.class);
                    startActivity(intent7);
                }
                else{
                    Intent intent7 = new Intent(MainActivity.this, com.example.sih.PublishJob.jobsPublished.class);
                    startActivity(intent7);
                }
                break;
            case R.id.topJobs:
                Intent intent8 = new Intent(MainActivity.this, com.example.sih.Jobs.topJobsFragment.class);
                startActivity(intent8);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        if(t.onOptionsItemSelected(menuItem))
            return true;
        switch (menuItem.getItemId()) {
            case R.id.switch1:
                if(English){
                    toHin();
                    optionHin();
                    NavHin();
                    English = false;
                }else{
                    toEng();
                    NavEng();
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

    public void NavHin(){
        Government.setTitle("                  सरकारी नौकरियों");
        Non_Government.setTitle("                  गैर सरकारी नौकरी");
        Tender.setTitle("                  निविदाएं");
        FreeLancing.setTitle("                  फ़्रीलांसिंग");
        GetPremium.setTitle("                  प्रीमियम प्राप्त करें");
        Publish.setTitle("                  अपनी नौकरी प्रकाशित करें");
        Top_Jobs.setTitle("                  शीर्ष नौकरियां");
        Connection.setTitle("                  अपने कनेक्शन बनाएँ");
        Premium.setText("प्रीमियम");
        Days.setText(days + " दिन शेष");
        Jobs.setTitle("           नौकरी क्षेत्र");
        Features.setTitle("           अधिक सुविधाएं");
    }
    public void NavEng(){
        Government.setTitle("                  Government Jobs");
        Non_Government.setTitle("                  Non-Government Jobs");
        Tender.setTitle("                  Tenders");
        FreeLancing.setTitle("                  Freelancing");
        GetPremium.setTitle("                  Get Premium");
        Publish.setTitle("                  Publish Your Job");
        Top_Jobs.setTitle("                  Top Jobs");
        Connection.setTitle("                  Build Your Connections");
        Premium.setText("Premium");
        if(days.equals("1")){
            Days.setText(days + " day remaining");
        } else {
            Days.setText(days + " days remaining");

        }
        Jobs.setTitle("           Job Sectors");
        Features.setTitle("           More Features");
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
    }
    public void optionEng(){
        setOptionTitle(R.id.switch1, "Change Language");
        setOptionTitle(R.id.rate_us, "Rate Us");
        setOptionTitle(R.id.logout, "Logout");
        setOptionTitle(R.id.contact_us, "Contact Us");
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