package com.example.sih.Atmanirbhar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sih.R;

public class ProductService extends AppCompatActivity {
    TextView Title;
    Intent intent;
    String Type;
    Button Product, Service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_service);

        Product = findViewById(R.id.buttonProduct);
        Service = findViewById(R.id.buttonService);
        intent = getIntent();
        Type = intent.getStringExtra("Type");

        Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Type.equals("Client")){
                    Intent intent = new Intent(ProductService.this, ClientProduct.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ProductService.this, VendorProduct.class);
                    startActivity(intent);
                }
            }
        });

        Service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Type.equals("Client")){
                    Intent intent = new Intent(ProductService.this, ClientService.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ProductService.this, VendorService.class);
                    startActivity(intent);
                }
            }
        });

    }
}