package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Forgot_Password extends AppCompatActivity {
    EditText Username, DOB;
    Calendar myCalendar;
    String dob, username, storedDOB, storedUsername, phone, S, M, check, K;
    Button Verify;
    int i, j, l;
    Boolean English = true;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(S,i);
        phone = preferences.getString("Phone","");
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        setContentView(R.layout.activity_forgot__password);
        Username = findViewById(R.id.username);
        DOB = findViewById(R.id.dob);
        Verify = findViewById(R.id.verifyButton);
        Verify.setBackgroundResource(R.drawable.button);

        if(check.equals("Hin"))
        {
            toHin();
        }
        SharedPreferences.Editor editor = getSharedPreferences(K,l).edit();
        editor.putString("Clicked", "No");
        editor.apply();

        FirebaseApp.initializeApp(this);
        Firebase.setAndroidContext(this);

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        DOB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(Forgot_Password.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        try {
            reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        } catch (Exception e) {

        }

        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dob = DOB.getText().toString();
                username = Username.getText().toString().trim();
                 if(username.equals("")){
                    if(check.equals("Hin")){
                        Username.setError(getResources().getString(R.string.must_be_filled1));
                    }else {
                        Username.setError("Must be filled");
                    }
                }
                 else if(dob.equals("")){
                     if(check.equals("Hin")){
                         DOB.setError(getResources().getString(R.string.must_be_filled1));
                     }else {
                         DOB.setError("Must be filled");
                     }
                 }
                 else {

                     // matching entered credentials with our database records
                     reff.addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             storedUsername = dataSnapshot.child("Username").getValue().toString();
                             storedDOB = dataSnapshot.child("DOB").getValue().toString();

                             if (storedDOB.equals(dob) && storedUsername.equals(username)) {
                                 Intent intent = new Intent(Forgot_Password.this, Forgot_Second.class);
                                 startActivity(intent);

                             } else {

                                 if (check.equals("Hin")) {
                                     Toast.makeText(Forgot_Password.this, getResources().getString(R.string.username_dob_incorrect), Toast.LENGTH_SHORT).show();
                                 } else {
                                     Toast.makeText(Forgot_Password.this, "Username or DOB is incorrect", Toast.LENGTH_SHORT).show();
                                 }
                             }

                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {
                             if (check.equals("Hin") || !English) {
                                 Toast.makeText(Forgot_Password.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                             } else {
                                 Toast.makeText(Forgot_Password.this, "There is some error", Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
                 }

            }
        });
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        DOB.setText(sdf.format(myCalendar.getTime()));
    }
    public void toHin(){
        Username.setHint(R.string.username1);
        DOB.setHint(R.string.date1);
        Verify.setText(R.string.verify1);
    }
}
