package com.example.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class jobDetails extends AppCompatActivity {
    EditText title, description, experience, city, email;
    Button post;
    DatabaseReference reff;
    details details;
    int i;
    String phone, newPhone, S;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences1 = getSharedPreferences(S,i);
        phone = preferences1.getString("Phone","");

        setContentView(R.layout.activity_job_details);

        title = findViewById(R.id.editText1);
        description = findViewById(R.id.editText2);
        experience = findViewById(R.id.editText3);
        city = findViewById(R.id.editText4);
        email = findViewById(R.id.editText5);
        post = findViewById(R.id.button4);
        details = new details();
        reff = FirebaseDatabase.getInstance().getReference().child("Users");

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                details.setTitle(title.getText().toString().trim());
                details.setDescription(description.getText().toString().trim());
                details.setExperience(experience.getText().toString().trim());
                details.setCity(city.getText().toString().trim());
                details.setEmail(email.getText().toString().trim());

                try{

                    if (title.getText().toString().trim().equals("")) {
                        title.setError("Must be Filled");
                        title.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (description.getText().toString().trim().equals("")) {
                        description.setError("Must be Filled");
                        description.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (description.getText().toString().trim().length() < 30){
                        description.setError("At least 30 characters must be there");
                        description.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (experience.getText().toString().trim().equals("")) {
                        experience.setError("Must be Filled");
                        experience.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (city.getText().toString().trim().equals("")) {
                        city.setError("Must be Filled");
                        city.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else  if (email.getText().toString().trim().equals("")) {
                        email.setError("Must be Filled");
                        email.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else {
                        reff.child("Job Post").child(phone).child(title.getText().toString().trim()).setValue(details);
                        Toast.makeText(jobDetails.this, "Uploaded Job Details Successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(jobDetails.this, jobsPublished.class);
                        startActivity(intent);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
}
