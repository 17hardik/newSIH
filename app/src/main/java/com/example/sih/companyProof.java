package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class companyProof extends AppCompatActivity {
    private TextView companyName;
    final static int PICK_PDF_CODE = 2342;
    TextView companyId, textViewStatus;
    Boolean tw = false, gr = false;
    StorageReference mStorageReference;
    int counter = 1, i, j;
    DatabaseReference mDatabaseReference;
    Button upButton, register;
    String M, check, user_name, phone, newPhone, S;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_proof);

        companyName = findViewById(R.id.textView3);

        String Jname = getIntent().getStringExtra("companyName");

        companyName.setText("Your Company/Start-up Name: " + Jname);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        companyId = findViewById(R.id.textView4);
        textViewStatus = findViewById(R.id.textView5);
        upButton = findViewById(R.id.button2);
        register = findViewById(R.id.button3);

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newPhone = dataSnapshot.child("Phone").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(companyProof.this, "There is some error", Toast.LENGTH_SHORT).show();

            }
        });

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPDF();
            }
        });
    }

        private void getPDF() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Please grant storage permission", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                return;
            }

            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_CODE);

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data.getData() != null) {
                uploadFile(data.getData());
            }
            else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }



    }
}
