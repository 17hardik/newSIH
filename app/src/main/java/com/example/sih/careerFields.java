package com.example.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class careerFields extends AppCompatActivity {
    RadioGroup careerFields;
    RadioButton field;
    Button proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_fields);

        careerFields = findViewById(R.id.careerFields);
        proceed = findViewById(R.id.getRoadmap);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId = careerFields.getCheckedRadioButtonId();
                field = findViewById(radioId);
                Toast.makeText(careerFields.this, "You aspire to be in: " + field.getText(), Toast.LENGTH_LONG).show();
                if(field.getText().equals("IT SECTOR")){
                    Intent intent = new Intent(careerFields.this, ItSector.class);
                    startActivity(intent);
                    finish();
                }
                if(field.getText().equals("BANK SECTOR")){
                    Intent intent = new Intent(careerFields.this, BankSector.class);
                    startActivity(intent);
                    finish();
                }
                if(field.getText().equals("EDUCATION SECTOR")){
                    Intent intent = new Intent(careerFields.this, EduSector.class);
                    startActivity(intent);
                    finish();
                }
                if(field.getText().equals("MARKETING SECTOR")){
                    Intent intent = new Intent(careerFields.this, Marketing.class);
                    startActivity(intent);
                    finish();
                }
                if(field.getText().equals("AGRICULTURE")){
                    Intent intent = new Intent(careerFields.this, Agriculture.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    public void checkButton(View v){
        int radioId = careerFields.getCheckedRadioButtonId();
        field = findViewById(radioId);
    }

}