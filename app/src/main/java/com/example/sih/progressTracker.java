package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class progressTracker extends AppCompatActivity {

    TextView keySkills;

    ArrayList<String> data = new ArrayList<>();
    ArrayList<dataListView> list = new ArrayList<>();

    private ClipDrawable mImageDrawable;

    private int n = 5;

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

        keySkills = findViewById(R.id.keySkills);

        ImageView img = (ImageView) findViewById(R.id.imageView1);
        mImageDrawable = (ClipDrawable) img.getDrawable();
        mImageDrawable.setLevel(0);

        setTitle("Roadmap");

        // Get listview checkbox.
        final ListView listViewWithCheckbox= (ListView) findViewById(R.id.steps);

        // Initiate listview data.
        final List<dataListView> initItemList = this.getInitViewItemDtoList();

        // Create a custom list view adapter with checkbox control.
        final adapterListView listViewDataAdapter = new adapterListView(getApplicationContext(), initItemList);

        listViewDataAdapter.notifyDataSetChanged();

        // Set data adapter to list view.
        listViewWithCheckbox.setAdapter((ListAdapter) listViewDataAdapter);

        // When list view item is clicked.
        listViewWithCheckbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {

                // Get user selected item.
                Object itemObject = adapterView.getAdapter().getItem(itemIndex);

                // Translate the selected item to DTO object.
                dataListView item = (dataListView) itemObject;

                // Get the checkbox.
                CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);

                // Reverse the checkbox and clicked item check state.

                String text= item.getItemText();

                if(item.isChecked())
                {
                    itemCheckbox.setChecked(false);
                    item.setChecked(false);
                }else
                {
                    final AlertDialog alertDialog1 = new AlertDialog.Builder(

                            progressTracker.this).create();

                    alertDialog1.setTitle("CONGRATULATIONS!!!");

                    alertDialog1.setMessage("That's GOOD, you've taken your first step into a LARGER world");

                    alertDialog1.setIcon(R.drawable.ic_completed_24);


                    alertDialog1.setButton(Dialog.BUTTON_POSITIVE,"Proceed",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog1.dismiss();
                        }
                    });

                    alertDialog1.show();

                    if (text.contains("1")){

                        int temp_level = (1 * MAX_LEVEL) / n;
                        if (toLevel == temp_level || temp_level > MAX_LEVEL) {
                            return;
                        }
                        toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
                        if (toLevel > fromLevel) {
                            mDownHandler.removeCallbacks(animateDownImage);
                            progressTracker.this.fromLevel = toLevel;

                            mUpHandler.post(animateUpImage);

                        }

                        alertDialog1.setMessage("That's GOOD, you've taken your first step into a LARGER world");

                    }

                    else if (text.contains("2")){

                        int temp_level = (2 * MAX_LEVEL) / n;
                        if (toLevel == temp_level || temp_level > MAX_LEVEL) {
                            return;
                        }
                        toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
                        if (toLevel > fromLevel) {
                            mDownHandler.removeCallbacks(animateDownImage);
                            progressTracker.this.fromLevel = toLevel;

                            mUpHandler.post(animateUpImage);
                        }

                        alertDialog1.setMessage("You have SUCCESSFULLY completed STEP 2");

                    }

                    else if (text.contains("3")){

                        int temp_level = (3 * MAX_LEVEL) / n;
                        if (toLevel == temp_level || temp_level > MAX_LEVEL) {
                            return;
                        }
                        toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
                        if (toLevel > fromLevel) {
                            mDownHandler.removeCallbacks(animateDownImage);
                            progressTracker.this.fromLevel = toLevel;

                            mUpHandler.post(animateUpImage);
                        }

                        alertDialog1.setMessage("You have SUCCESSFULLY completed STEP 3");

                    }

                    else if (text.contains("4")){

                        int temp_level = (4 * MAX_LEVEL) / n;
                        if (toLevel == temp_level || temp_level > MAX_LEVEL) {
                            return;
                        }
                        toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
                        if (toLevel > fromLevel) {
                            mDownHandler.removeCallbacks(animateDownImage);
                            progressTracker.this.fromLevel = toLevel;

                            mUpHandler.post(animateUpImage);
                        }

                        alertDialog1.setMessage("You have SUCCESSFULLY completed STEP 4");

                    }

                    else if (text.contains("5")){

                        int temp_level = (5 * MAX_LEVEL) / n;
                        if (toLevel == temp_level || temp_level > MAX_LEVEL) {
                            return;
                        }
                        toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
                        if (toLevel > fromLevel) {
                            mDownHandler.removeCallbacks(animateDownImage);
                            progressTracker.this.fromLevel = toLevel;

                            mUpHandler.post(animateUpImage);
                        }

                        alertDialog1.setMessage("You have SUCCESSFULLY completed STEP 5");

                    }

                    else if (text.contains("6")){

                        int temp_level = (6 * MAX_LEVEL) / n;
                        if (toLevel == temp_level || temp_level > MAX_LEVEL) {
                            return;
                        }
                        toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
                        if (toLevel > fromLevel) {
                            mDownHandler.removeCallbacks(animateDownImage);
                            progressTracker.this.fromLevel = toLevel;

                            mUpHandler.post(animateUpImage);
                        }

                        alertDialog1.setMessage("You have SUCCESSFULLY completed STEP 6");

                    }

                    else if (text.contains("7")){

                        int temp_level = (7 * MAX_LEVEL) / n;
                        if (toLevel == temp_level || temp_level > MAX_LEVEL) {
                            return;
                        }
                        toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
                        if (toLevel > fromLevel) {
                            mDownHandler.removeCallbacks(animateDownImage);
                            progressTracker.this.fromLevel = toLevel;

                            mUpHandler.post(animateUpImage);
                        }

                        alertDialog1.setMessage("You have SUCCESSFULLY completed STEP 7");

                    }

                    else if (text.contains("8")){

                        int temp_level = (8 * MAX_LEVEL) / n;
                        if (toLevel == temp_level || temp_level > MAX_LEVEL) {
                            return;
                        }
                        toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
                        if (toLevel > fromLevel) {
                            mDownHandler.removeCallbacks(animateDownImage);
                            progressTracker.this.fromLevel = toLevel;

                            mUpHandler.post(animateUpImage);
                        }

                        alertDialog1.setMessage("You have SUCCESSFULLY completed STEP 8");

                    }

                    itemCheckbox.setChecked(true);
                    item.setChecked(false);
                }

            }
        });

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

    private  ArrayList<dataListView> getInitViewItemDtoList()
    {
//        String[] itemTextArr = {"Step1: - Attain specialization in the key skills mentioned above", "Step2: - Fulfill all the requirements specicified by the organization", "Step3: - Register for the Job on the organization's website", "Step4: - Try to know the whole interview process and start preparing for it", "Step5 : - Work on your communication skills in order to excell in interview"};

        DatabaseReference reff;

        reff = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("Science and Technology").child("Government").child("0").child("ROADMAP");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               int size = (int) snapshot.getChildrenCount();

                for(int j=1;j<=size;j++) {

                    String k = Integer.toString(j);
                    data.add(snapshot.child("Step"+k).child("Step").getValue().toString());

                }

                String[] arr = new String[data.size()];

                for (int l = 0; l < data.size(); l++){

                    arr[l] = data.get(l);
                    String itemText = arr[l];
//                    Toast.makeText(progressTracker.this, itemText , Toast.LENGTH_SHORT).show();
                    dataListView item = new dataListView();
                    item.setChecked(false);
                    item.setItemText(itemText);
                    list.add(item);

//                    Toast.makeText(progressTracker.this, "" + list, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(progressTracker.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });


//        List<dataListView> ret = new ArrayList<dataListView>();




//        int length = itemTextArr.length;
//
//        for(int i=0;i<length;i++)
//        {
//            String itemText = itemTextArr[i];
//
//            dataListView dto = new dataListView();
//            dto.setChecked(false);
//            dto.setItemText(itemText);
//
//            list.add(dto);
//
//            Object[] s  = list.toArray();
//
//            for(int x = 0; x < s.length ; x++){
//                keySkills.setText(s[i].toString());
//            }
//
//        }
        Toast.makeText(progressTracker.this, "" + list, Toast.LENGTH_SHORT).show();
        return list ;
    }

}