package com.example.sih;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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

import java.util.ArrayList;

public class Free_Lancing extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView uphone, uname, Premium, Days, jobType;
    Boolean English = true;
    String lang, M, J, check, S, phone, u_name, path, days, isPremium,activity, domain;
    int j, i, x;
    DrawerLayout drawer;
    ImageView profile, crown;
    NavigationView navigationView;
    StorageReference mStorageReference;
    ActionBarDrawerToggle t;
    Menu menu1, menu2;
    MenuItem Gov, Non_Gov, Tender, Free_Lancing, GetPremium;
    DatabaseReference reff, reff1, reff2, reff3, reff4, reff5, reff6, reff7, reff8, reff9, reff10, reff11, reff12;
    RecyclerView freelance;
    ArrayList<data_in_cardview> details;
    gov_adapter govAdapter;
    ProgressDialog pd;
    AdView mAdView;
    int size, k;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(S, i);
        phone = preferences.getString("Phone", "");
        path = preferences.getString("path", "");
        isPremium = preferences.getString("isPremium", "No");
        days = preferences.getString("remainingDays", "0");
        SharedPreferences preferences1 = getSharedPreferences(M, j);
        check = preferences1.getString("Lang", "Eng");
        SharedPreferences preferences2 = getSharedPreferences(J,x);
        activity = preferences2.getString("Activity","");
        domain = preferences.getString("Domain", "");
        setContentView(R.layout.activity_free__lancing);
        freelance = findViewById(R.id.freelance);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_2);
        View view =getSupportActionBar().getCustomView();

        freelance.setLayoutManager(new LinearLayoutManager(this));
        details = new ArrayList<>();

        jobType = view.findViewById(R.id.jobsType);
        if (activity.equals("Government")){
            if(check.equals("Eng")) {
                jobType.setText("Government Jobs");
            } else{
                jobType.setText(R.string.government_jobs1);
            }
        }

        else if (activity.equals("Private")){
            if(check.equals("Eng")) {
                jobType.setText("Private Jobs");
            } else{
                jobType.setText(R.string.non_government_jobs1);
            }
        }

        else if (activity.equals("Freelancing")){
            if(check.equals("Eng")) {
                jobType.setText("Freelancing");
            } else{
                jobType.setText(R.string.freelancing1);
            }
        }

        else {
            if(check.equals("Eng")) {
                jobType.setText("Tenders");
            } else{
                jobType.setText(R.string.tenders1);
            }
        }
        final SearchView mySearchView = view.findViewById(R.id.mySearchView);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }
                            @Override
                            public boolean onQueryTextChange(String newText) {
                                govAdapter.getFilter().filter(newText);
                                return false;
                            }
                        });
                    }
                },
                3000
        );

        pd = new ProgressDialog(Free_Lancing.this);

        if (check.equals("Eng")) {
            pd.setMessage("Fetching data");
        } else {
            pd.setMessage("डेटा लाया जा रहा है");
        }

        pd.show();

        if (domain.equals("Science")) {
            reff = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Science and Technology").child("Freelancing");

            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    size = (int) dataSnapshot.getChildrenCount();

                    for (int l = 0; l < size; l++) {

                        String i = Integer.toString(l);
                        reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Science and Technology").child("Freelancing").child(i);
                        reff1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                data_in_cardview d = snapshot.getValue(data_in_cardview.class);
                                details.add(d);
                                govAdapter = new gov_adapter(Free_Lancing.this, details);
                                freelance.setAdapter(govAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (domain.equals("Business")) {

            reff2 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Business, Management and Administration").child("Freelancing");
            reff2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    size = (int) dataSnapshot.getChildrenCount();

                    for (int l = 0; l < size; l++) {

                        String i = Integer.toString(l);
                        reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Business, Management and Administration").child("Freelancing").child(i);
                        reff1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                data_in_cardview d = snapshot.getValue(data_in_cardview.class);
                                details.add(d);
                                govAdapter = new gov_adapter(Free_Lancing.this, details);
                                freelance.setAdapter(govAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });

        }
        if (domain.equals("Farming")) {
            reff3 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Farming, Fishing and Forestry").child("Freelancing");
            reff3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    size = (int) dataSnapshot.getChildrenCount();

                    for (int l = 0; l < size; l++) {

                        String i = Integer.toString(l);
                        reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Farming, Fishing and Forestry").child("Freelancing").child(i);
                        reff1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                data_in_cardview d = snapshot.getValue(data_in_cardview.class);
                                details.add(d);
                                govAdapter = new gov_adapter(Free_Lancing.this, details);
                                freelance.setAdapter(govAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (domain.equals("Community")) {
            reff4 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Community and Social Services").child("Freelancing");
            reff4.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    size = (int) dataSnapshot.getChildrenCount();

                    for (int l = 0; l < size; l++) {

                        String i = Integer.toString(l);
                        reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Community and Social Services").child("Freelancing").child(i);
                        reff1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                data_in_cardview d = snapshot.getValue(data_in_cardview.class);
                                details.add(d);
                                govAdapter = new gov_adapter(Free_Lancing.this, details);
                                freelance.setAdapter(govAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (domain.equals("Labors")) {
            reff5 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Labour").child("Freelancing");
            reff5.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    size = (int) dataSnapshot.getChildrenCount();

                    for (int l = 0; l < size; l++) {

                        String i = Integer.toString(l);
                        reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Labour").child("Freelancing").child(i);
                        reff1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                data_in_cardview d = snapshot.getValue(data_in_cardview.class);
                                details.add(d);
                                govAdapter = new gov_adapter(Free_Lancing.this, details);
                                freelance.setAdapter(govAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (domain.equals("Health")) {

            reff6 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Healthcare and Medical").child("Freelancing");
            reff6.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    size = (int) dataSnapshot.getChildrenCount();

                    for (int l = 0; l < size; l++) {

                        String i = Integer.toString(l);
                        reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Healthcare and Medical").child("Freelancing").child(i);
                        reff1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                data_in_cardview d = snapshot.getValue(data_in_cardview.class);
                                details.add(d);
                                govAdapter = new gov_adapter(Free_Lancing.this, details);
                                freelance.setAdapter(govAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });

        }
        if (domain.equals("Communications")) {

            reff7 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Communication").child("Freelancing");
            reff7.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    size = (int) dataSnapshot.getChildrenCount();

                    for (int l = 0; l < size; l++) {

                        String i = Integer.toString(l);
                        reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Communication").child("Freelancing").child(i);
                        reff1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                data_in_cardview d = snapshot.getValue(data_in_cardview.class);
                                details.add(d);
                                govAdapter = new gov_adapter(Free_Lancing.this, details);
                                freelance.setAdapter(govAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });

        }
        if (domain.equals("Arts")) {

            reff8 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Arts, Culture and Entertainment").child("Freelancing");
            reff8.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    size = (int) dataSnapshot.getChildrenCount();

                    for (int l = 0; l < size; l++) {

                        String i = Integer.toString(l);
                        reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Arts, Culture and Entertainment").child("Freelancing").child(i);
                        reff1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                data_in_cardview d = snapshot.getValue(data_in_cardview.class);
                                details.add(d);
                                govAdapter = new gov_adapter(Free_Lancing.this, details);
                                freelance.setAdapter(govAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });

        }
        if (domain.equals("Education")) {

            reff9 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Education").child("Freelancing");
            reff9.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    size = (int) dataSnapshot.getChildrenCount();

                    for (int l = 0; l < size; l++) {

                        String i = Integer.toString(l);
                        reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Education").child("Freelancing").child(i);
                        reff1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                data_in_cardview d = snapshot.getValue(data_in_cardview.class);
                                details.add(d);
                                govAdapter = new gov_adapter(Free_Lancing.this, details);
                                freelance.setAdapter(govAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });

        }
        if (domain.equals("Installation")) {

            reff10 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Installation, Repair and Maintenance").child("Freelancing");
            reff10.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    size = (int) dataSnapshot.getChildrenCount();

                    for (int l = 0; l < size; l++) {

                        String i = Integer.toString(l);
                        reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Installation, Repair and Maintenance").child("Freelancing").child(i);
                        reff1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                data_in_cardview d = snapshot.getValue(data_in_cardview.class);
                                details.add(d);
                                govAdapter = new gov_adapter(Free_Lancing.this, details);
                                freelance.setAdapter(govAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });

        }

        reff11 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("All Jobs").child("Freelancing");

        reff11.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                size = (int) dataSnapshot.getChildrenCount();

                for (int k = 0; k < size; k++) {

                    String i = Integer.toString(k);
                    reff12 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("All Jobs").child("Freelancing").child(i);
                    reff12.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String subDomain = snapshot.child("sub_domain").getValue().toString();

                            data_in_cardview d = snapshot.getValue(data_in_cardview.class);
                            if (!(subDomain.equals(domain))) {
                                details.add(d);
                                govAdapter = new gov_adapter(Free_Lancing.this, details);
                                freelance.setAdapter(govAdapter);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            if(check.equals("Eng")) {
                                Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Free_Lancing.this, "कृपया अपने इंटरनेट कनेक्शन की जाँच करें", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals("Eng")) {
                    Toast.makeText(Free_Lancing.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Free_Lancing.this, "कृपया अपने इंटरनेट कनेक्शन की जाँच करें", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
            }
        }, 3000);
        drawer = findViewById(R.id.draw_layout);
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
        GetPremium = menu2.findItem(R.id.premium);
        uname = navigationView.getHeaderView(0).findViewById(R.id.name_of_user);
        uphone = navigationView.getHeaderView(0).findViewById(R.id.phone_of_user);
        profile = navigationView.getHeaderView(0).findViewById(R.id.image_of_user);
        Premium = navigationView.getHeaderView(0).findViewById(R.id.premium);
        Days = navigationView.getHeaderView(0).findViewById(R.id.days);
        crown = navigationView.getHeaderView(0).findViewById(R.id.crownimage);

        if (isPremium.equals("No")) {
            showAd();
        }
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(Free_Lancing.this, Profile.class);
                startActivity(profileIntent);
            }
        });
        uphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(Free_Lancing.this, Profile.class);
                startActivity(profileIntent);
            }
        });
        uname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(Free_Lancing.this, Profile.class);
                startActivity(profileIntent);
            }
        });
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                u_name = dataSnapshot.child("Username").getValue().toString();
                phone = dataSnapshot.child("Phone").getValue().toString();
                if (isPremium.equals("Yes")) {
                    if (check.equals("Hin")) {
                        Premium.setText("प्रीमियम");
                    }
                    Premium.setVisibility(View.VISIBLE);
                    crown.setVisibility(View.VISIBLE);
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
                                    profile.setMinimumHeight(dm.heightPixels);
                                    profile.setMinimumWidth(dm.widthPixels);
                                    profile.setImageBitmap(bm);
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
                    Toast.makeText(Free_Lancing.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Free_Lancing.this, "There is some error", Toast.LENGTH_SHORT).show();
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
                Intent intent1 = new Intent(Free_Lancing.this, Non_Government.class);
                startActivity(intent1);
                break;
            case R.id.government:
                SharedPreferences.Editor editor1 = getSharedPreferences(J,x).edit();
                editor1.putString("Activity", "Government");
                editor1.apply();
                Intent intent = new Intent(Free_Lancing.this, Government.class);
                startActivity(intent);
                break;
            case R.id.tenders:
                SharedPreferences.Editor editor2 = getSharedPreferences(J,x).edit();
                editor2.putString("Activity", "Tender");
                editor2.apply();
                Intent intent5 = new Intent(Free_Lancing.this, Tenders.class);
                startActivity(intent5);
                break;
            case R.id.premium:
                Intent intent2 = new Intent(Free_Lancing.this, Testing.class);
                startActivity(intent2);
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
        }
        else{
            optionEng();
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if(t.onOptionsItemSelected(menuItem))
            return true;
        switch (menuItem.getItemId()) {
            case R.id.switch1:
                if(check.equals("Eng")) {
                    pd.setMessage("डेटा लाया जा रहा है");
                    SharedPreferences.Editor editor1 = getSharedPreferences(M, j).edit();
                    editor1.putString("Lang", "Hin");
                    editor1.apply();
                } else{
                    pd.setMessage("Fetching data");
                    SharedPreferences.Editor editor1 = getSharedPreferences(M, j).edit();
                    editor1.putString("Lang", "Eng");
                    editor1.apply();
                }
                recreate();
                return true;
            case R.id.logout: {
                Intent intent = new Intent(Free_Lancing.this, Login.class);
                startActivity(intent);
                SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                editor.putString("Status", "Null");
                editor.apply();
                finishAffinity();
                break;
            }
            case R.id.rate_us:
                Intent rateIntent = new Intent(Free_Lancing.this, Rating.class);
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

    public void toEng(){
        switch (activity){
            case "Government":
                jobType.setText("Government Jobs");
                break;
            case "Private":
                jobType.setText("Private Jobs");
                break;
            case "Freelancing":
                jobType.setText("Freelancing");
                break;
            case "Tenders":
                jobType.setText("Tenders");
                break;
        }
        English = true;
        lang = "Eng";
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
    }

    public void toHin(){
        switch (activity){
            case "Government":
                jobType.setText(R.string.government_jobs1);
                break;
            case "Private":
                jobType.setText(R.string.non_government_jobs1);
                break;
            case "Freelancing":
                jobType.setText(R.string.freelancing1);
                break;
            case "Tenders":
                jobType.setText(R.string.tenders1);
                break;
        }
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
        setOptionTitle(R.id.create_your_job, getResources().getString(R.string.publish_your_job1));
        setOptionTitle(R.id.topJobs, getResources().getString(R.string.top_jobs1));
    }
    public void optionEng(){
        setOptionTitle(R.id.switch1, "Change Language");
        setOptionTitle(R.id.rate_us, "Rate Us");
        setOptionTitle(R.id.logout, "Logout");
        setOptionTitle(R.id.contact_us, "Contact Us");
        setOptionTitle(R.id.create_your_job, "Publish Your Job");
        setOptionTitle(R.id.topJobs, "Top Jobs");
    }
    private void setOptionTitle(int id, String title)
    {
        MenuItem item = menu1.findItem(id);
        item.setTitle(title);
    }
    public void NavHin(){
        Gov.setTitle("                  सरकारी नौकरियों");
        Non_Gov.setTitle("                  गैर सरकारी नौकरी");
        Tender.setTitle("                  निविदाएं");
        Free_Lancing.setTitle("                  फ़्रीलांसिंग");
        GetPremium.setTitle("                  प्रीमियम प्राप्त करें");
        Premium.setText("प्रीमियम");
        Days.setText(days + " दिन शेष");
    }
    public void NavEng(){
        Gov.setTitle("                  Government Jobs");
        Non_Gov.setTitle("                  Non-Government Jobs");
        Tender.setTitle("                  Tenders");
        Free_Lancing.setTitle("                  Freelancing");
        GetPremium.setTitle("                  Get Premium");
        Premium.setText("Premium");
        if(days.equals("1")){
            Days.setText(days + " day remaining");
        } else {
            Days.setText(days + "days remaining");
        }
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

    public void showAd(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}
