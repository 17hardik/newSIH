package com.example.sih.Jobs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sih.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class topJobsFragment extends AppCompatActivity {

    ListView domainsList;
    ArrayList<String> arrlist;
    int size;
    SearchView mySearchView;
    ArrayAdapter<String> arradapter;
    DatabaseReference reff, reff1;
    int k, j;
    Translate translate;
    String M, check;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M, j);
        check = preferences1.getString("Lang", "Eng");
        setContentView(R.layout.activity_top_jobs_fragment);
        mySearchView =  findViewById(R.id.SearchView);
        domainsList =  findViewById(R.id.domainsList);
        arrlist = new ArrayList<>();
        Firebase.setAndroidContext(this);
        pd = new ProgressDialog(topJobsFragment.this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(check.equals(getResources().getString(R.string.english))) {
            pd.setMessage(getResources().getString(R.string.loading));
            actionBar.setTitle("Top Jobs");
            pd.show();
        } else {
            mySearchView.setQueryHint("अपना क्षेत्र खोजें");
            domainsList.setVisibility(View.GONE);
            pd.setMessage(getResources().getString(R.string.loading1));
            actionBar.setTitle(getResources().getString(R.string.top_jobs1));
            pd.show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getTranslateService();
                    int size = arrlist.size();
                    Toast.makeText(topJobsFragment.this, Integer.toString(size), Toast.LENGTH_SHORT).show();
                    for(int i = 0; i < size; i++) {
                        translateToHin(arrlist.get(i), i);
                    }
                    pd.dismiss();
                    domainsList.setVisibility(View.VISIBLE);
                }
            }, 2000);

        }
        reff = FirebaseDatabase.getInstance().getReference();
        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arradapter.getFilter().filter(newText);
                return false;
            }
        });



        reff.child("Top Jobs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arradapter = new ArrayAdapter<String>(topJobsFragment.this, android.R.layout.simple_list_item_1, arrlist){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){

                        View view = super.getView(position, convertView, parent);
                        TextView tv = view.findViewById(android.R.id.text1);
                        tv.setTextSize(18);
                        tv.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                domainsList.setAdapter(arradapter);
                size = (int) dataSnapshot.getChildrenCount();
                for(k=1;k<=size;k++) {
                    String i = Integer.toString(k);
                    arrlist.add(dataSnapshot.child("Category"+i).child("Category").getValue().toString());
                    if(check.equals(getResources().getString(R.string.english))){
                        pd.dismiss();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
                if(check.equals(getResources().getString(R.string.english))){
                    Toast.makeText(topJobsFragment.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(topJobsFragment.this, getResources().getString(R.string.check_internet1), Toast.LENGTH_SHORT).show();
                }
            }
        });

        domainsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                String cat = domainsList.getItemAtPosition(position).toString();
                String[] ID = cat.split(". ");
                Intent intent1 = new Intent(topJobsFragment.this, topJobs.class);
                intent1.putExtra("CategoryNumber", ID[0]);
                startActivity(intent1);
            }
        });

    }

    public void getTranslateService() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try (InputStream is = getResources().openRawResource(R.raw.translate)) {
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void translateToHin (String originalText, int i) {
        Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage("hin"), Translate.TranslateOption.model("base"));
        arrlist.set(i, translation.getTranslatedText());
    }
}
