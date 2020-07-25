package com.example.sih.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sih.Registration.Forgot_Second;
import com.example.sih.R;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/** Activity for account recovery in case if a user forgets his/her password
 * Users can change their password by verifying their DOB and Username
 */

public class Forgot_Password extends AppCompatActivity {
    private EditText Phone, Username, DOB;
    private Calendar myCalendar;
    private String dob, username, storedDOB, storedUsername, phone, check;
    String M, K;
    private Button Verify;
    int j, l;

    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgot__password);
        Phone = findViewById(R.id.phone);
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

        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(Forgot_Password.this);
                if(check.equals("Hin")){
                    pd.setMessage(getResources().getString(R.string.verifying1));
                }else {
                    pd.setMessage("Verifying...");
                }
                pd.show();
                phone = Phone.getText().toString().trim();
                dob = DOB.getText().toString();
                username = Username.getText().toString().trim();
                if(phone.equals("")){
                    if(check.equals("Hin")){
                        Phone.setError(getResources().getString(R.string.must_be_filled1));
                        Phone.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                        pd.dismiss();
                    }else {
                        Phone.setError("Must be filled");
                        Phone.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                        pd.dismiss();
                    }
                }
                 else if(username.equals("")){
                    if(check.equals("Hin")){
                        Username.setError(getResources().getString(R.string.must_be_filled1));
                        Username.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                        pd.dismiss();
                    }else {
                        Username.setError("Must be filled");
                        Username.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                        pd.dismiss();
                    }
                }
                 else if(dob.equals("")){
                     if(check.equals("Hin")){
                         DOB.setError(getResources().getString(R.string.must_be_filled1));
                         DOB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                         pd.dismiss();
                     }else {
                         DOB.setError("Must be filled");
                         DOB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                         pd.dismiss();
                     }
                 }

                 else {

                     // matching entered credentials with our database records
                     try {
                         reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
                     } catch (Exception e) {

                     }
                     reff.addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             try {
                                 storedUsername = dataSnapshot.child("Username").getValue().toString();
                                 storedDOB = dataSnapshot.child("DOB").getValue().toString();
                             } catch (Exception e){

                             }
                             try {
                                 if (storedDOB.equals(dob) && storedUsername.equals(username)) {
                                     Intent intent = new Intent(Forgot_Password.this, Forgot_Second.class);
                                     intent.putExtra("phone", phone);
                                     startActivity(intent);
                                     pd.dismiss();

                                 } else {

                                     if (check.equals("Hin")) {
                                         pd.dismiss();
                                         Toast.makeText(Forgot_Password.this, getResources().getString(R.string.username_dob_incorrect), Toast.LENGTH_SHORT).show();
                                     } else {
                                         pd.dismiss();
                                         Toast.makeText(Forgot_Password.this, "Username or DOB is incorrect", Toast.LENGTH_SHORT).show();
                                     }
                                 }
                             } catch (Exception e){
                                 if (check.equals("Hin")) {
                                     pd.dismiss();
                                     Toast.makeText(Forgot_Password.this, getResources().getString(R.string.user_not_found1), Toast.LENGTH_SHORT).show();
                                 } else {
                                     pd.dismiss();
                                     Toast.makeText(Forgot_Password.this, "User not found", Toast.LENGTH_SHORT).show();
                                 }
                             }

                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {
                             if (check.equals("Hin")) {
                                 pd.dismiss();
                                 Toast.makeText(Forgot_Password.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                             } else {
                                 pd.dismiss();
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
        Phone.setHint(R.string.phone1);
        Username.setHint(R.string.username1);
        DOB.setHint(R.string.date1);
        Verify.setText(R.string.verify1);
    }
}
