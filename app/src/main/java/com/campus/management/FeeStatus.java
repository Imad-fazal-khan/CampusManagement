package com.campus.management;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.List;

public class FeeStatus extends AppCompatActivity {

    private Spinner monthSpinner, yearSpinner, classSpinner, feeTypeSpinner;
    private TextView checkButton, headerTV;
    private RecyclerView recyclerView;
    private LinearLayout header, firstLayout;
    private DatabaseReference ref1, ref2;
    private List<Fee> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_status);

        initFeeStatus();
        itemList=new ArrayList<>();
        String campus=Campus.getCampusName();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        ref1=database.getReference().child("Data").child(campus).child("Students");
        ref2=database.getReference().child("Data").child(campus).child("Fees");

        SharedPreferences sharedPreferences=getSharedPreferences("myPref", MODE_PRIVATE);
        String campusName=sharedPreferences.getString("campus","");
        headerTV.setText("Fee: "+campusName);

        String[] feeTypes= new String[]{"Registration Fee", "Admission Charges", "Monthly Tution Fee", "Security Charges", };
        ArrayAdapter<String> feeTypeAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, feeTypes);
        feeTypeSpinner.setAdapter(feeTypeAdopter);

        if(campus.equalsIgnoreCase("National Institute of Education and Management")) {
            String[] classes = new String[]{"M.A Urdu", "M.A eng", "Msc Math", "MCS", "B.ed", "ADE", "BS sports science",
                    "BS eng", "BS math", "BS computer science", "BS islamic studies", "BBA", "DVAS 2years", "DVAS 3years"};
            ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
            classSpinner.setAdapter(classAdopter);

        }else if(campus.equalsIgnoreCase("Fatima Academy")){
            String[] classes = new String[]{"7th Male","8th Male","9th Male","10th Male","Fsc1 Male","Fsc11 Male","Bsc Male","7th Female",
                    "8th Female","9th Female","10th Female","Fsc1 Female","Fsc11 Female","Bsc Female"};
            ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
            classSpinner.setAdapter(classAdopter);

        }else if(campus.equalsIgnoreCase("The Knowledge school")){
            String[] classes = new String[]{"PG","Nursery", "Prep", "One", "Two", "Three", "4th", "5th", "6th boys", "6th girls",
                    "7th boys", "7th girls", "8th boys", "8th girls", "9th boys", "9th girls","10th boys", "10th girls"};
            ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
            classSpinner.setAdapter(classAdopter);

        }else if(campus.equalsIgnoreCase("School of Nursing Education Bhakkar")){
            String[] classes = new String[]{"RN","RM","LHV","CMW","CNA","LPA","FWW"};
            ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
            classSpinner.setAdapter(classAdopter);
        }else if(campus.equalsIgnoreCase("GU Campus Bhakkar")) {
            String[] classes = new String[]{"M.A Urdu", "M.A eng", "Msc Math", "MCS", "B.ed", "ADE", "BS sports science",
                    "BS eng", "BS math", "BS computer science", "BS islamic studies", "BBA", "DVAS 2years", "DVAS 3years"};
            ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
            classSpinner.setAdapter(classAdopter);

        }

        String[] items = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String[] items1 = new String[]{"2010", "2011", "2012", "2013", "2014", "2015", "2016","2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
//set the spinners adapter to the previously created one.
        monthSpinner.setAdapter(adapter);
        yearSpinner.setAdapter(adapter1);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstLayout.setVisibility(View.GONE);
                header.setVisibility(View.VISIBLE);
                headerTV.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                String month=monthSpinner.getSelectedItem().toString();
                month=month.substring(0,3);

                final String year=yearSpinner.getSelectedItem().toString();
                final String clas=classSpinner.getSelectedItem().toString();
                final String feeType=feeTypeSpinner.getSelectedItem().toString();

                final ProgressDialog progressDialog=new ProgressDialog(FeeStatus.this);
                progressDialog.setMessage("Loading");
                progressDialog.show();

                final String finalMonth = month;
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            //Map<String, String> map = (Map<String, String>) childSnapshot.getValue();
                            // Shop user = childSnapshot.getValue(Shop.class);
                            Fee fee=childSnapshot.getValue(Fee.class);
                            if(fee.getDate().contains(finalMonth) && fee.getDate().contains(year) && fee.getStdClass().equals(clas) && fee.getFeeType().equals(feeType))
                            {
                                itemList.add(fee);
                            }
                    /*String shop_name = map.get("shop_Name");
                    String shop_image = map.get("shop_image");*/
                            progressDialog.dismiss();
                        }

                       StatusAdopter myAdapter = new StatusAdopter(FeeStatus.this, itemList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(FeeStatus.this));
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

    private void initFeeStatus() {
        headerTV=findViewById(R.id.check_status_header_tv_id);
        monthSpinner=findViewById(R.id.spinner_status_month_id);
        yearSpinner=findViewById(R.id.spinner_status_year_id);
        classSpinner=findViewById(R.id.spinner_status_class_id);
        feeTypeSpinner=findViewById(R.id.spinner_status_feetype_id);
        checkButton=findViewById(R.id.check_status_button_id);
        recyclerView=findViewById(R.id.statuss_recycler_view);
        header=findViewById(R.id.status_header_layout);
        firstLayout=findViewById(R.id.status_first_layout_id);
    }
}
