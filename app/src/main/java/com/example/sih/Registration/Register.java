package com.example.sih.Registration;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sih.R;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText username, password, TPhone, TName, TDate, TConfirm;
    Button registerButton;
    String user, pass, user_phone, user_name, date,confirm, mVerificationId, url, M, check;
    TextView login, already;
    ProgressDialog pd;
    int j, count = 1;
    ImageView Eye;
    Calendar myCalendar;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        login = findViewById(R.id.login2);
        login.setOnClickListener(this);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        already = findViewById(R.id.already);
        Eye = findViewById(R.id.eye);
        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);
        TDate= findViewById(R.id.dob);
        TPhone = findViewById(R.id.phone);
        TConfirm=findViewById(R.id.confirm);
        TName=findViewById(R.id.name);
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        TDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(Register.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        if(!check.equals(getResources().getString(R.string.english))){
            toHin();
        }

        Eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count%2!=0) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    password.setSelection(password.getText().length());
                    TConfirm.setInputType(InputType.TYPE_CLASS_TEXT);
                    TConfirm.setSelection(TConfirm.getText().length());
                    Eye.setImageResource(R.drawable.closed_eye);
                }
                else{
                    password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setTypeface(Typeface.SANS_SERIF);
                    password.setSelection(password.getText().length());
                    TConfirm.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    TConfirm.setTypeface(Typeface.SANS_SERIF);
                    TConfirm.setSelection(TConfirm.getText().length());
                    Eye.setImageResource(R.drawable.open_eye);
                }
                count++;
            }
        });

        Firebase.setAndroidContext(this);
        pd = new ProgressDialog(Register.this);
        initFireBaseCallbacks();
        url = "https://smart-e60d6.firebaseio.com/Users.json";
    }
    void initFireBaseCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if(check.equals(getResources().getString(R.string.english))){
                    TPhone.setError(getResources().getString(R.string.error1));
                } else {
                    TPhone.setError("Error: "+e.getMessage());
                }
                pd.dismiss();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                pd.dismiss();
                if(!check.equals(getResources().getString(R.string.english)))
                {
                    Toast.makeText(Register.this, getResources().getString(R.string.otp_sent1), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Register.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                }
                mVerificationId = verificationId;
                mResendToken = token;
                Intent intent = new Intent(Register.this, PhoneVerification.class);
                intent.putExtra("username", user);
                intent.putExtra("phone", user_phone);
                intent.putExtra("password", pass);
                intent.putExtra("name", user_name);
                intent.putExtra("dob", date);
                intent.putExtra("verificationId", mVerificationId);
                intent.putExtra("resendToken", mResendToken);
                startActivity(intent);
            }
        };
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_button:
                user = username.getText().toString().trim();
                pass = password.getText().toString().trim();
                user_phone = TPhone.getText().toString();
                user_name = TName.getText().toString().trim();
                confirm = TConfirm.getText().toString().trim();
                date = TDate.getText().toString();
                if(!check.equals(getResources().getString(R.string.english))){
                    pd.setMessage(getResources().getString(R.string.registering1));
                }else {
                    pd.setMessage("Registering...");
                }
                pd.show();
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject obj = new JSONObject(s);
                            if (obj.has(user_phone)) {
                                pd.dismiss();
                                if(!check.equals(getResources().getString(R.string.english))){
                                    Toast.makeText(Register.this, getResources().getString(R.string.user_exists1), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(Register.this, "User already exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if (user_name.equals("")) {
                                if(!check.equals(getResources().getString(R.string.english))){
                                    TName.setError(getResources().getString(R.string.must_be_filled1));
                                }else {
                                    TName.setError("Must be filled");
                                }
                                TName.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                                pd.dismiss();
                            }
                            else if (user.equals("")) {
                                if(!check.equals(getResources().getString(R.string.english))){
                                    username.setError(getResources().getString(R.string.must_be_filled1));
                                }else {
                                    username.setError("Must be filled");
                                }
                                username.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                                pd.dismiss();
                            } else if (pass.equals("")) {
                                if(!check.equals(getResources().getString(R.string.english))){
                                    password.setError(getResources().getString(R.string.must_be_filled1));
                                }else {
                                    password.setError("Must be filled");
                                }
                                password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                                pd.dismiss();
                            } else if (user.contains(" ")) {
                                if(!check.equals(getResources().getString(R.string.english))){
                                    username.setError(getResources().getString(R.string.space1));
                                } else {
                                    username.setError("Space is not allowed");
                                }
                                username.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                                pd.dismiss();
                            } else if (user.length() < 5) {
                                if(!check.equals(getResources().getString(R.string.english))){
                                    username.setError(getResources().getString(R.string.too_short1));
                                }else {
                                    username.setError("At least 5 characters must be there");
                                }
                                username.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                                pd.dismiss();
                            } else if (user_phone.equals("")) {
                                if(!check.equals(getResources().getString(R.string.english))){
                                    TPhone.setError(getResources().getString(R.string.must_be_filled1));
                                }else {
                                    TPhone.setError("Must be filled");
                                }
                                TPhone.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                                pd.dismiss();
                            } else if (user_phone.length() != 10) {
                                if(!check.equals(getResources().getString(R.string.english))){
                                    TPhone.setError(getResources().getString(R.string.valid1));
                                }else {
                                    TPhone.setError("Please enter a valid number");
                                }
                                TPhone.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                                pd.dismiss();
                            }else if (pass.length() < 5) {
                                if(!check.equals(getResources().getString(R.string.english))){
                                    password.setError(getResources().getString(R.string.too_short1));
                                }else {
                                    password.setError("At least 5 characters must be there");
                                }
                                password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                                pd.dismiss();
                            } else if (date.length() != 10 || (!date.contains("/"))) {
                                pd.dismiss();
                                if(check.equals(getResources().getString(R.string.english))){
                                    TDate.setError(getResources().getString(R.string.incorrect_dob1));
                                }else {
                                    TDate.setError("Incorrect DOB Format");
                                }
                                TDate.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                            } else if (!(pass.equals(confirm))) {
                                pd.dismiss();
                                if(!check.equals(getResources().getString(R.string.english))){
                                    TConfirm.setError(getResources().getString(R.string.passwords_matching1));
                                }else {
                                    TConfirm.setError("Passwords are not matching");
                                }
                                TConfirm.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                            } else {
                                if(!check.equals(getResources().getString(R.string.english))){
                                    pd.setMessage(getResources().getString(R.string.sending_otp1));
                                }else {
                                    pd.setMessage("Sending OTP...");
                                }
                                pd.show();

                                //Sending OTP to verify provided phone number
                                send_data();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                    }
                });
                RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                rQueue.add(request);

                break;
            case R.id.login2:
                Intent loginIntent = new Intent(Register.this, Login.class);
                startActivity(loginIntent);
                break;
        }
    }
    public void send_data(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + user_phone,
                1,
                TimeUnit.MINUTES,
                this,
                mCallbacks);
    }

    public void toHin(){
        login.setText(R.string.login1);
        password.setHint(R.string.password1);
        username.setHint(R.string.username1);
        TName.setHint(R.string.name1);
        TConfirm.setHint(R.string.reenter1);
        TPhone.setHint(R.string.phone1);
        TDate.setHint(R.string.date1);
        already.setText(R.string.already1);
        registerButton.setText(R.string.register1);
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        TDate.setText(sdf.format(myCalendar.getTime()));
    }
}