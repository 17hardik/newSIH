package com.example.sih;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class jobDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText title, description, skills, city, sector, salary, qualification;
    Button post;
    DatabaseReference reff, reff2;
    details details;
    Spinner jobType;
    int i;
    String JobType, Salary, Company;
    String phone, S;
    Boolean isAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(S,i);
        phone = preferences1.getString("Phone","");
        setContentView(R.layout.activity_job_details);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        skills = findViewById(R.id.skills);
        city = findViewById(R.id.city);
        sector = findViewById(R.id.sector);
        salary = findViewById(R.id.salary);
        qualification = findViewById(R.id.qualification);
        post = findViewById(R.id.button4);
        jobType = findViewById(R.id.jobtype);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.jobtype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobType.setAdapter(adapter);
        jobType.setOnItemSelectedListener(this);
        details = new details();
        reff = FirebaseDatabase.getInstance().getReference();
        reff2 = FirebaseDatabase.getInstance().getReference();

        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Company = snapshot.child("Users").child(phone).child("Company").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(jobDetails.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                details.setTitle(title.getText().toString().trim());
                details.setDescription(description.getText().toString().trim());
                details.setSkills(skills.getText().toString().trim());
                details.setCity(city.getText().toString().trim());
                details.setSector(sector.getText().toString().trim());

                try{

                    if (title.getText().toString().trim().equals("")) {
                        title.setError("Must be Filled");
                        title.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (description.getText().toString().trim().equals("")) {
                        description.setError("Must be Filled");
                        description.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (description.getText().toString().trim().length() < 30){
                        description.setError("At least 30 characters must be there");
                        description.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (Company.equals("")){
                        Toast.makeText(jobDetails.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }

                    else if (skills.getText().toString().trim().equals("")) {
                        skills.setError("Must be Filled");
                        skills.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (qualification.getText().toString().trim().equals("")) {
                        skills.setError("Must be Filled");
                        skills.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if ((skills.getText().toString().contains(" ")) && !(skills.getText().toString().contains(","))) {
                        skills.setError("Please write in proper format");
                        skills.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (city.getText().toString().trim().equals("")) {
                        city.setError("Must be Filled");
                        city.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else  if (sector.getText().toString().trim().equals("")) {
                        sector.setError("Must be Filled");
                        sector.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if((JobType.equals("Select Job Type") || JobType.equals("नौकरी के प्रकार का चयन करें"))){
                        Toast.makeText(jobDetails.this, "Please select Job Type", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        if(salary.getText().toString().equals("")){
                            Salary = "Not Disclosed";
                        } else{
                            Salary = salary.getText().toString().trim();
                        }
                        reff.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Toast.makeText(jobDetails.this, JobType, Toast.LENGTH_SHORT).show();
                                long count = snapshot.child("Jobs").child(JobType).getChildrenCount();
                                String children = Long.toString(count);
                                Toast.makeText(jobDetails.this, children, Toast.LENGTH_SHORT).show();
                                if(!isAdded) {
                                    reff.child("Jobs").child(JobType).child(children).child("Company_Name").setValue(Company);
                                    reff.child("Jobs").child(JobType).child(children).child("ID").setValue(children);
                                    reff.child("Jobs").child(JobType).child(children).child("Job_Description").setValue(description.getText().toString().trim());
                                    reff.child("Jobs").child(JobType).child(children).child("Job_Post").setValue(title.getText().toString().trim());
                                    reff.child("Jobs").child(JobType).child(children).child("Job_Type").setValue(JobType);
                                    reff.child("Jobs").child(JobType).child(children).child("Location").setValue(city.getText().toString().trim());
                                    reff.child("Jobs").child(JobType).child(children).child("Key_Skills").setValue(skills.getText().toString().trim());
                                    reff.child("Jobs").child(JobType).child(children).child("Qualification").setValue(qualification.getText().toString().trim());
                                    reff.child("Jobs").child(JobType).child(children).child("Sector").setValue(sector.getText().toString().trim());
                                    reff.child("Jobs").child(JobType).child(children).child("Salary_PA_in_Rs").setValue(Salary);
                                    reff.child("Users").child("Job Post").child(phone).child(title.getText().toString().trim()).setValue(details);
                                    isAdded = true;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
//                        reff.child("Job Post").child(phone).child(title.getText().toString().trim()).setValue(details);
//                        Toast.makeText(jobDetails.this, "Uploaded Job Details Successfully",Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(jobDetails.this, jobsPublished.class);
//                        startActivity(intent);
//                        finish();
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        JobType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
