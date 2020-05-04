package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/** Activity through which users can view their profile and can make edit in it
 * Both database reading and writing functionality are working here
 */

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    EditText ETUsername, ETName, ETPhone;
    Button BTUsername, BTName, BTPhone, BTPassword, BTCertificates;
    DatabaseReference reff;
    TextView uname, uphone;
    ImageView camera, profile, drawerProfile;
    Boolean English = true;
    Firebase firebase;
    StorageReference mStorageReference;
    String user_name, name, phone, S, M, check, lang, mVerificationId, user_phone;
    int i, j;
    final static int PICK_IMAGE_REQUEST = 2342;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle t;
    Menu menu1, menu2;
    MenuItem Gov, Non_Gov, Tender, Free_Lancing;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    ProgressDialog pd;
    File mypath;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Non-Government Jobs");
        SharedPreferences preferences = getSharedPreferences(S,i);
        phone = preferences.getString("Phone","");
        path = preferences.getString("path","");
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        setContentView(R.layout.activity_profile);
        ETUsername = findViewById(R.id.usernameET);
        ETName = findViewById(R.id.nameET);
        camera = findViewById(R.id.camera);
        profile = findViewById(R.id.profile_image);
        ETPhone = findViewById(R.id.phoneET);
        BTUsername = findViewById(R.id.usernameBT);
        BTName = findViewById(R.id.nameBT);
        BTPhone = findViewById(R.id.phoneBT);
        BTPassword = findViewById(R.id.password);
        BTCertificates = findViewById(R.id.certificate);
        FirebaseApp.initializeApp(this);
        Firebase.setAndroidContext(this);
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
        uname = navigationView.getHeaderView(0).findViewById(R.id.name_of_user);
        uphone = navigationView.getHeaderView(0).findViewById(R.id.phone_of_user);
        drawerProfile = navigationView.getHeaderView(0).findViewById(R.id.image_of_user);
        loadImageFromStorage(path);

        if(check.equals("Hin")){
            NavHin();
            toHin();
        } else{
            NavEng();
            toEng();
        }
        pd = new ProgressDialog(Profile.this);
        if(check.equals("Hin") || !English){
            pd.setMessage("छवि अपडेट हो रही है...");
        } else {
            pd.setMessage("Updating Image...");
        }
        firebase = new Firebase("https://smart-e60d6.firebaseio.com/Users");
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_name = dataSnapshot.child("Username").getValue().toString();
                name = dataSnapshot.child("Name").getValue().toString();
                user_phone = dataSnapshot.child("Phone").getValue().toString();
                mStorageReference = FirebaseStorage.getInstance().getReference().child(user_phone).child("Profile Picture");
                ETUsername.setText(user_name);
                ETName.setText(name);
                ETPhone.setText(user_phone);

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
                                    drawerProfile.setMinimumHeight(dm.heightPixels);
                                    drawerProfile.setMinimumWidth(dm.widthPixels);
                                    drawerProfile.setImageBitmap(bm);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });
                } catch(Exception e){

                }
               uphone.setText(user_phone);
               uname.setText(user_name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals("Hin") || !English) {
                    Toast.makeText(Profile.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(Profile.this, "There is some error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

        BTUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    firebase.child(phone).child("Username").setValue(ETUsername.getText().toString().trim());
                    if(check.equals("Hin") || !English){
                        Toast.makeText(Profile.this, getResources().getString(R.string.username_updated1), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Profile.this, "Username Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
        });
        BTName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    firebase.child(phone).child("Name").setValue(ETName.getText().toString().trim());
                    if(check.equals("Hin") || !English){
                        Toast.makeText(Profile.this, getResources().getString(R.string.name_updated1), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Profile.this, "Name Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        BTPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Change_Password.class);
                startActivity(intent);
            }
        });
        BTCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Update_Certificates.class);
                startActivity(intent);
            }
        });
        BTPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_phone.equals(ETPhone.getText().toString().trim())) {
                    if(check.equals("Hin") || !English)
                    {
                        Toast.makeText(Profile.this, getResources().getString(R.string.phone_change1), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Profile.this, "Please change your phone before clicking", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    initFireBaseCallbacks();
                    send_data();
                }
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.government:
                Intent intent1 = new Intent(Profile.this,Government.class);
                startActivity(intent1);
                break;
            case R.id.free_lancing:
                Intent intent = new Intent(Profile.this, Free_Lancing.class);
                startActivity(intent);
                break;
            case R.id.tenders:
                Intent intent5 = new Intent(Profile.this, Tenders.class);
                startActivity(intent5);
                break;
            case R.id.non_government:
                Intent intent6 = new Intent(Profile.this, Non_Government.class);
                startActivity(intent6);
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
                if(English){
                    toHin();
                    NavHin();
                    optionHin();
                    English = false;
                }else{
                    toEng();
                    NavEng();
                    optionEng();
                    English = true;
                }
                return true;
            case R.id.logout: {
                Intent intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
                SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                editor.putString("Status", "Null");
                editor.apply();
                finishAffinity();
                break;

            }
            case R.id.rate_us:
                Intent rateIntent = new Intent(Profile.this, Rating.class);
                startActivity(rateIntent);
                return true;

            case R.id.contact_us:
                String recipient = "firstloveyourself1999@gmail.com";
                String subject = "Related to Rojgar App";
                Intent intent4 = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                intent4.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
                intent4.putExtra(Intent.EXTRA_SUBJECT, subject);
                startActivity(intent4);
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
    public void toEng(){
        BTUsername.setText("Change Username");
        BTPassword.setText("Change Password");
        BTName.setText("Change Name");
        BTCertificates.setText("Update Qualification Certificates");
        BTPhone.setText("Change Phone");
        getSupportActionBar().setTitle("Profile");
        English = true;
        lang = "Eng";
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
    }

    public void toHin(){
        BTUsername.setText(R.string.change_username1);
        BTPassword.setText(R.string.change_password1);
        BTName.setText(R.string.change_name1);
        BTCertificates.setText(R.string.update_qualification_certificates1);
        BTPhone.setText(R.string.change_phone1);
        getSupportActionBar().setTitle(R.string.profle1);
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
        setOptionTitle(R.id.go_to_profile, getResources().getString(R.string.go_to_profile1));
    }
    public void optionEng(){
        setOptionTitle(R.id.switch1, "Change Language");
        setOptionTitle(R.id.rate_us, "Rate Us");
        setOptionTitle(R.id.logout, "Logout");
        setOptionTitle(R.id.contact_us, "Contact Us");
        setOptionTitle(R.id.go_to_profile, "Go To Profile");
    }
    public void NavHin(){
        Gov.setTitle("                  सरकारी नौकरियों");
        Non_Gov.setTitle("                  गैर सरकारी नौकरी");
        Tender.setTitle("                  निविदाएं");
        Free_Lancing.setTitle("                  फ़्रीलांसिंग");
    }
    public void NavEng(){
        Gov.setTitle("                  Government Jobs");
        Non_Gov.setTitle("                  Non-Government Jobs");
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
    void initFireBaseCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (check.equals("Hin")) {
                    ETPhone.setError(getResources().getString(R.string.valid1));
                } else {
                    ETPhone.setError("Enter a valid number");
                }
            }
            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                if(check.equals("Hin"))
                {
                    Toast.makeText(Profile.this, getResources().getString(R.string.otp_sent1), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Profile.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                }
                mVerificationId = verificationId;
                mResendToken = token;
                Intent intent = new Intent(Profile.this, Change_Phone.class);
                intent.putExtra("verificationId", mVerificationId);
                intent.putExtra("resendToken", mResendToken);
                intent.putExtra("phone", phone);
                intent.putExtra("newPhone", ETPhone.getText().toString().trim());
                startActivity(intent);
        }

      };

    }
            public void send_data(){
                 PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + ETPhone.getText().toString().trim(),
                1,
                TimeUnit.MINUTES,
                this,
                mCallbacks);
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
         //   NavEng();
            toEng();
            try{
                optionEng();
            } catch(Exception e){

            }
        }
    }

    private void checkPermission() {

        //Checking Storage read permission for fetching internal documents
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (check.equals("Hin")) {
                Toast.makeText(this, R.string.permission1, Toast.LENGTH_SHORT).show();
            } else {
                if(check.equals("Hin") || !English) {
                    Toast.makeText(this, getResources().getString(R.string.permission1), Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(this, "Please grant storage permission", Toast.LENGTH_SHORT).show();
                }
            }
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if(check.equals("Hin") || !English){
            startActivityForResult(Intent.createChooser(intent, "छवि का चयन करें"), PICK_IMAGE_REQUEST);
        } else {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap img = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                if (img != null) {
                    pd.show();
                    profile.setImageBitmap(img);
                    drawerProfile.setImageBitmap(img);
                    uploadFile(data.getData());
                    path = saveToInternalStorage(img);
                    SharedPreferences.Editor editor1 = getSharedPreferences(S,i).edit();
                    editor1.putString("path", path);
                    editor1.apply();
                } else{

                }
            } catch (Exception e) {

            }
        }
    }

    private void uploadFile(Uri data) {
            mStorageReference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @SuppressWarnings("VisibleForTests")
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   pd.dismiss();
                   if(check.equals("Hin")) {
                       Toast.makeText(Profile.this, "अद्यतन सफलतापूर्ण हो गया", Toast.LENGTH_SHORT).show();
                   } else{
                       Toast.makeText(Profile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                   }
                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }
                    });
        }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("profile_picture", Context.MODE_PRIVATE);
        mypath = new File(directory,"profile.jpg");
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
    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            profile.setImageBitmap(b);
            drawerProfile.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}
