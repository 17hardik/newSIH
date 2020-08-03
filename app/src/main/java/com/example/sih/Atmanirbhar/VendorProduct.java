package com.example.sih.Atmanirbhar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sih.MainActivity;
import com.example.sih.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VendorProduct extends AppCompatActivity {
    EditText ProductName, Description, Price, Location;
    Button Post;
    TextView title;
    DatabaseReference reff, reff1, reff2;
    String phone, S, M, check;
    Boolean isUploaded = false;
    long count1;
    int i, j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        SharedPreferences preferences = getSharedPreferences(S, i);
        phone = preferences.getString("Phone", "");
        setContentView(R.layout.activity_vendor_product);
        ProductName = findViewById(R.id.product_name);
        Description = findViewById(R.id.description);
        title = findViewById(R.id.title);
        Location = findViewById(R.id.location);
        Price = findViewById(R.id.price);
        Post = findViewById(R.id.post);
        reff = FirebaseDatabase.getInstance().getReference();
        reff1 = FirebaseDatabase.getInstance().getReference().child("Atmanirbhar");
        reff2 = FirebaseDatabase.getInstance().getReference().child("Products");

        if(check.equals("Hin")){
            title.setText("अपना उत्पाद बेचें");
            ProductName.setHint("उत्पाद का नाम दर्ज करें");
            Location.setHint("स्थान दर्ज करें");
            Description.setHint("उत्पाद विवरण दर्ज करें");
            Post.setText("पोस्ट करें");
            Price.setHint("रुपये में मूल्य दर्ज करें");
        }

        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count1 = snapshot.child("Atmanirbhar").child(phone).getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ProductName.getText().toString().equals("") || Description.getText().toString().equals("") || Price.getText().toString().equals("") || Location.getText().toString().equals("")){
                    if(check.equals("Eng")) {
                        Toast.makeText(VendorProduct.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VendorProduct.this, "कृपया सभी विवरण भरें", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    reff1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long count = snapshot.child(phone).getChildrenCount();
                            if(!isUploaded) {
                                reff.child("Atmanirbhar").child(phone).child(Long.toString(count + 1)).child("ProductName").setValue(ProductName.getText().toString());
                                reff.child("Atmanirbhar").child(phone).child(Long.toString(count + 1)).child("Description").setValue(Description.getText().toString());
                                reff.child("Atmanirbhar").child(phone).child(Long.toString(count + 1)).child("Price").setValue(Price.getText().toString());
                                reff.child("Atmanirbhar").child(phone).child(Long.toString(count + 1)).child("Location").setValue(Location.getText().toString());
                                reff.child("Atmanirbhar").child(phone).child(Long.toString(count + 1)).child("Phone").setValue(phone);
                                reff.child("Products").child("Atmanirbhar").child(phone).child(Long.toString(count1 + 1)).child("ProductName").setValue(ProductName.getText().toString());
                                reff.child("Products").child("Atmanirbhar").child(phone).child(Long.toString(count1 + 1)).child("Location").setValue(Location.getText().toString());
                                reff.child("Products").child("Atmanirbhar").child(phone).child(Long.toString(count1 + 1)).child("Description").setValue(Description.getText().toString());
                                reff.child("Products").child("Atmanirbhar").child(phone).child(Long.toString(count1 + 1)).child("Price").setValue(Price.getText().toString());
                                reff.child("Products").child("Atmanirbhar").child(phone).child(Long.toString(count1 + 1)).child("Phone").setValue(phone);
                                if(check.equals("Eng")) {
                                    Toast.makeText(VendorProduct.this, "Product added successully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(VendorProduct.this, "उत्पाद सफलतापूर्वक जोड़ा गया", Toast.LENGTH_SHORT).show();
                                }
                                Intent intent = new Intent(VendorProduct.this, MainActivity.class);
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