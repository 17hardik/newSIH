package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/** This is the activity after filling registration form
 *  For phone verification
 *  Verification is done by sending an OTP on given number and verifying the OTP
 */

public class PhoneVerification extends AppCompatActivity implements View.OnClickListener {

    EditText etOtp;
    Button btResendOtp, btVerifyOtp;
    private FirebaseAuth mAuth;
    String name, password, phone, username, dob, Cipher, mVerificationId, S, M, check, encryptedUsername;
    int i, j;
    Intent intent;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        setContentView(R.layout.activity_phone_verification);
        initFields();
        if(check.equals("Hin")){
            toHin();
        }
        intent = getIntent();
        phone = intent.getStringExtra("phone");
        username = intent.getStringExtra("username");
        name = intent.getStringExtra("name");
        dob = intent.getStringExtra("dob");
        password = intent.getStringExtra("password");
        mResendToken = intent.getParcelableExtra("mResendToken");
        mVerificationId = intent.getStringExtra("verificationId");

        mAuth = FirebaseAuth.getInstance();
        encryptedUsername = encryptUsername(username).toString();
        BigInteger hash = BigInteger.valueOf((phone.charAt(0)-'0')+(phone.charAt(2)-'0')+(phone.charAt(4)-'0')+(phone.charAt(6)-'0')+(phone.charAt(8)-'0'));
        StringBuilder sb = new StringBuilder();
        char[] letters = password.toCharArray();
        for (char ch : letters) {
            sb.append((byte) ch);
        }
        String a = sb.toString();
        BigInteger i = new BigInteger(a);
        hash = i.multiply(hash);
        Cipher = String.valueOf(hash);
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
                            "+91" + phone,
                            1,
                            TimeUnit.MINUTES,
                            this,
                            mCallbacks,
                            mResendToken);
                    if(check.equals("Hin"))
                    {
                        Toast.makeText(PhoneVerification.this, getResources().getString(R.string.otp_sent1), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(PhoneVerification.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                    }
                    btResendOtp.setEnabled(false);
                    buttonEnable();
                    break;
                case R.id.bt_verify_otp:
                    if (etOtp.getText().toString().equals("")) {
                        if(check.equals("Hin")){
                            etOtp.setError(getResources().getString(R.string.must_be_filled1));
                        }else {
                            etOtp.setError("Must be filled");
                        }
                    }else {
                        final ProgressDialog pd = new ProgressDialog(PhoneVerification.this);
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
                                                SharedPreferences.Editor editor1 = getSharedPreferences(S,i).edit();
                                                editor1.putString("Phone", phone);
                                                editor1.putString("NewPhone", phone);
                                                editor1.apply();
                                                Firebase reference = new Firebase("https://smart-e60d6.firebaseio.com/Users");
                                                reference.child(phone).child("Username").setValue(encryptedUsername);
                                                reference.child(phone).child("Password").setValue(Cipher);
                                                reference.child(phone).child("Name").setValue(name);
                                                reference.child(phone).child("DOB").setValue(dob);
                                                reference.child(phone).child("Phone").setValue(phone);

                                                if(check.equals("Hin")){
                                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(PhoneVerification.this)
                                                            .setSmallIcon(R.drawable.logo)
                                                            .setDefaults(Notification.DEFAULT_ALL)
                                                            .setDefaults(Notification.DEFAULT_SOUND)
                                                            .setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                                                            .setContentTitle(getResources().getString(R.string.registration_successful1))
                                                            .setContentText(getResources().getString(R.string.welcome1));
                                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                    notificationManager.notify(1, builder.build());
                                                    Toast.makeText(PhoneVerification.this, getResources().getString(R.string.registration_successful1), Toast.LENGTH_LONG).show();
                                                }else {
                                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(PhoneVerification.this)
                                                            .setSmallIcon(R.drawable.logo)
                                                            .setDefaults(Notification.DEFAULT_ALL)
                                                            .setDefaults(Notification.DEFAULT_SOUND)
                                                            .setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                                                            .setContentTitle("Registration Successful")
                                                            .setContentText("Welcome to Rojgar App");
                                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                    notificationManager.notify(1, builder.build());
                                                    Toast.makeText(PhoneVerification.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                                }
                                                Intent verificationIntent = new Intent(PhoneVerification.this, Register2.class);
                                                verificationIntent.putExtra("userphone", phone);
                                                startActivity(verificationIntent);
                                                finishAffinity();
                                                pd.dismiss();
                                        } else {
                                            if(check.equals("Hin")){
                                                Toast.makeText(PhoneVerification.this, getResources().getString(R.string.verification_failed1), Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(PhoneVerification.this, "Verification Failed, Invalid OTP", Toast.LENGTH_SHORT).show();
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

                PhoneVerification.this.runOnUiThread(new Runnable() {

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
    }

    public StringBuilder encryptUsername(String uname) {
        StringBuilder stringBuilder = new StringBuilder();
        Random r = new Random();
            int len = uname.length();
            for (int i = 0; i < len; i++) {
                char a = uname.charAt(i);
                char c = (char) (r.nextInt(26) + 'a');
                stringBuilder.append((a - len) - (2 * i));
                stringBuilder.append(c);
            }
            String strlen = Integer.toString(len + 2);
            stringBuilder.append(strlen);
            return stringBuilder;
    }

}