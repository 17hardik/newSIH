package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/** Activity through which users can update or change their phone number
 * An OTP will be sent to verify the provided phone number
 */

public class Change_Phone extends AppCompatActivity implements View.OnClickListener {

    EditText etOtp;
    Button btResendOtp, btVerifyOtp;
    private FirebaseAuth mAuth;
    String phone, mVerificationId, S, M, check, newPhone;
    int i, j;
    Intent intent;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Verify Phone");
        actionBar.setDisplayHomeAsUpEnabled(true);
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        SharedPreferences preferences = getSharedPreferences(S,i);
        phone = preferences.getString("Phone","");
        setContentView(R.layout.activity_change__phone);
        initFields();
        if(check.equals("Hin")){
            toHin();
        }
        intent = getIntent();
        mResendToken = intent.getParcelableExtra("mResendToken");
        mVerificationId = intent.getStringExtra("verificationId");
        newPhone = intent.getStringExtra("newPhone");
        mAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        btVerifyOtp.setBackgroundResource(R.drawable.button);
        btResendOtp.setBackgroundResource(R.drawable.button);
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                if(check.equals("Hin")){
                    btResendOtp.setText(millisUntilFinished / 1000+" सेकंड "+ getResources().getString(R.string.resend_message1));
                } else {
                    btResendOtp.setText("You can resend OTP after " + millisUntilFinished / 1000+" seconds");
                }
            }

            public void onFinish() {
                if(check.equals("Hin")){
                    btResendOtp.setText(R.string.resend_otp1);
                } else {
                    btResendOtp.setText("Resend OTP");
                }
            }

        }.start();
    }

    void initFields() {
        etOtp = findViewById(R.id.et_otp);
        btResendOtp = findViewById(R.id.bt_resend_otp);
        btVerifyOtp = findViewById(R.id.bt_verify_otp);
        btResendOtp.setOnClickListener(this);
        btVerifyOtp.setOnClickListener(this);
        btResendOtp.setEnabled(false);
        buttonEnable();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_resend_otp:
                initFireBaseCallbacks();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + newPhone,
                        1,
                        TimeUnit.MINUTES,
                        this,
                        mCallbacks,
                        mResendToken);
                if(check.equals("Hin"))
                {
                    Toast.makeText(Change_Phone.this, getResources().getString(R.string.otp_sent1), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Change_Phone.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                }
                btResendOtp.setEnabled(false);
                buttonEnable();
                break;
            case R.id.bt_verify_otp:
                if (etOtp.getText().toString().equals("")) {
                    if(check.equals("Hin")){
                        etOtp.setError(getResources().getString(R.string.must_be_filled1));
                        etOtp.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }else {
                        etOtp.setError("Must be filled");
                        etOtp.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }
                }else {
                    final ProgressDialog pd = new ProgressDialog(Change_Phone.this);
                    if(check.equals("Hin")){
                        pd.setMessage(getResources().getString(R.string.verifying1));
                    }else {
                        pd.setMessage("Verifying...");
                    }
                    pd.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, etOtp.getText().toString());
                    mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Firebase reference = new Firebase("https://smart-e60d6.firebaseio.com/Users");
                                        reference.child(phone).child("Phone").setValue(newPhone);
                                        SharedPreferences.Editor editor1 = getSharedPreferences(S,i).edit();
                                        editor1.putString("NewPhone", newPhone);
                                        editor1.apply();
                                        if(check.equals("Hin")){
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(Change_Phone.this)
                                                    .setSmallIcon(R.drawable.logo)
                                                    .setDefaults(Notification.DEFAULT_ALL)
                                                    .setDefaults(Notification.DEFAULT_SOUND)
                                                    .setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                                                    .setContentTitle(getResources().getString(R.string.phone_updated1))
                                                    .setContentText(getResources().getString(R.string.phone_changed1));
                                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.notify(1, builder.build());
                                            Toast.makeText(Change_Phone.this, getResources().getString(R.string.phone_updated1), Toast.LENGTH_LONG).show();
                                        }else {
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(Change_Phone.this)
                                                    .setSmallIcon(R.drawable.logo)
                                                    .setDefaults(Notification.DEFAULT_ALL)
                                                    .setDefaults(Notification.DEFAULT_SOUND)
                                                    .setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                                                    .setContentTitle("Phone Updated Succesfully")
                                                    .setContentText("Your phone number has been changed");
                                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.notify(1, builder.build());
                                            Toast.makeText(Change_Phone.this, "Phone Changed Successful", Toast.LENGTH_LONG).show();
                                        }
                                        Intent profileIntent = new Intent(Change_Phone.this, Profile.class);
                                        startActivity(profileIntent);
                                        finishAffinity();
                                        pd.dismiss();
                                    } else {
                                        if(check.equals("Hin")){
                                            Toast.makeText(Change_Phone.this, getResources().getString(R.string.verification_failed1), Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(Change_Phone.this, "Verification Failed, Invalid OTP", Toast.LENGTH_SHORT).show();
                                        }
                                        pd.dismiss();
                                    }
                                }
                            });
                }
                break;
        }
    }
    void initFireBaseCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

            }
        };
    }
    public void buttonEnable(){
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.schedule(new Runnable() {

            @Override
            public void run() {

                Change_Phone.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        btResendOtp.setEnabled(true);
                    }
                });
            }
        }, 1, TimeUnit.MINUTES);
    }

    public void toHin(){
        etOtp.setHint(R.string.verify_otp1);
        btResendOtp.setText(R.string.resend_otp1);
        btVerifyOtp.setText(R.string.verify_otp_btn1);
        getSupportActionBar().setTitle(R.string.phone_verify1);
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