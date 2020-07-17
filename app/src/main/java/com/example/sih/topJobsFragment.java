package com.example.sih;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class topJobsFragment extends AppCompatActivity {

    ListView domainsList;
    ArrayList<String> arrlist;
    int size;
    SearchView mySearchView;
    ArrayAdapter<String> arradapter;
    ArrayList<forID> details;
    DatabaseReference reff, reff1;
    int j;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_jobs_fragment);

        mySearchView =  findViewById(R.id.SearchView);
        domainsList =  findViewById(R.id.domainsList);
        arrlist = new ArrayList<>();
        details = new ArrayList<>();
        Firebase.setAndroidContext(this);
        pd = new ProgressDialog(topJobsFragment.this);
        pd.setMessage("Loading...");
        pd.show();
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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Top Jobs");

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

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    forID id = dataSnapshot1.getValue(forID.class);
                    details.add(id);

                }

                for(j=1;j<=size;j++) {
                    String i = Integer.toString(j);
                    arrlist.add(dataSnapshot.child("Category"+i).child("Category").getValue().toString());
                    pd.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
                Toast.makeText(topJobsFragment.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        domainsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                String cat = domainsList.getItemAtPosition(position).toString();
                String[] ID = cat.split(". ");
                Intent intent1 = new Intent(topJobsFragment.this, topJobs.class);
                String id = details.get(position).getId();
                intent1.putExtra("CategoryNumber", ID[0]);
                startActivity(intent1);
            }
        });

    }
}

class forID {

    private String id;

    public forID() {
    }

    public forID(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

