package com.example.sih.PublishJob;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sih.R;
import com.example.sih.details;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class jobDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText title, description, skills, city, sector, salary, qualification;
    Button post;
    TextView Title;
    DatabaseReference reff, reff2;
    com.example.sih.details details;
    Spinner jobType;
    int i, j;
    String JobType, Salary, Company, check, M;
    String phone, S;
    Boolean isAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(S,i);
        phone = preferences1.getString("Phone","");
        SharedPreferences preferences2 = getSharedPreferences(M,j);
        check = preferences2.getString("Lang","Eng");
        setContentView(R.layout.activity_job_details);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        skills = findViewById(R.id.skills);
        city = findViewById(R.id.city);
        sector = findViewById(R.id.sector);
        salary = findViewById(R.id.salary);
        Title = findViewById(R.id.textView1);
        qualification = findViewById(R.id.qualification);
        post = findViewById(R.id.button4);
        jobType = findViewById(R.id.jobtype);
        if(check.equals(getResources().getString(R.string.english))) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.jobtype, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            jobType.setAdapter(adapter);
        } else {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.jobtype1, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            jobType.setAdapter(adapter);
        }
        jobType.setOnItemSelectedListener(this);
        details = new details();
        reff = FirebaseDatabase.getInstance().getReference();
        reff2 = FirebaseDatabase.getInstance().getReference();
        if(!check.equals(getResources().getString(R.string.english))){
            toHin();
        }

        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Company = snapshot.child("Users").child(phone).child("Company").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(check.equals(getResources().getString(R.string.english))) {
                    Toast.makeText(jobDetails.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(jobDetails.this, getResources().getString(R.string.check_internet1), Toast.LENGTH_SHORT).show();
                }
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
                        if(check.equals(getResources().getString(R.string.english))) {
                            title.setError("Must be Filled");
                        } else {
                            title.setError(getResources().getString(R.string.must_be_filled1));
                        }
                        title.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (description.getText().toString().trim().equals("")) {
                        if(check.equals(getResources().getString(R.string.english))) {
                            description.setError("Must be Filled");
                        } else {
                            description.setError(getResources().getString(R.string.must_be_filled1));
                        }
                        description.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (description.getText().toString().trim().length() < 30){
                        if(check.equals(getResources().getString(R.string.english))){
                            description.setError("At least 30 characters must be there");
                        } else {
                            description.setError("कम से कम 30 अक्षर होने चाहिए");
                        }
                        description.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (Company.equals("")){
                        if(check.equals(getResources().getString(R.string.english))) {
                            Toast.makeText(jobDetails.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(jobDetails.this, getResources().getString(R.string.check_internet1), Toast.LENGTH_SHORT).show();
                        }                    }

                    else if (skills.getText().toString().trim().equals("")) {
                        if(check.equals(getResources().getString(R.string.english))) {
                            skills.setError("Must be Filled");
                        } else {
                            skills.setError(getResources().getString(R.string.must_be_filled1));
                        }
                        skills.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (qualification.getText().toString().trim().equals("")) {
                        if(check.equals(getResources().getString(R.string.english))) {
                            qualification.setError("Must be Filled");
                        } else {
                            qualification.setError(getResources().getString(R.string.must_be_filled1));
                        }
                        skills.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if ((skills.getText().toString().contains(" ")) && !(skills.getText().toString().contains(","))) {
                        if(check.equals(getResources().getString(R.string.english))) {
                            skills.setError("Please write in proper format");
                        } else {
                            skills.setError("कृपया उचित प्रारूप में लिखें");
                        }
                        skills.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if (city.getText().toString().trim().equals("")) {
                        if(check.equals(getResources().getString(R.string.english))) {
                            city.setError("Must be Filled");
                        } else {
                            city.setError(getResources().getString(R.string.must_be_filled1));
                        }
                        city.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else  if (sector.getText().toString().trim().equals("")) {
                        if(check.equals(getResources().getString(R.string.english))) {
                            sector.setError("Must be Filled");
                        } else {
                            sector.setError(getResources().getString(R.string.must_be_filled1));
                        }
                        sector.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
                    }

                    else if((JobType.equals("Select Job Type") || JobType.equals("नौकरी के प्रकार का चयन करें"))){
                        if(check.equals(getResources().getString(R.string.english))){
                            Toast.makeText(jobDetails.this, "Please select Job Type", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(jobDetails.this, "नौकरी के प्रकार का चयन करें", Toast.LENGTH_SHORT).show();
                        }
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
    public void toHin(){
        title.setHint("नौकरी का नाम");
        sector.setHint("नौकरी का क्षेत्र");
        description.setHint("नौकरी का विवरण> 30 अक्षर");
        skills.setHint("स्किल की आवश्यकता (स्किल 1, स्किल 2)");
        qualification.setHint("आवश्यक योग्यता");
        city.setHint("रिपोर्टिंग शहर");
        salary.setHint("प्रति वर्ष वेतन (वैकल्पिक)");
        post.setText("अपलोड और पोस्ट करें");
        Title.setText("नौकरी का विवरण दर्ज करें");
    }
}
