package com.example.sih;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Update_Certificates extends AppCompatActivity {

    final static int PICK_PDF_CODE = 2342;
    TextView tenth, twelfth, graduation, textViewStatus;
    Boolean tw = false, gr = false;
    StorageReference mStorageReference;
    int counter = 1, i, j;
    DatabaseReference mDatabaseReference;
    Button Button10, Button12, ButtonGr;
    String M, check, phone, newPhone, S;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Update Qualification Certificates");
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences preferences = getSharedPreferences(S,i);
        phone = preferences.getString("Phone","");
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        setContentView(R.layout.activity_update__certificates);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        textViewStatus =  findViewById(R.id.textViewStatus);
        tenth =  findViewById(R.id.editTextFileName);
        twelfth =  findViewById(R.id.editTextFileName1);
        graduation =  findViewById(R.id.editTextFileName2);
        Button10 = findViewById(R.id.buttonUploadFile);
        Button12 = findViewById(R.id.buttonUploadFile1);
        ButtonGr = findViewById(R.id.buttonUploadFile2);
        ButtonGr.setBackgroundResource(R.drawable.button);
        Button10.setBackgroundResource(R.drawable.button);
        Button12.setBackgroundResource(R.drawable.button);
        if(check.equals("Hin")){
            toHin();
        }
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newPhone = dataSnapshot.child("Phone").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals("Hin")) {
                    Toast.makeText(Update_Certificates.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(Update_Certificates.this, "There is some error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPDF();
            }
        });
        Button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tw = true;
                getPDF();
            }
        });
        ButtonGr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr = true;
                getPDF();
            }
        });
    }

    private void getPDF() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 1);
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
            }else{
                if(check.equals("Hin")){
                    Toast.makeText(this, R.string.textViewStatus1, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadFile(Uri data) {
        if(tw){
            StorageReference sRef = mStorageReference.child(newPhone).child("12th marksheet.pdf");
            tw = false;
            sRef.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if(check.equals("Hin")){
                                textViewStatus.setText(R.string.textViewStatus2);
                            }else {
                                textViewStatus.setText("File Uploaded Successfully");
                            }
                            counter++;
                            Button12.setEnabled(false);
                            if(check.equals("Hin")){
                                Button12.setText(R.string.uploaded1);
                            }else {
                                Button12.setText("Uploaded");
                            }
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
                            if(check.equals("Hin")){
                                textViewStatus.setText((int) progress + "% अपलोडिंग...");
                            }else {
                                textViewStatus.setText((int) progress + "% Uploading...");
                            }
                        }
                    });
        }
        else if(gr){
            StorageReference sRef = mStorageReference.child(newPhone).child("Graduation marksheet.pdf");
            gr = false;
            sRef.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if(check.equals("Hin")){
                                textViewStatus.setText(R.string.textViewStatus2);
                            }else {
                                textViewStatus.setText("File Uploaded Successfully");
                            }
                            counter++;
                            ButtonGr.setEnabled(false);
                            if(check.equals("Hin")){
                                ButtonGr.setText(R.string.uploaded1);
                            }else {
                                ButtonGr.setText("Uploaded");
                            }
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
                            if(check.equals("Hin")){
                                textViewStatus.setText((int) progress + "% अपलोडिंग...");
                            }else {
                                textViewStatus.setText((int) progress + "% Uploading...");
                            }
                        }
                    });
        }
        else {
            StorageReference sRef = mStorageReference.child(newPhone).child("10th marksheet.pdf");
            sRef.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if(check.equals("Hin")){
                                textViewStatus.setText(R.string.textViewStatus2);
                            }else {
                                textViewStatus.setText("File Uploaded Successfully");
                            }
                            counter++;
                            Button10.setEnabled(false);
                            if(check.equals("Hin")){
                                Button10.setText(R.string.uploaded1);
                            }else {
                                Button10.setText("Uploaded");
                            }
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
                            if(check.equals("Hin")){
                                textViewStatus.setText((int) progress + "% अपलोडिंग...");
                            }else {
                                textViewStatus.setText((int) progress + "% Uploading...");
                            }
                        }
                    });
        }

    }

    public void toHin(){
        tenth.setText(R.string.tenth1);
        twelfth.setText(R.string.twelfth1);
        graduation.setText(R.string.graduation1);
        Button10.setText(R.string.upload1);
        Button12.setText(R.string.upload1);
        ButtonGr.setText(R.string.upload1);
        textViewStatus.setText(R.string.textViewStatus1);
        getSupportActionBar().setTitle(R.string.update_qualification_certificates1);
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