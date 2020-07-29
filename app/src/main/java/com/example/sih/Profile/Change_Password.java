
package com.example.sih.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sih.R;
import com.example.sih.Registration.Login;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;

/** Through this activity, a user can change his/her password by providing their current password
 * After verification, new password will be assigned to the user
 */

public class Change_Password extends AppCompatActivity {
    EditText ETOld, ETNew, ETConfirm;
    Button BTPassword;
    String phone, pass, M, S, check, Cipher, password, new_pass, conf_pass, New_Cipher, lang;
    int i, j, count = 1, count2 = 1;
    Firebase firebase;
    DatabaseReference reff;
    ImageView Eye, EyeNew;
    Boolean isVerified = false, English = true;
    Menu menu1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Change Password");
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences preferences = getSharedPreferences(S,i);
        phone = preferences.getString("Phone","");
        SharedPreferences preferences1 = getSharedPreferences(M, j);
        check = preferences1.getString("Lang", "Eng");
        setContentView(R.layout.activity_change__password);
        ETOld = findViewById(R.id.old_password);
        ETNew = findViewById(R.id.new_password);
        Eye = findViewById(R.id.eye);
        EyeNew = findViewById(R.id.eyeNew);
        ETConfirm = findViewById(R.id.confirm_password);
        BTPassword = findViewById(R.id.passwordButton);
        ETNew.setEnabled(false);
        ETConfirm.setEnabled(false);
        Firebase.setAndroidContext(this);
        if(!check.equals(getResources().getString(R.string.english))){
            toHin();
        } else{
            toEng();
        }

        Eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count%2!=0) {
                    ETOld.setInputType(InputType.TYPE_CLASS_TEXT);
                    ETOld.setSelection(ETOld.getText().length());
                    Eye.setImageResource(R.drawable.closed_eye);
                }
                else{
                    ETOld.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ETOld.setTypeface(Typeface.SANS_SERIF);
                    ETOld.setSelection(ETOld.getText().length());
                    Eye.setImageResource(R.drawable.open_eye);
                }
                count++;
            }
        });

        EyeNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count2%2!=0) {
                    ETNew.setInputType(InputType.TYPE_CLASS_TEXT);
                    ETNew.setSelection(ETNew.getText().length());
                    ETConfirm.setInputType(InputType.TYPE_CLASS_TEXT);
                    ETConfirm.setSelection(ETConfirm.getText().length());
                    EyeNew.setImageResource(R.drawable.closed_eye);
                }
                else{
                    ETNew.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ETNew.setTypeface(Typeface.SANS_SERIF);
                    ETNew.setSelection(ETNew.getText().length());
                    ETConfirm.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ETConfirm.setTypeface(Typeface.SANS_SERIF);
                    ETConfirm.setSelection(ETConfirm.getText().length());
                    EyeNew.setImageResource(R.drawable.open_eye);
                }
                count2++;
            }
        });

        firebase = new Firebase("https://smart-e60d6.firebaseio.com/Users");
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                password = dataSnapshot.child("Password").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(!check.equals(getResources().getString(R.string.english))) {
                    Toast.makeText(Change_Password.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(Change_Password.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }
        });
        BTPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isVerified) {
                    pass = ETOld.getText().toString().trim();
                    if (pass.equals("")) {
                        if (!check.equals(getResources().getString(R.string.english))) {
                            ETOld.setError(getResources().getString(R.string.must_be_filled1));
                        } else {
                            ETOld.setError("Must be filled");
                        }
                    } else {
                        BigInteger hash = BigInteger.valueOf((phone.charAt(0) - '0') + (phone.charAt(2) - '0') + (phone.charAt(4) - '0') + (phone.charAt(6) - '0') + (phone.charAt(8) - '0'));
                        StringBuilder sb = new StringBuilder();
                        char[] letters = pass.toCharArray();
                        for (char ch : letters) {
                            sb.append((byte) ch);
                        }
                        String a = sb.toString();
                        BigInteger i = new BigInteger(a);
                        hash = i.multiply(hash);
                        Cipher = String.valueOf(hash);
                        if (Cipher.equals(password)) {
                            ETNew.setEnabled(true);
                            ETConfirm.setEnabled(true);
                            if(!check.equals(getResources().getString(R.string.english)) || !English){
                                BTPassword.setText(R.string.change_password1);
                            } else {
                                BTPassword.setText("Change Password");
                            }
                            isVerified = true;
                        } else {
                            if(!check.equals(getResources().getString(R.string.english)) || !English){
                                Toast.makeText(Change_Password.this, getResources().getString(R.string.incorrect_password1), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Change_Password.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                else {
                    new_pass = ETNew.getText().toString().trim();
                    conf_pass = ETConfirm.getText().toString().trim();
                    if (!(new_pass.equals(conf_pass))) {
                        if (!check.equals(getResources().getString(R.string.english))) {
                            ETConfirm.setError(getResources().getString(R.string.passwords_matching1));
                        } else {
                            ETConfirm.setError("Passwords are not matching");
                        }
                        ETConfirm.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    } else if (new_pass.length() < 5) {
                        if (!check.equals(getResources().getString(R.string.english))) {
                            ETNew.setError(getResources().getString(R.string.too_short1));
                        } else {
                            ETNew.setError("At least 5 characters must be there");
                        }
                        ETNew.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    } else{
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
                        Intent intent = new Intent(Change_Password.this, Profile.class);
                        startActivity(intent);
                        if(check.equals("Hin") || !English) {
                            Toast.makeText(Change_Password.this,  getResources().getString(R.string.password_updated1), Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(Change_Password.this, "Password updated successfully" , Toast.LENGTH_SHORT).show();
                        }
                    }
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
                    English = false;
                } else {
                    toEng();
                    optionEng();
                    English = true;
                }
                return true;

            case R.id.logout: {
                Intent intent = new Intent(Change_Password.this, Login.class);
                startActivity(intent);
                SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                editor.putString("Status", "No");
                editor.apply();
                finishAffinity();
                break;
            }
            case R.id.rate_us:
                Intent rateIntent = new Intent(Change_Password.this, Rating.class);
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
    public void toEng() {
        BTPassword.setText("Verify");
        ETOld.setHint("Enter Your Old Password");
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
        BTPassword.setText(R.string.verify1);
        ETOld.setHint(R.string.old_password1);
        ETNew.setHint(R.string.new_password1);
        ETConfirm.setHint(R.string.confirm_password1);
        getSupportActionBar().setTitle(R.string.change_password1);
        English = false;
        lang = "Hin";
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
    }
}