package com.example.sih.Registration;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {


    }
    @Override
    // This will execute when new token will be created by either re-installing the app or changing the version
    public void onNewToken(String token) {
        // will do something here too
    }
}