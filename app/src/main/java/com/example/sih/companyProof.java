package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class companyProof extends AppCompatActivity {
    private TextView companyName;
    EditText CRpost;
    final static int PICK_PDF_CODE = 2342;
    TextView companyId, textViewStatus;
    StorageReference mStorageReference;
    int counter = 1, i, j;
    DatabaseReference mDatabaseReference;
    Button upButton, register;
    String phone, newPhone, S, company;
    DatabaseReference reff;
    Users1 users1;
    Intent intent;
    Boolean isUploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(S,i);
        phone = preferences1.getString("Phone","");
        setContentView(R.layout.activity_company_proof);
        companyName = findViewById(R.id.textView3);
        String Jname = getIntent().getStringExtra("companyName");
        companyName.setText("Your Company/Start-up Name: " + Jname);
        CRpost = findViewById(R.id.editText2);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        companyId = findViewById(R.id.textView4);
        textViewStatus = findViewById(R.id.textView5);
        upButton = findViewById(R.id.button2);
        register = findViewById(R.id.button3);
        intent = getIntent();
        company = intent.getStringExtra("companyName");
        reff = FirebaseDatabase.getInstance().getReference().child("Users");
        users1 = new Users1();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                users1.setCRpost(CRpost.getText().toString().trim());

                try{

                    if (CRpost.getText().toString().trim().equals("")) {
                        CRpost.setError("Must be Filled");
                        CRpost.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (!isUploaded){
                        Toast.makeText(companyProof.this, "Upload Company ID",Toast.LENGTH_LONG).show();
                    }

                    else {
                        reff.child(phone).child("Company").setValue(company);
                        reff.child("Company Representative Details").child(phone).child("Post").setValue(CRpost.getText().toString().trim());
                        Toast.makeText(companyProof.this, "Company Registered successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(companyProof.this, jobPublish.class );
                        startActivity(intent);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newPhone = dataSnapshot.child(phone).child("Phone").getValue().toString();
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

    private void uploadFile(Uri data) {
            StorageReference sRef = mStorageReference.child(newPhone).child("Company ID.pdf");
            sRef.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                textViewStatus.setText("File Uploaded Successfully");
                            counter++;
                            upButton.setEnabled(false);
                                upButton.setText("Uploaded");
                            isUploaded = true;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                textViewStatus.setText((int) progress + "% Uploading...");
                        }
                    });

    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return true;

    }
}
