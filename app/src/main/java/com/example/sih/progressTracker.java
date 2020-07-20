package com.example.sih;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class progressTracker extends AppCompatActivity {

    TextView keySkills;

    Button reset;

    String S, jobCategory, jobReference, skills, phone, J, status;

    int i,x;

    DatabaseReference reff1, reff2;

    List<dataListView> initItemList;
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

        SharedPreferences preferences = getSharedPreferences(S,i);
        phone= preferences.getString("Phone","");

        SharedPreferences preferences2 = getSharedPreferences(J,x);
        status = preferences2.getString("progress","");

        SharedPreferences preferences1 = getSharedPreferences(J,x);
        jobCategory = preferences1.getString("jobCategory", "");
        jobReference = preferences1.getString("jobReference", "");

        setContentView(R.layout.activity_progress_tracker);

        keySkills = findViewById(R.id.keySkills);

        reset = findViewById(R.id.reset);

        data = new ArrayList<>();

        ImageView img = (ImageView) findViewById(R.id.imageView1);
        mImageDrawable = (ClipDrawable) img.getDrawable();
        mImageDrawable.setLevel(0);

        setTitle("Roadmap");

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(progressTracker.this, progressTracker.class);
                startActivity(intent);
                finish();
                SharedPreferences.Editor editor = getSharedPreferences(J, x).edit();
                editor.putString("progress", "0");
                editor.apply();

            }
        });

        reff1 = FirebaseDatabase.getInstance().getReference().child("Jobs").child(jobCategory).child(jobReference);
        reff1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    skills = dataSnapshot.child("Key_Skills").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                keySkills.setText(skills);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(progressTracker.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

            }
        });

        // Initialized elements of activity_checkbox_listview
        LayoutInflater inflater = getLayoutInflater();
        View myView = inflater.inflate(R.layout.activity_checkbox_listview, null);
        final CheckBox listItemCheckbox = (CheckBox) myView.findViewById(R.id.list_view_item_checkbox);
        final TextView listItemText = (TextView) myView.findViewById(R.id.list_view_item_text);

        final ListView listViewWithCheckbox= (ListView) findViewById(R.id.steps);

        // Initiate listview data.
        getInitViewItemDtoList();

        // Create a custom list view adapter with checkbox control.
        final adapterListView listViewDataAdapter = new adapterListView(getApplicationContext(), initItemList);

        listViewDataAdapter.notifyDataSetChanged();

        // Set data adapter to list view.
        listViewWithCheckbox.setAdapter((ListAdapter) listViewDataAdapter);

