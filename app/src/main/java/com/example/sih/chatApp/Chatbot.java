package com.example.sih.chatApp;

import com.google.firebase.database.FirebaseDatabase;

public class Chatbot extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}