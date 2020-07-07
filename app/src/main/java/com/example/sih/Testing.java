package com.example.sih;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Testing extends AppCompatActivity {

    Button PremiumButton;
    final int UPI_PAYMENT = 0;
    String check, M, phone, S, formattedDate, isPremium;
    int i, j;
    TextView Title, Title2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        SharedPreferences preferences = getSharedPreferences(S,i);
        phone = preferences.getString("Phone","");
        isPremium = preferences.getString("isPremium", "No");
        setContentView(R.layout.activity_testing);

        Title = findViewById(R.id.title);
        Title2 = findViewById(R.id.title2);
        PremiumButton = findViewById(R.id.premiumButton);

        if(isPremium.equals("Yes")){
            Title.setText("Congratulations!");
            Title2.setText("You are already a premium member");
            PremiumButton.setText("Go to dashboard");
        }
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(c);
        PremiumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPremium.equals("Yes")){
                    Intent intent = new Intent(Testing.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Payment("Rojgar Premium", "himanshugangil1999@oksbi",
                            "Monthly Premium", "96.00");
                }
            }
        });
    }

    public void Payment(  String name,String upiId, String note, String amount) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay using");
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(Testing.this,"No UPI app found, please an UPI app to continue",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String response = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + response);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(response);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(Testing.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Firebase reference = new Firebase("https://smart-e60d6.firebaseio.com/Users");
                reference.child(phone).child("Premium Date").setValue(formattedDate);
                SharedPreferences.Editor editor = getSharedPreferences(S,i).edit();
                editor.putString("isPremium", "Yes");
                editor.apply();
                Intent intent = new Intent(Testing.this, MainActivity.class);
                startActivity(intent);
                if(check.equals("Hin")){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Testing.this)
                            .setSmallIcon(R.drawable.logo)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                            .setContentTitle(getResources().getString(R.string.congrats))
                            .setContentText(getResources().getString(R.string.premium_member));
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1, builder.build());
                    Toast.makeText(Testing.this, getResources().getString(R.string.congrats_premium), Toast.LENGTH_LONG).show();
                }else {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Testing.this)
                            .setSmallIcon(R.drawable.logo)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                            .setContentTitle("Congratulations!")
                            .setContentText("You are now a premium member");
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1, builder.build());
                    Toast.makeText(Testing.this, "Congratulations!, You are now a premium member", Toast.LENGTH_LONG).show();
                }
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(Testing.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(Testing.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(Testing.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

}


