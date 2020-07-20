package com.example.sih;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class careerFields extends AppCompatActivity {
    RadioGroup careerFields;
    TextView FieldsQ;
    RadioButton field, IT, Bank, Education, Marketing, Agriculture;
    Button proceed;
    String M, check;
    int j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        setContentView(R.layout.activity_career_fields);

        careerFields = findViewById(R.id.careerFields);
        proceed = findViewById(R.id.getRoadmap);
        FieldsQ = findViewById(R.id.fieldsQ);
        IT = findViewById(R.id.IT);
        Agriculture = findViewById(R.id.AGRICULTURE);
        Bank = findViewById(R.id.BANK);
        Education = findViewById(R.id.EDUCATION);
        Marketing = findViewById(R.id.MARKETING);

        if(check.equals("Hin")){
            FieldsQ.setText(R.string.fields_question1);
            IT.setText(R.string.it_sector1);
            Agriculture.setText(R.string.agriculture_sector1);
            Bank.setText(R.string.bank_sector1);
            Education.setText(R.string.education_sector1);
            Marketing.setText(R.string.marketing_sector1);
            proceed.setText(R.string.get_roadmap1);
        }

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