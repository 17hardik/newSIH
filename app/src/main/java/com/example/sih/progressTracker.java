package com.example.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class progressTracker extends AppCompatActivity {

    private EditText etPercent;
    private ClipDrawable mImageDrawable;

    private int mlevel = 0;
    private int fromLevel = 0;
    private int toLevel = 0;

    public static final int MAX_LEVEL = 10000;
    public static final int LEVEL_DIFF = 100;
    public static final int DELAY = 30;

    private Handler mUpHandler = new Handler();
    private Runnable animateUpImage = new Runnable() {
        @Override
        public void run() {
            doTheUpAnimation(fromLevel, toLevel);
        }
    };

    private  Handler mDownHandler = new Handler();
    private  Runnable animateDownImage = new Runnable() {
        @Override
        public void run() {
            doTheDownAnimation(fromLevel, toLevel);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_tracker);

        etPercent = (EditText) findViewById(R.id.etPercent);

        ImageView img = (ImageView) findViewById(R.id.imageView1);
        mImageDrawable = (ClipDrawable) img.getDrawable();
        mImageDrawable.setLevel(0);
    }

    private  void  doTheUpAnimation(int fromLevel, int toLevel){
        mlevel += LEVEL_DIFF;
        mImageDrawable.setLevel(mlevel);
        if(mlevel <= toLevel){
            mUpHandler.postDelayed(animateUpImage, DELAY);
        }else{
            mUpHandler.removeCallbacks(animateUpImage);
            progressTracker.this.fromLevel = toLevel;
        }
    }

    private void doTheDownAnimation(int fromLevel, int toLevel){
        mlevel -= LEVEL_DIFF;
        mImageDrawable.setLevel(mlevel);
        if(mlevel >= toLevel){
            mDownHandler.postDelayed(animateDownImage, DELAY);
        }else{
            mDownHandler.removeCallbacks(animateDownImage);
            progressTracker.this.fromLevel = toLevel;
        }
    }

    public void onClickOk(View v){

        int temp_level = ((Integer.parseInt(etPercent.getText().toString()))*MAX_LEVEL)/100;
        if (toLevel == temp_level || temp_level > MAX_LEVEL){
            return;
        }
        toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
        if(toLevel > fromLevel){
            mDownHandler.removeCallbacks(animateDownImage);
            progressTracker.this.fromLevel = toLevel;

            mUpHandler.post(animateUpImage);
        }else{
            mUpHandler.removeCallbacks(animateUpImage);
            progressTracker.this.fromLevel = toLevel;

            mDownHandler.post(animateDownImage);
        }

    }
}