//        if (status.equals("1")){
//
//            int temp_level = (1 * MAX_LEVEL) / n;
//            if (toLevel == temp_level || temp_level > MAX_LEVEL) {
//                return;
//            }
//            toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
//            if (toLevel > fromLevel) {
//                mDownHandler.removeCallbacks(animateDownImage);
//                progressTracker.this.fromLevel = toLevel;
//
//                mUpHandler.post(animateUpImage);
//
//            }
//        }
//
//        if (status.equals("2")){
//
//            int temp_level = (2 * MAX_LEVEL) / n;
//            if (toLevel == temp_level || temp_level > MAX_LEVEL) {
//                return;
//            }
//            toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
//            if (toLevel > fromLevel) {
//                mDownHandler.removeCallbacks(animateDownImage);
//                progressTracker.this.fromLevel = toLevel;
//
//                mUpHandler.post(animateUpImage);
//
//            }
//        }
//
//        if (status.equals("3")){
//
//            int temp_level = (3 * MAX_LEVEL) / n;
//            if (toLevel == temp_level || temp_level > MAX_LEVEL) {
//                return;
//            }
//            toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
//            if (toLevel > fromLevel) {
//                mDownHandler.removeCallbacks(animateDownImage);
//                progressTracker.this.fromLevel = toLevel;
//
//                mUpHandler.post(animateUpImage);
//
//            }
//        }
//
//        if (status.equals("4")){
//
//            int temp_level = (4 * MAX_LEVEL) / n;
//            if (toLevel == temp_level || temp_level > MAX_LEVEL) {
//                return;
//            }
//            toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
//            if (toLevel > fromLevel) {
//                mDownHandler.removeCallbacks(animateDownImage);
//                progressTracker.this.fromLevel = toLevel;
//
//                mUpHandler.post(animateUpImage);
//
//            }
//        }
//
//        if (status.equals("5")){
//
//            int temp_level = (5 * MAX_LEVEL) / n;
//            if (toLevel == temp_level || temp_level > MAX_LEVEL) {
//                return;
//            }
//            toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
//            if (toLevel > fromLevel) {
//                mDownHandler.removeCallbacks(animateDownImage);
//                progressTracker.this.fromLevel = toLevel;
//
//                mUpHandler.post(animateUpImage);
//
//            }
//        }
//
//        if (status.equals("6")){
//
//            int temp_level = (6 * MAX_LEVEL) / n;
//            if (toLevel == temp_level || temp_level > MAX_LEVEL) {
//                return;
//            }
//            toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
//            if (toLevel > fromLevel) {
//                mDownHandler.removeCallbacks(animateDownImage);
//                progressTracker.this.fromLevel = toLevel;
//
//                mUpHandler.post(animateUpImage);
//
//            }
//        }
//
//        if (status.equals("7")){
//
//            int temp_level = (7 * MAX_LEVEL) / n;
//            if (toLevel == temp_level || temp_level > MAX_LEVEL) {
//                return;
//            }
//            toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
//            if (toLevel > fromLevel) {
//                mDownHandler.removeCallbacks(animateDownImage);
//                progressTracker.this.fromLevel = toLevel;
//
//                mUpHandler.post(animateUpImage);
//
//            }
//        }
//
//        if (status.equals("8")){
//
//            int temp_level = (8 * MAX_LEVEL) / n;
//            if (toLevel == temp_level || temp_level > MAX_LEVEL) {
//                return;
//            }
//            toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
//            if (toLevel > fromLevel) {
//                mDownHandler.removeCallbacks(animateDownImage);
//                progressTracker.this.fromLevel = toLevel;
//
//                mUpHandler.post(animateUpImage);
//
//            }
//        }

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

                    if (text.contains("1") || status.equals("1")){

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
                        SharedPreferences.Editor editor = getSharedPreferences(J, x).edit();
                        editor.putString("progress", "1");
                        editor.apply();

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

                        SharedPreferences.Editor editor = getSharedPreferences(J, x).edit();
                        editor.putString("progress", "2");
                        editor.apply();
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

                        SharedPreferences.Editor editor = getSharedPreferences(J, x).edit();
                        editor.putString("progress", "3");
                        editor.apply();
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

                        SharedPreferences.Editor editor = getSharedPreferences(J, x).edit();
                        editor.putString("progress", "4");
                        editor.apply();
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

                        SharedPreferences.Editor editor = getSharedPreferences(J, x).edit();
                        editor.putString("progress", "5");
                        editor.apply();
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

                        SharedPreferences.Editor editor = getSharedPreferences(J, x).edit();
                        editor.putString("progress", "6");
                        editor.apply();
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

                        SharedPreferences.Editor editor = getSharedPreferences(J, x).edit();
                        editor.putString("progress", "7");
                        editor.apply();
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

                        SharedPreferences.Editor editor = getSharedPreferences(J, x).edit();
                        editor.putString("progress", "8");
                        editor.apply();
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

    private void getInitViewItemDtoList()
    {

        final ListView listViewWithCheckbox= (ListView) findViewById(R.id.steps);

        String check, M = null;

        int j = 0;

        final ProgressDialog pd;

        DatabaseReference reff;

        SharedPreferences preferences1 = getSharedPreferences(M, j);
        check = preferences1.getString("Lang", "Eng");


        pd = new ProgressDialog(progressTracker.this);

        if (check.equals("Eng")) {
            pd.setMessage("Fetching data");
        } else {
            pd.setMessage("डेटा लाया जा रहा है");
        }

        pd.show();

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
                    dataListView item = new dataListView();
                    item.setChecked(false);
                    item.setItemText(itemText);
                    list.add(item);

                }

                initItemList = list;
                final adapterListView listViewDataAdapter = new adapterListView(getApplicationContext(), initItemList);
                listViewDataAdapter.notifyDataSetChanged();
                listViewWithCheckbox.setAdapter((ListAdapter) listViewDataAdapter);
                pd.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(progressTracker.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

}