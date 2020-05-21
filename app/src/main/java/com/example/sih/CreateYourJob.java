package com.example.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateYourJob extends AppCompatActivity {

    EditText Cname, CRemail, CRnum, Cloc;
    Button Cregister;
    DatabaseReference reff;
    Users1 users1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Intent intent = new Intent(CreateYourJob.this, companyProof.class);
                intent.putExtra("companyName", cName);
                startActivity(intent);

                reff.child("Jobs").child(CRnumb).setValue(users1);

                Toast.makeText(CreateYourJob.this, "data inserted successfully",Toast.LENGTH_LONG).show();
            }
        });

    }
}
