package com.example.sih.Profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.sih.R;
import com.example.sih.Registration.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Delete_Acc extends Dialog implements View.OnClickListener{
    Activity activity;
    Button BTYes, BTNo;
    int i, j;
    String phone, check, S, M;
    TextView deleteText;
    DatabaseReference reff;
    FirebaseUser currentuser;
    StorageReference mStorageReference;

    public Delete_Acc(Activity a) {
        super(a);
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = activity.getSharedPreferences(S,i);
        phone = preferences.getString("Phone","");
        SharedPreferences preferences1 = activity.getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.delete_account);
        BTYes =  findViewById(R.id.yes_button);
        BTNo = findViewById(R.id.no_button);
        deleteText = findViewById(R.id.delete_text);
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        if(check.equals("Hin")){
            deleteText.setText(R.string.want_to_delete1);
            BTYes.setText(R.string.yes1);
            BTNo.setText(R.string.no1);
        }
        BTYes.setOnClickListener(this);
        BTNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes_button:
                currentuser.delete();
                reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
                StorageReference sRef1 = mStorageReference.child(phone).child("12th marksheet.pdf");
                StorageReference sRef2 = mStorageReference.child(phone).child("10th marksheet.pdf");
                StorageReference sRef3 = mStorageReference.child(phone).child("Company ID.pdf");
                StorageReference sRef4 = mStorageReference.child(phone).child("Profile Picture");
                reff.removeValue();
                sRef1.delete();
                sRef2.delete();
                sRef3.delete();
                sRef4.delete();
                Intent intent = new Intent(activity, Login.class);
                activity.startActivity(intent);
                activity.finishAffinity();
                break;
            case R.id.no_button:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
