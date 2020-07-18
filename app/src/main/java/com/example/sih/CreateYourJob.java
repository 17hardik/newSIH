package com.example.sih;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateYourJob extends AppCompatActivity {

    EditText Cname, CRemail, CRnum, Cloc;
    Button Cregister;
    DatabaseReference reff;
    Users1 users1;
    int i;
    String phone, S;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences1 = getSharedPreferences(S,i);
        phone = preferences1.getString("Phone","");

        setContentView(R.layout.activity_create_your_job);

        Cname = findViewById(R.id.editText);
        CRemail = findViewById(R.id.editText3);
        CRnum = findViewById(R.id.editText4);
        Cloc = findViewById(R.id.editText5);
        Cregister = findViewById(R.id.button);
        users1 = new Users1();
        reff = FirebaseDatabase.getInstance().getReference().child("Users");

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
                        Cname.setError("Must be Filled");
                        Cname.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (CRemail.getText().toString().trim().equals("")){
                        CRemail.setError("Must be Filled");
                        CRemail.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (CRnumb.equals("")){
                        CRnum.setError("Must be Filled");
                        CRnum.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (Cloc.getText().toString().trim().equals("")){
                        Cloc.setError("Must be Filled");
                        Cloc.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else{
                        reff.child(phone).child("Company").setValue(Cname.getText().toString().trim());
                        reff.child("Company Representative Details").child(phone).setValue(users1);
                        Toast.makeText(CreateYourJob.this, "data inserted successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CreateYourJob.this, companyProof.class);
                        intent.putExtra("companyName", cName);
                        startActivity(intent);
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

}