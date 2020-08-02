package com.example.sih.Atmanirbhar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sih.R;

public class Atmanirbhar extends AppCompatActivity {

    Button Client, Vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmanirbhar);

        Client = findViewById(R.id.buttonClient);
        Vendor = findViewById(R.id.buttonVendor);

        Client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Atmanirbhar.this, ProductService.class);
                intent.putExtra("Type", "Client");
                startActivity(intent);
            }
        });
        Vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Atmanirbhar.this, ProductService.class);
                intent.putExtra("Type", "Vendor");
                startActivity(intent);
            }
        });
    }
}