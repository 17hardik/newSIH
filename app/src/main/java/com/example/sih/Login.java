package com.example.sih;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigInteger;

public class Login extends AppCompatActivity {
    TextView register;
    EditText Phone, Password;
    Button loginButton;
    String phone, pass, S, Cipher, M, check, new_phone, realPhone = "Null";
    int i, j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        SharedPreferences preferences = getSharedPreferences(S,i);
        realPhone = preferences.getString("Phone","Null");
        new_phone = preferences.getString("NewPhone","Null");
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        register = findViewById(R.id.register);
        Phone = findViewById(R.id.phone);
        Password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setBackgroundResource(R.drawable.button);
        if(check.equals("Hin"))
        {
            toHin();
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone = Phone.getText().toString().trim();
                pass = Password.getText().toString().trim();
                if(new_phone.equals("Null")){
                    new_phone = phone;
                }

                if(phone.equals("")){
                    if(check.equals("Hin")){
                        Phone.setError(getResources().getString(R.string.must_be_filled1));
                    }else {
                        Phone.setError("Must be filled");
                    }
                }
                else if(pass.equals("")){
                    if(check.equals("Hin")){
                        Password.setError(getResources().getString(R.string.must_be_filled1));
                    }else {
                        Password.setError("Must be filled");
                    }
                }
                else if(!(phone.equals(new_phone))){
                    if(check.equals("Hin")){
                        Toast.makeText(Login.this, R.string.user_not_found1, Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(Login.this, "User not found", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    String url = "https://smart-e60d6.firebaseio.com/Users.json";
                    final ProgressDialog pd = new ProgressDialog(Login.this);
                    if(check.equals("Hin")){
                        pd.setMessage(getResources().getString(R.string.logging_in1));
                    }else {
                        if(check.equals("Hin")){
                            pd.setMessage(getResources().getString(R.string.logging_in1));
                        }else {
                            pd.setMessage("Logging in...");
                        }
                    }
                    pd.show();
                    if(!(realPhone.equals("Null"))) {
                        BigInteger hash = BigInteger.valueOf((realPhone.charAt(0) - '0') + (realPhone.charAt(2) - '0') + (realPhone.charAt(4) - '0') + (realPhone.charAt(6) - '0') + (realPhone.charAt(8) - '0'));
                        StringBuilder sb = new StringBuilder();
                        char[] letters = pass.toCharArray();

                        for (char ch : letters) {
                            sb.append((byte) ch);
                        }
                        String a = sb.toString();
                        BigInteger temp = new BigInteger(a);
                        hash = temp.multiply(hash);
                        Cipher = String.valueOf(hash);
                    } else{
                        BigInteger hash = BigInteger.valueOf((phone.charAt(0) - '0') + (phone.charAt(2) - '0') + (phone.charAt(4) - '0') + (phone.charAt(6) - '0') + (phone.charAt(8) - '0'));
                        StringBuilder sb = new StringBuilder();
                        char[] letters = pass.toCharArray();

                        for (char ch : letters) {
                            sb.append((byte) ch);
                        }
                        String a = sb.toString();
                        BigInteger temp = new BigInteger(a);
                        hash = temp.multiply(hash);
                        Cipher = String.valueOf(hash);
                    }
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            if(s.equals("null")){
                                if(check.equals("Hin")){
                                    Toast.makeText(Login.this, R.string.user_not_found1, Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(Login.this, "User not found", Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if(realPhone.equals("Null") && obj.getJSONObject(phone).getString("Password").equals(Cipher)){
                                        SharedPreferences.Editor editor = getSharedPreferences(S,i).edit();
                                        editor.putString("Status", "Yes");
                                        editor.putString("Phone", phone);
                                        editor.apply();
                                        startActivity(new Intent(Login.this, MainActivity.class));
                                        finish();
                                    }

                                   else if(obj.getJSONObject(realPhone).getString("Password").equals(Cipher)){
                                        SharedPreferences.Editor editor = getSharedPreferences(S,i).edit();
                                        editor.putString("Status", "Yes");
                                        editor.putString("Phone", realPhone);
                                        editor.apply();
                                        startActivity(new Intent(Login.this, MainActivity.class));
                                        finish();
                                    }

                                    else if(!obj.has(realPhone)){
                                        if(check.equals("Hin")){
                                            Toast.makeText(Login.this, R.string.user_not_found1, Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(Login.this, "User not found", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    else {
                                        if(check.equals("Hin")){
                                            Toast.makeText(Login.this, R.string.incorrect_password1, Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                    rQueue.add(request);
                }

            }
        });
    }

    public void toHin(){
        register.setText(R.string.register1);
        loginButton.setText(R.string.login1);
        Phone.setHint(R.string.phone1);
        Password.setHint(R.string.password1);
    }

}