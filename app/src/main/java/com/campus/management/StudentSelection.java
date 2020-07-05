package com.campus.management;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentSelection extends AppCompatActivity {

    private LinearLayout header, addfeelyout;
    private RecyclerView recyclerView;
    private TextView viewStudents, toolbar;
    private CardView addfee, viewfee, feeStatus;
    private Spinner dropdown;
    String tag;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private ArrayList<Student> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_selection);

        database= FirebaseDatabase.getInstance();
        //ref=database.getReference().child("Data").child("Students");
        itemList=new ArrayList<>();

        addfee=findViewById(R.id.selection_add_fee_id);
        feeStatus=findViewById(R.id.selection_view_feestatus_id);
        viewfee=findViewById(R.id.selection_view_fee_id);
        addfeelyout=findViewById(R.id.add_fee_layout);
        dropdown = findViewById(R.id.selection_spinner_id);
        header=findViewById(R.id.selection_hearder_id);
        recyclerView=findViewById(R.id.selection_recycler_id);
        viewStudents=findViewById(R.id.selection_search_id);
        toolbar=findViewById(R.id.selection_toolbar_id);
        SharedPreferences sharedPreferences=getSharedPreferences("myPref", MODE_PRIVATE);
        String campusName=sharedPreferences.getString("campus","");

        toolbar.setText("Fee: "+campusName);

        final String campus=Campus.getCampusName();

        if(campus.equalsIgnoreCase("National Institute of Education and Management")) {
            String[] classes = new String[]{"M.A Urdu", "M.A eng", "Msc Math", "MCS", "B.ed", "ADE", "BS sports science",
                    "BS eng", "BS math", "BS computer science", "BS islamic studies", "BBA", "DVAS 2years", "DVAS 3years"};
            ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
            dropdown.setAdapter(classAdopter);

        }else if(campus.equalsIgnoreCase("Fatima Academy")){
            String[] classes = new String[]{"7th Male","8th Male","9th Male","10th Male","Fsc1 Male","Fsc11 Male","Bsc Male","7th Female",
                    "8th Female","9th Female","10th Female","Fsc1 Female","Fsc11 Female","Bsc Female"};
            ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
            dropdown.setAdapter(classAdopter);

        }else if(campus.equalsIgnoreCase("The Knowledge school")){
            String[] classes = new String[]{"PG","Nursery", "Prep", "One", "Two", "Three", "4th", "5th", "6th boys", "6th girls",
                    "7th boys", "7th girls", "8th boys", "8th girls", "9th boys", "9th girls","10th boys", "10th girls"};
            ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
            dropdown.setAdapter(classAdopter);

        }else if(campus.equalsIgnoreCase("School of Nursing Education Bhakkar")){
            String[] classes = new String[]{"RN","RM","LHV","CMW","CNA","LPA","FWW"};
            ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
            dropdown.setAdapter(classAdopter);
        }else if(campus.equalsIgnoreCase("GU Campus Bhakkar")) {
            String[] classes = new String[]{"M.A Urdu", "M.A eng", "Msc Math", "MCS", "B.ed", "ADE", "BS sports science",
                    "BS eng", "BS math", "BS computer science", "BS islamic studies", "BBA", "DVAS 2years", "DVAS 3years"};
            ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
            dropdown.setAdapter(classAdopter);

        }
//create a list of items for the spinner.
        /*String[] items = new String[]{"M.A Urdu", "M.A eng", "Msc Math", "MCS", "B.ed", "ADE", "BS sports science",
                "BS eng", "BS math", "BS computer science", "BS islamic studies", "BBA", "DVAS 2years", "DVAS 3years"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
*/
        addfee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addfeelyout.setVisibility(View.VISIBLE);
                addfee.setVisibility(View.GONE);
                viewfee.setVisibility(View.GONE);
                feeStatus.setVisibility(View.GONE);
            }
        });

        viewfee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), ViewFee.class));
            }
        });

        feeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FeeStatus.class));
            }
        });

        viewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag=dropdown.getSelectedItem().toString();

                if(tag.equals("M.A Urdu")){
                    tag="MA Urdu";
                }else if(tag.equals("M.A eng")){
                    tag="MA eng";
                }else if(tag.equals("B.ed")){
                    tag="Bed";
                }else{

                }
                ref=database.getReference().child("Data").child(campus).child("Students").child(tag);
                dropdown.setVisibility(View.GONE);
                viewStudents.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                header.setVisibility(View.VISIBLE);

                final ProgressDialog progressDialog=new ProgressDialog(StudentSelection.this);
                progressDialog.setMessage("Loading");
                progressDialog.show();

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                            //Map<String, String> map = (Map<String, String>) childSnapshot.getValue();
                            //Shop user = childSnapshot.getValue(Shop.class);
                            Student student=childSnapshot.getValue(Student.class);
//                            String className=student.getSClass().toString();
//                            if(tag.equals(className)) {
                                itemList.add(student);
  //                          }
                    /*String shop_name = map.get("shop_Name");
                    String shop_image = map.get("shop_image");*/
                            progressDialog.dismiss();
                        }

                        progressDialog.dismiss();
                        SelectionAdopter myAdapter = new SelectionAdopter(StudentSelection.this, itemList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(StudentSelection.this));
                        recyclerView.setAdapter(myAdapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        //Log.w(TAG, "Failed to read value.", error.toException());
                        progressDialog.dismiss();
                    }
                });

            }
        });
    }
}
