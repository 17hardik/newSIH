package com.example.sih.chatApp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.R;

public class chat_rec extends RecyclerView.ViewHolder  {
    TextView leftText,rightText;

    public chat_rec(View itemView){
        super(itemView);
        leftText = itemView.findViewById(R.id.leftText);
        rightText = itemView.findViewById(R.id.rightText);
    }
}