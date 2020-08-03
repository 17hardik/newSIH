package com.example.sih.Atmanirbhar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sih.MainActivity;
import com.example.sih.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VendorService extends AppCompatActivity {
    EditText JobName, Description, NumberOfDays;
    Button Post;
    DatabaseReference reff, reff1, reff2;
    String phone, S;
    Boolean isUploaded = false;
    long count1;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(S, i);
        phone = preferences.getString("Phone", "");
        setContentView(R.layout.activity_vendor_service);
        JobName = findViewById(R.id.job_name);
        Description = findViewById(R.id.description);
        NumberOfDays = findViewById(R.id.number_of_days);
        Post = findViewById(R.id.post);
        reff = FirebaseDatabase.getInstance().getReference();
        reff1 = FirebaseDatabase.getInstance().getReference().child("Atmanirbhar");
        reff2 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution");
        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count1 = snapshot.child("Atmanirbhar").getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(JobName.getText().toString().equals("") || Description.getText().toString().equals("") || NumberOfDays.getText().toString().equals("" )){
                    Toast.makeText(VendorService.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    reff1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long count = snapshot.child(phone).getChildrenCount();
                            if(!isUploaded) {
                                reff.child("Atmanirbhar").child(phone).child(Long.toString(count + 1)).child("JobName").setValue(JobName.getText().toString());
                                reff.child("Atmanirbhar").child(phone).child(Long.toString(count + 1)).child("Description").setValue(Description.getText().toString());
                                reff.child("Atmanirbhar").child(phone).child(Long.toString(count + 1)).child("Days").setValue(NumberOfDays.getText().toString());
                                reff.child("Atmanirbhar").child(phone).child(Long.toString(count + 1)).child("Phone").setValue(phone);
                                reff.child("Jobs Revolution").child("Atmanirbhar").child(Long.toString(count1 + 1)).child("JobName").setValue(JobName.getText().toString());
                                reff.child("Jobs Revolution").child("Atmanirbhar").child(Long.toString(count1 + 1)).child("Description").setValue(Description.getText().toString());
                                reff.child("Jobs Revolution").child("Atmanirbhar").child(Long.toString(count1 + 1)).child("Days").setValue(NumberOfDays.getText().toString());
                                reff.child("Jobs Revolution").child("Atmanirbhar").child(Long.toString(count1 + 1)).child("Phone").setValue(phone);
                                Toast.makeText(VendorService.this, "Job added successully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VendorService.this, MainActivity.class);
                                startActivity(intent);
                                isUploaded = true;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
    }
}