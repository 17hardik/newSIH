package com.example.sih.Atmanirbhar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sih.Jobs.Free_Lancing;
import com.example.sih.Jobs.Government;
import com.example.sih.Jobs.Non_Government;
import com.example.sih.Jobs.Tenders;
import com.example.sih.Profile.Profile;
import com.example.sih.Profile.Rating;
import com.example.sih.R;
import com.example.sih.Registration.Login;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserPublished extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView uphone, uname;
    Boolean English = true;
    String lang, M, check, S, phone, u_name, path;
    int j, i;
    DrawerLayout drawer;
    ImageView profile;
    CardView cd2, cd3, cd4, cd5;
    NavigationView navigationView;
    TextView JobTitle1, JobTitle2, JobTitle3, JobTitle4, JobTitle5;
    TextView JobDesc1, JobDesc2, JobDesc3, JobDesc4, JobDesc5;
    TextView Days1, Days2, Days3, Days4, Days5;
    StorageReference mStorageReference;
    ActionBarDrawerToggle t;
    Menu menu1, menu2;
    MenuItem Gov, Non_Gov, Tender, Free_Lancing;
    DatabaseReference reff, reff1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        SharedPreferences preferences = getSharedPreferences(S,i);
        phone= preferences.getString("Phone","");
        path = preferences.getString("path", "");
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        setContentView(R.layout.activity_user_published);
        drawer = findViewById(R.id.draw_layout);

        JobTitle1 = findViewById(R.id.job_title1);
        JobTitle2 = findViewById(R.id.job_title2);
        JobTitle3 = findViewById(R.id.job_title3);
        JobTitle4 = findViewById(R.id.job_title4);
        JobTitle5 = findViewById(R.id.job_title5);
        JobDesc1 = findViewById(R.id.job_description1);
        JobDesc2 = findViewById(R.id.job_description2);
        JobDesc3 = findViewById(R.id.job_description3);
        JobDesc4 = findViewById(R.id.job_description4);
        JobDesc5 = findViewById(R.id.job_description5);
        Days1 = findViewById(R.id.days1);
        Days2 = findViewById(R.id.days2);
        Days3 = findViewById(R.id.days3);
        Days4 = findViewById(R.id.days4);
        Days5 = findViewById(R.id.days5);
        cd2 = findViewById(R.id.job_card2);
        cd3 = findViewById(R.id.job_card3);
        cd4 = findViewById(R.id.job_card4);
        cd5 = findViewById(R.id.job_card5);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(this);
        t = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(t);
        t.syncState();
        menu2 = navigationView.getMenu();
        Gov = menu2.findItem(R.id.government);
        Non_Gov = menu2.findItem(R.id.non_government);
        Tender = menu2.findItem(R.id.tenders);
        Free_Lancing = menu2.findItem(R.id.free_lancing);
        uname = navigationView.getHeaderView(0).findViewById(R.id.name_of_user);
        uphone = navigationView.getHeaderView(0).findViewById(R.id.phone_of_user);
        profile = navigationView.getHeaderView(0).findViewById(R.id.image_of_user);
        loadImageFromStorage(path);
        if(check.equals("Eng")){
            actionBar.setTitle("Your Posts");
        } else {
            actionBar.setTitle("आपके पोस्ट");
        }

        reff1 = FirebaseDatabase.getInstance().getReference().child("Atmanirbhar");
        reff1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    JobTitle1.setText(snapshot.child(phone).child("1").child("JobName").getValue().toString());
                    JobDesc1.setText(snapshot.child(phone).child("1").child("Description").getValue().toString());
                    Days1.setText(snapshot.child(phone).child("1").child("Location").getValue().toString());
                } catch(Exception e){
                    if(check.equals("Eng")){
                        Toast.makeText(UserPublished.this, "You have not published any job yet.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserPublished.this, "आपने अभी तक कोई नौकरी प्रकाशित नहीं की है", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reff1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    JobTitle2.setText(snapshot.child(phone).child("2").child("JobName").getValue().toString());
                    JobDesc2.setText(snapshot.child(phone).child("2").child("Description").getValue().toString());
                    Days2.setText(snapshot.child(phone).child("2").child("Location").getValue().toString());
                    cd2.setVisibility(View.VISIBLE);
                } catch(Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reff1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    JobTitle3.setText(snapshot.child(phone).child("3").child("JobName").getValue().toString());
                    JobDesc3.setText(snapshot.child(phone).child("3").child("Description").getValue().toString());
                    Days3.setText(snapshot.child(phone).child("3").child("Location").getValue().toString());
                    cd3.setVisibility(View.VISIBLE);
                } catch(Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reff1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    JobTitle4.setText(snapshot.child(phone).child("4").child("JobName").getValue().toString());
                    JobDesc4.setText(snapshot.child(phone).child("4").child("Description").getValue().toString());
                    Days4.setText(snapshot.child(phone).child("4").child("Location").getValue().toString());
                    cd4.setVisibility(View.VISIBLE);
                } catch(Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reff1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    JobTitle5.setText(snapshot.child(phone).child("5").child("JobName").getValue().toString());
                    JobDesc5.setText(snapshot.child(phone).child("5").child("Description").getValue().toString());
                    Days5.setText(snapshot.child(phone).child("5").child("Location").getValue().toString());
                    cd5.setVisibility(View.VISIBLE);
                } catch(Exception e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(UserPublished.this, Profile.class);
                startActivity(profileIntent);
            }
        });
        uphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(UserPublished.this, Profile.class);
                startActivity(profileIntent);
            }
        });
        uname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(UserPublished.this, Profile.class);
                startActivity(profileIntent);
            }
        });

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                u_name = dataSnapshot.child("Username").getValue().toString();
                phone = dataSnapshot.child("Phone").getValue().toString();
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
                                    profile.setMinimumHeight(dm.heightPixels);
                                    profile.setMinimumWidth(dm.widthPixels);
                                    profile.setImageBitmap(bm);
                                    path = saveToInternalStorage(bm);
                                    SharedPreferences.Editor editor1 = getSharedPreferences(S,i).edit();
                                    editor1.putString("path", path);
                                    editor1.apply();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });
                } catch(Exception e){

                }
                uphone.setText(phone);
                uname.setText(decryptUsername(u_name));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals("Hin")) {
                    Toast.makeText(UserPublished.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(UserPublished.this, "There is some error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(check.equals("Hin")){
            NavHin();
            toHin();
        } else{
            NavEng();
            toEng();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.non_government:
                Intent intent1 = new Intent(UserPublished.this, Non_Government.class);
                startActivity(intent1);
                break;
            case R.id.free_lancing:
                Intent intent = new Intent(UserPublished.this, com.example.sih.Jobs.Free_Lancing.class);
                startActivity(intent);
                break;
            case R.id.government:
                Intent intent2 = new Intent(UserPublished.this, Government.class);
                startActivity(intent2);
                break;
            case R.id.tenders:
                Intent intent5 = new Intent(UserPublished.this, Tenders.class);
                startActivity(intent5);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
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
                if (English) {
                    toHin();
                    NavHin();
                    optionHin();
                } else {
                    toEng();
                    NavEng();
                    optionEng();
                }
                return true;
            case R.id.logout: {
                Intent intent = new Intent(UserPublished.this, Login.class);
                startActivity(intent);
                SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                editor.putString("Status", "Null");
                editor.apply();
                finishAffinity();
                break;
            }
            case R.id.rate_us:
                Intent rateIntent = new Intent(UserPublished.this, Rating.class);
                startActivity(rateIntent);
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    public void toEng(){
        getSupportActionBar().setTitle("Your Posts");
        English = true;
        lang = "Eng";
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
    }

    public void toHin(){
        getSupportActionBar().setTitle("आपके पोस्ट");
        English = false;
        lang = "Hin";
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
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
    public void NavHin(){
        Gov.setTitle("                  सरकारी नौकरियों");
        Non_Gov.setTitle("                  प्राइवेट नौकरी");
        Tender.setTitle("                  निविदाएं");
        Free_Lancing.setTitle("                  फ़्रीलांसिंग");
    }
    public void NavEng(){
        Gov.setTitle("                  Government Jobs");
        Non_Gov.setTitle("                  Private Jobs");
        Tender.setTitle("                  Tenders");
        Free_Lancing.setTitle("                  Freelancing");
    }
    private void setOptionTitle(int id, String title)
    {
        MenuItem item = menu1.findItem(id);
        item.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer((GravityCompat.START));
        } else {
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        if(check.equals("Hin")){
            English = false;
            NavHin();
            toHin();
            try{
                optionHin();
            } catch(Exception e){
            }
        } else{
            English = true;
            NavEng();
            toEng();
            try{
                optionEng();
            } catch(Exception e){

            }
        }
    }
    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            profile.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("profile_picture", Context.MODE_PRIVATE);
        File mypath = new File(directory,"profile.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
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

}