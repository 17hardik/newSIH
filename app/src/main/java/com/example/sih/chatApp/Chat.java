package com.example.sih.chatApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sih.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.smartreply.SmartReply;
import com.google.mlkit.nl.smartreply.SmartReplyGenerator;
import com.google.mlkit.nl.smartreply.SmartReplySuggestion;
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult;
import com.google.mlkit.nl.smartreply.TextMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat extends AppCompatActivity {

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    TextView Suggestion1, Suggestion2, Suggestion3;
    String S, name, chatwith, phone, username;
    int i, count = 0;
    Intent intent;
    List<TextMessage> conversation;
    Firebase reference1, reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(S,i);
        phone = preferences.getString("Phone","");
        setContentView(R.layout.activity_chat);
        intent = getIntent();
        chatwith = intent.getStringExtra("Phone");
        username = intent.getStringExtra("Username");
        layout = findViewById(R.id.layout1);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);
        Suggestion1 = findViewById(R.id.suggestion1);
        Suggestion2 = findViewById(R.id.suggestion2);
        Suggestion3 = findViewById(R.id.suggestion3);
        name = phone;
        Firebase.setAndroidContext(this);
        conversation = new ArrayList<>();
        reference1 = new Firebase("https://smart-e60d6.firebaseio.com/messages/" +name+ "_" +chatwith);
        reference2 = new Firebase("https://smart-e60d6.firebaseio.com/messages/" + chatwith + "_" + name);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    conversation.add(TextMessage.createForLocalUser(messageText, System.currentTimeMillis()));
                    smartReply();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", name);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                }
            }
        });

        Suggestion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageArea.setText(Suggestion1.getText().toString());
                Suggestion1.setVisibility(View.GONE);
                Suggestion2.setVisibility(View.GONE);
                Suggestion3.setVisibility(View.GONE);
            }
        });

        Suggestion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageArea.setText(Suggestion2.getText().toString());
                Suggestion1.setVisibility(View.GONE);
                Suggestion2.setVisibility(View.GONE);
                Suggestion3.setVisibility(View.GONE);
            }
        });

        Suggestion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageArea.setText(Suggestion3.getText().toString());
                Suggestion1.setVisibility(View.GONE);
                Suggestion2.setVisibility(View.GONE);
                Suggestion3.setVisibility(View.GONE);
            }
        });

        getSupportActionBar().setTitle(username);
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                messageArea.setText("");

                if(userName.equals(name)){
                    addMessageBox("You\n" + message, 1);
                }
                else{
                    addMessageBox(username + "\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);

        if(type == 1) {
            textView.setBackgroundResource(R.drawable.userchatbox);
            lp.setMargins(600,0,0,10);
        }
        else{
            textView.setBackgroundResource(R.drawable.chatwithchatbox);
            lp.setMargins(0,0,600,10);
        }

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
     public void smartReply(){
         SmartReplyGenerator smartReply = SmartReply.getClient();
         smartReply.suggestReplies(conversation)
                 .addOnSuccessListener(new OnSuccessListener<SmartReplySuggestionResult>() {
                     @Override
                     public void onSuccess(SmartReplySuggestionResult result) {
                         if (result.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                             // The conversation's language isn't supported, so
                             // the result doesn't contain any suggestions.

                         } else if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                             // Task completed successfully
                             // ...
                             for (SmartReplySuggestion suggestion : result.getSuggestions()) {
                                 String replyText = suggestion.getText();
                                 if(count == 0) {
                                     Suggestion1.setVisibility(View.VISIBLE);
                                     Suggestion1.setText(replyText);
                                     count++;
                                 } else if(count == 1) {
                                     Suggestion2.setVisibility(View.VISIBLE);
                                     Suggestion2.setText(replyText);
                                     count++;
                                 } else {
                                     Suggestion3.setVisibility(View.VISIBLE);
                                     Suggestion3.setText(replyText);
                                     count = 0;
                                 }
                             }
                         }
                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         // Task failed with an exception
                         // ...
                         Toast.makeText(Chat.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 });
     }
}