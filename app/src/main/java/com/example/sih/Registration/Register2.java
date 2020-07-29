package com.example.sih.Registration;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sih.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/** Activity through which users can submit their related documents
 * Documents are then stored in Firebase Storage associated with user's phone number
 * Users can also skip this activity if they don't want to share their document of don't have all the documents at the moment
 */

public class Register2 extends AppCompatActivity {

    final static int PICK_PDF_CODE = 2342;
    TextView tenth, twelfth, graduation, textViewStatus, skip;
    Boolean tw = false, gr = false;
    StorageReference mStorageReference;
    int counter = 1, j;
    DatabaseReference mDatabaseReference;
    Button Button10, Button12, ButtonGr;
    String M, check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        setContentView(R.layout.activity_register2);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        textViewStatus =  findViewById(R.id.textViewStatus);
        tenth =  findViewById(R.id.editTextFileName);
        twelfth =  findViewById(R.id.editTextFileName1);
        graduation =  findViewById(R.id.editTextFileName2);
        skip = findViewById(R.id.skip);
        Button10 = findViewById(R.id.buttonUploadFile);
        Button12 = findViewById(R.id.buttonUploadFile1);
        ButtonGr = findViewById(R.id.buttonUploadFile2);
        ButtonGr.setBackgroundResource(R.drawable.button);
        Button10.setBackgroundResource(R.drawable.button);
        Button12.setBackgroundResource(R.drawable.button);
        skip.setBackgroundResource(R.drawable.button);
        if(!check.equals(getResources().getString(R.string.english))){
            toHin();
        }
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent skipIntent = new Intent(Register2.this, Login.class);
                startActivity(skipIntent);
            }
        });

        Button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Here counter is checking whether all documents are submitted that is 3
                if(counter==3){
                    if(!check.equals(getResources().getString(R.string.english))){
                        skip.setText(R.string.next1);
                    }else {
                        skip.setText("Next");
                    }
                }
                getPDF();
            }
        });
        Button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tw = true;
                if(counter==3){
                    if(!check.equals(getResources().getString(R.string.english))){
                        skip.setText(R.string.next1);
                    }else {
                        skip.setText("Next");
                    }
                }
                getPDF();
            }
        });
        ButtonGr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gr = true;
                if(counter==3){
                    if(!check.equals(getResources().getString(R.string.english))){
                        skip.setText(R.string.next1);
                    }else {
                        skip.setText("Next");
                    }
                }
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
                if(!check.equals(getResources().getString(R.string.english))){
                    Toast.makeText(this, R.string.textViewStatus1, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadFile(Uri data) {
        Intent intent = getIntent();
        String userphone = intent.getStringExtra("userphone");
        if(tw){
            StorageReference sRef = mStorageReference.child(userphone).child("12th marksheet.pdf");
            tw = false;
            sRef.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if(!check.equals(getResources().getString(R.string.english))){
                                textViewStatus.setText(R.string.textViewStatus2);
                            }else {
                                textViewStatus.setText("File Uploaded Successfully");
                            }
                            counter++;
                            Button12.setEnabled(false);
                            if(!check.equals(getResources().getString(R.string.english))){
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
                            if(!check.equals(getResources().getString(R.string.english))){
                                textViewStatus.setText((int) progress + "% अपलोडिंग...");
                            }else {
                                textViewStatus.setText((int) progress + "% Uploading...");
                            }
                        }
                    });
        }
        else if(gr){
            StorageReference sRef = mStorageReference.child(userphone).child("Graduation marksheet.pdf");
            gr = false;
            sRef.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if(!check.equals(getResources().getString(R.string.english))){
                                textViewStatus.setText(R.string.textViewStatus2);
                            }else {
                                textViewStatus.setText("File Uploaded Successfully");
                            }

                            //Increasing the value of counter after submission of every document
                            counter++;
                            ButtonGr.setEnabled(false);
                            if(!check.equals(getResources().getString(R.string.english))){
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
                            if(!check.equals(getResources().getString(R.string.english))){
                                textViewStatus.setText((int) progress + "% अपलोडिंग...");
                            }else {
                                textViewStatus.setText((int) progress + "% Uploading...");
                            }
                        }
                    });
        }
        else {
            StorageReference sRef = mStorageReference.child(userphone).child("10th marksheet.pdf");
            sRef.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if(!check.equals(getResources().getString(R.string.english))){
                                textViewStatus.setText(R.string.textViewStatus2);
                            }else {
                                textViewStatus.setText("File Uploaded Successfully");
                            }
                            counter++;
                            Button10.setEnabled(false);
                            if(!check.equals(getResources().getString(R.string.english))){
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
                            if(!check.equals(getResources().getString(R.string.english))){
                                textViewStatus.setText((int) progress + "% अपलोडिंग...");
                            }else {
                                textViewStatus.setText((int) progress + "% Uploading...");
                            }
                        }
                    });
        }

    }

    public void toHin() {
        skip.setText(R.string.skip1);
        tenth.setText(R.string.tenth1);
        twelfth.setText(R.string.twelfth1);
        graduation.setText(R.string.graduation1);
        Button10.setText(R.string.upload1);
        Button12.setText(R.string.upload1);
        ButtonGr.setText(R.string.upload1);
        textViewStatus.setText(R.string.textViewStatus1);
    }

}