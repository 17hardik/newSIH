package com.example.sih.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sih.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.math.BigInteger;

/** Another activity for account recovery
 * Here, users can will write their new password as changed password
 * After verification, new password will be assigned to them
 */

public class Forgot_Second extends AppCompatActivity {
    EditText ETNew, ETConfirm;
    Button BTPassword;
    String phone, M, check, password, new_pass, conf_pass, New_Cipher, lang, K, clicked;
    int j, l;
    Firebase firebase;
    Intent intent;
    DatabaseReference reff;
    Boolean English = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Change Password");
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences preferences2 = getSharedPreferences(K, l);
        clicked = preferences2.getString("Clicked","");
        SharedPreferences preferences1 = getSharedPreferences(M, j);

        check = preferences1.getString("Lang", "Eng");
        setContentView(R.layout.activity_forgot__second);
        ETNew = findViewById(R.id.new_password);
        ETConfirm = findViewById(R.id.confirm_password);
        BTPassword = findViewById(R.id.passwordButton);
        BTPassword.setBackgroundResource(R.drawable.button);
        Firebase.setAndroidContext(this);
        intent = getIntent();
        phone = intent.getStringExtra("phone");
        if(check.equals("Hin")){
            toHin();
        } else{
            toEng();
        }
        if(clicked.equals("Yes")){
            finish();
        }
        firebase = new Firebase("https://smart-e60d6.firebaseio.com/Users");
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                password = dataSnapshot.child("Password").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals("Hin")) {
                    Toast.makeText(Forgot_Second.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(Forgot_Second.this, "There is some error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        BTPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    new_pass = ETNew.getText().toString().trim();
                    conf_pass = ETConfirm.getText().toString().trim();
                    if (!(new_pass.equals(conf_pass))) {
                        if (check.equals("Hin")) {
                            ETConfirm.setError(getResources().getString(R.string.passwords_matching1));
                            ETConfirm.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                        } else {
                            ETConfirm.setError("Passwords are not matching");
                            ETConfirm.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                        }
                    } else if (new_pass.length() < 5) {
                        if (check.equals("Hin")) {
                            ETNew.setError(getResources().getString(R.string.too_short1));
                            ETNew.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                        } else {
                            ETNew.setError("At least 5 characters must be there");
                            ETConfirm.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                        }
                    } else{
                        SharedPreferences.Editor editor = getSharedPreferences(K,l).edit();
                        editor.putString("Clicked", "Yes");
                        editor.apply();
                        BigInteger hash = BigInteger.valueOf((phone.charAt(0) - '0') + (phone.charAt(2) - '0') + (phone.charAt(4) - '0') + (phone.charAt(6) - '0') + (phone.charAt(8) - '0'));
                        StringBuilder sb = new StringBuilder();
                        char[] letters = new_pass.toCharArray();
                        for (char ch : letters) {
                            sb.append((byte) ch);
                        }
                        String a = sb.toString();
                        BigInteger i = new BigInteger(a);
                        hash = i.multiply(hash);
                        New_Cipher = String.valueOf(hash);
                        firebase.child(phone).child("Password").setValue(New_Cipher);
                        if(check.equals("Hin") || !English) {
                            Toast.makeText(Forgot_Second.this,  getResources().getString(R.string.password_updated1), Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(Forgot_Second.this, "Password updated successfully" , Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(Forgot_Second.this, Login.class);
                        startActivity(intent);
                        finishAffinity();
                    }
            }
        });
    }


    public void toEng() {
        BTPassword.setText("Change Password");
        ETNew.setHint("Enter Your New Password");
        ETConfirm.setHint("Confirm Your Password");
        English = true;
        lang = "Eng";
        getSupportActionBar().setTitle("Change Password");
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
    }

    public void toHin(){
        BTPassword.setText(R.string.change_password1);
        ETNew.setHint(R.string.new_password1);
        ETConfirm.setHint(R.string.confirm_password1);
        getSupportActionBar().setTitle(R.string.change_password1);
        English = false;
        lang = "Hin";
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(clicked.equals("Yes")) {
            finish();
        }
    }
}
