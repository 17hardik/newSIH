package com.example.sih.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sih.Profile.Forgot_Password;
import com.example.sih.MainActivity;
import com.example.sih.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;

public class Login extends AppCompatActivity {
    TextView register, forget;
    EditText Phone, Password;
    Button loginButton;
    DatabaseReference reff;
    ImageView Eye;
    String phone, pass, S, Cipher, M, A, check, new_phone, realPhone = "Null",premium_date, isFirst;
    int i, j, count = 1, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M,j);

        // Checking the current selected language inside app
        check = preferences1.getString("Lang","Eng");
        SharedPreferences preferences = getSharedPreferences(S,i);

        SharedPreferences preferences2 = getSharedPreferences(A,b);
        isFirst = preferences2.getString("isFirst","notFirst");

        //retrieving original and changed phone number from SharedPreferences
        realPhone = preferences.getString("Phone","Null");
        new_phone = preferences.getString("NewPhone","Null");
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.register);
        forget = findViewById(R.id.forget);
        Phone = findViewById(R.id.phone);
        Password = findViewById(R.id.password);
        Eye = findViewById(R.id.eye);
        loginButton = findViewById(R.id.loginButton);

        if(check.equals("Hin"))
        {
            toHin();
        }

        Eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count%2!=0) {
                    Password.setInputType(InputType.TYPE_CLASS_TEXT);
                    Password.setSelection(Password.getText().length());
                    Eye.setImageResource(R.drawable.closed_eye);
                }
                else{
                    Password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Password.setTypeface(Typeface.SANS_SERIF);
                    Password.setSelection(Password.getText().length());
                    Eye.setImageResource(R.drawable.open_eye);
                }
                count++;
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Forgot_Password.class));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                premium_date = snapshot.child("Premium Date").getValue().toString();
                                SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                                editor.putString("isPremium", "Yes");
                                editor.apply();
                            } catch (Exception e) {
                                SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                                editor.putString("isPremium", "No");
                                editor.apply();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } catch(Exception e){

                }

                phone = Phone.getText().toString().trim();
                pass = Password.getText().toString().trim();
                if(phone.equals("")){
                    if(check.equals("Hin")){
                        Phone.setError(getResources().getString(R.string.must_be_filled1));
                    }else {
                        Phone.setError("Must be filled");
                    }
                    Phone.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                }
                else if(pass.equals("")){
                    if(check.equals("Hin")){
                        Password.setError(getResources().getString(R.string.must_be_filled1));
                    }else {
                        Password.setError("Must be filled");
                    }
                    Password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                }
                else{
                    final ProgressDialog pd = new ProgressDialog(Login.this);

                        if(check.equals("Hin")){
                            pd.setMessage(getResources().getString(R.string.logging_in1));
                        }else {
                            pd.setMessage("Logging in...");
                        }

                        pd.show();
                        BigInteger hash = BigInteger.valueOf((phone.charAt(0) - '0') + (phone.charAt(2) - '0') + (phone.charAt(4) - '0') + (phone.charAt(6) - '0') + (phone.charAt(8) - '0'));
                        StringBuilder sb = new StringBuilder();
                        char[] letters = pass.toCharArray();

                        for (char ch : letters) {
                            sb.append((byte) ch);
                        }
                        String a = sb.toString();
                        BigInteger temp = new BigInteger(a);
                        hash = temp.multiply(hash);
                        Cipher = String.valueOf(hash);
                        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
                        reff.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    String storedPass = dataSnapshot.child("Password").getValue().toString();

                                     if (storedPass.equals(Cipher)) {

                                        if (isFirst.equals("notFirst")){

                                            SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                                            editor.putString("Status", "Yes");
                                            editor.putString("Phone", phone);
                                            editor.apply();
                                            startActivity(new Intent(Login.this, Favorite_Sectors.class));
                                            finishAffinity();

                                        }

                                        else {
                                            SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                                            editor.putString("Status", "Yes");
                                            editor.putString("Phone", phone);
                                            editor.apply();
                                            startActivity(new Intent(Login.this, MainActivity.class));
                                            finishAffinity();
                                        }
                                    }

                                    else{
                                        if(check.equals("Hin")){
                                            Toast.makeText(Login.this, R.string.incorrect_password1, Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } catch (Exception e) {
                                    if(check.equals("Hin")){
                                        Toast.makeText(Login.this, R.string.user_not_found1, Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(Login.this, "User not found", Toast.LENGTH_LONG).show();
                                    }
                                }
                                pd.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                if(check.equals("Hin")) {
                                    Toast.makeText(Login.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(Login.this, "There is some error", Toast.LENGTH_SHORT).show();
                                }
                                pd.dismiss();
                            }
                        });
                }
            }
        });
    }

    public void toHin(){
        register.setText(R.string.register1);
        loginButton.setText(R.string.login1);
        Phone.setHint(R.string.phone1);
        forget.setText(R.string.forgot_password1);
        Password.setHint(R.string.password1);
    }

}