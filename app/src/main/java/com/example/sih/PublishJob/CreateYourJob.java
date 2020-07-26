package com.example.sih.PublishJob;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sih.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateYourJob extends AppCompatActivity {

    EditText Cname, CRemail, CRnum, Cloc;
    Button Cregister;
    DatabaseReference reff;
    Users1 users1;
    TextView title, warning;
    int i, j;
    String phone, S, email, isVerified, M, check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        SharedPreferences preferences3 = getSharedPreferences(S,i);
        phone = preferences3.getString("Phone","");
        SharedPreferences preference2 = getSharedPreferences(S,i);
        email = preference2.getString("Email","");
        SharedPreferences preference = getSharedPreferences(S,i);
        isVerified = preference.getString("isVerified","");
        setContentView(R.layout.activity_create_your_job);

        title = findViewById(R.id.title);
        warning = findViewById(R.id.warning);
        Cname = findViewById(R.id.company_name);
        CRemail = findViewById(R.id.work_email);
        CRnum = findViewById(R.id.contact);
        Cloc = findViewById(R.id.location);
        Cregister = findViewById(R.id.verify);
        users1 = new Users1();
        if(check.equals("Hin")){
            toHin();
        }
        reff = FirebaseDatabase.getInstance().getReference();
        Cregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String CRnumb = (CRnum.getText().toString().trim());
                users1.setCname(Cname.getText().toString().trim());
                users1.setCRemail(CRemail.getText().toString().trim());
                users1.setCRnum(CRnumb);
                users1.setCloc(Cloc.getText().toString().trim());
                String cName = Cname.getText().toString();

                try{

                    if (Cname.getText().toString().trim().equals("")){
                        if(check.equals("Hin")){
                            Cname.setError(getResources().getString(R.string.must_be_filled1));
                        } else {
                            Cname.setError("Must be Filled");
                        }
                        Cname.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (CRemail.getText().toString().trim().equals("")){
                        if(check.equals("Hin")){
                            CRemail.setError(getResources().getString(R.string.must_be_filled1));
                        } else {
                            CRemail.setError("Must be Filled");
                        }
                        CRemail.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (CRnumb.equals("")){
                        if(check.equals("Hin")){
                            CRnum.setError(getResources().getString(R.string.must_be_filled1));
                        } else {
                            CRnum.setError("Must be Filled");
                        }
                        CRnum.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }
                    else if(CRemail.getText().toString().contains("gmail") || CRemail.getText().toString().contains("outlook") || CRemail.getText().toString().contains("yahoo") || CRemail.getText().toString().contains("protonmail")){
                        if (check.equals("Hin")) {
                            CRemail.setError("कृपया अपना कार्य ईमेल दर्ज करें (जीमेल, आउटलुक आदि की अनुमति नहीं है)");
                        } else {
                            CRemail.setError("Please enter your work email (Gmail, Outlook etc are not allowed)");
                        }
                        CRemail.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }
                    else if (Cloc.getText().toString().trim().equals("")){
                        if(check.equals("Hin")){
                            Cloc.setError(getResources().getString(R.string.must_be_filled1));
                        } else {
                            Cloc.setError("Must be Filled");
                        }
                        Cloc.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else {
                          if(isVerified.equals("Yes")) {
                              if(check.equals("Eng")) {
                                  Cregister.setText("Register");
                              } else {
                                  Cregister.setText(R.string.register1);
                              }
                          reff.child("Users").child(phone).child("Company").setValue(Cname.getText().toString().trim());
                          reff.child("Company Representative Details").child(phone).setValue(users1);
                          if(check.equals("Eng")) {
                              Toast.makeText(CreateYourJob.this, "Data inserted successfully", Toast.LENGTH_LONG).show();
                          } else {
                              Toast.makeText(CreateYourJob.this, "डेटा सफलतापूर्वक डाला गया", Toast.LENGTH_LONG).show();
                          }
                          Intent intent = new Intent(CreateYourJob.this, companyProof.class);
                          intent.putExtra("companyName", cName);
                          startActivity(intent);
                          } else {
                              verifyEmail(CRemail.getText().toString().trim());
                          }
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String s1 = sh.getString("name","");
        String s2 = sh.getString("email","");
        String s3 = sh.getString("num","");
        String s4 = sh.getString("loc","");

        Cname.setText(s1);
        CRemail.setText(s2);
        CRnum.setText(s3);
        Cloc.setText(s4);
    }

    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("name", Cname.getText().toString());
        myEdit.putString("email", CRemail.getText().toString());
        myEdit.putString("num", CRnum.getText().toString());
        myEdit.putString("loc", Cloc.getText().toString());

        myEdit.apply();
    }

    public void verifyEmail(String email){
        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl("https://hope2.page.link/y1E4")
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setAndroidPackageName(
                                "com.example.sih",
                                false, /* installIfNotAvailable */
                                "12"    /* minimumVersion */)
                        .build();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendSignInLinkToEmail(email, actionCodeSettings).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateYourJob.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if(check.equals("Eng")) {
                                Toast.makeText(CreateYourJob.this, "Email sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CreateYourJob.this, "मेल भेजा जा चुका है", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    public void toHin(){
        Cname.setHint("अपनी कंपनी / स्टार्ट-अप का नाम दर्ज करें");
        CRemail.setHint("अपना कार्यस्थल ईमेल दर्ज करें");
        CRnum.setHint("अपना कार्यस्थल फ़ोन नंबर दर्ज करें");
        Cloc.setHint("अपनी कंपनी का स्थान दर्ज करें");
        title.setText("कंपनी के प्रतिनिधि का विवरण");
        warning.setText("विवरणों को ध्यान से भरें, इन्हें बाद में नहीं बदला जाएगा");
        if(isVerified.equals("Yes")) {
            Cregister.setText(R.string.register1);
        } else {
            Cregister.setText("ई मेल सत्यापित करें");
        }
    }

}