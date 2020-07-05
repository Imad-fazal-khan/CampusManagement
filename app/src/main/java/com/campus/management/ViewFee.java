package com.campus.management;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

public class ViewFee extends AppCompatActivity {

    private TextView monthlyeReportButton, annualReportButton, allExpense, header;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private LinearLayout headerLayout;
    private ArrayList<Fee> itemList;
    private RecyclerView recyclerView;
    private FeeAdopter myAdapter;
    private Spinner edMonth, edAnnual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fee);


        initExpenseReport();

        SharedPreferences sharedPreferences=getSharedPreferences("myPref", MODE_PRIVATE);
        String campusName=sharedPreferences.getString("campus","");
        header.setText("Fee: "+campusName);


        String[] items = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String[] items1 = new String[]{"2010", "2011", "2012", "2013", "2014", "2015", "2016","2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
//set the spinners adapter to the previously created one.
        edMonth.setAdapter(adapter);
        edAnnual.setAdapter(adapter1);


        monthlyeReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showMonthly();
            }
        });

        annualReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnnual();
            }
        });
        allExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerView.setVisibility(View.VISIBLE);
                headerLayout.setVisibility(View.VISIBLE);
                header.setVisibility(View.GONE);
                monthlyeReportButton.setVisibility(View.GONE);
                annualReportButton.setVisibility(View.GONE);
                allExpense.setVisibility(View.GONE);
                edMonth.setVisibility(View.GONE);
                edAnnual.setVisibility(View.GONE);

                showAllData();
            }
        });
    }

    private void initExpenseReport() {
        header=findViewById(R.id.viewfee_header_tv_id);
        headerLayout=findViewById(R.id.fee_header_layout);
        database= FirebaseDatabase.getInstance();
        String campus=Campus.getCampusName();
        ref=database.getReference().child("Data").child(campus).child("Fees");
        itemList=new ArrayList<>();
        monthlyeReportButton=findViewById(R.id.fee_report_monthly_id);
        annualReportButton=findViewById(R.id.fee_report_annual_id);
        allExpense=findViewById(R.id.fee_report_all_id);
        recyclerView=findViewById(R.id.fee_recycler_view);
        edMonth=findViewById(R.id.ed_monthly_fee_id);
        edAnnual=findViewById(R.id.ed_annual_fee_id);
    }

    private void showAllData(){

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    Fee fee=childSnapshot.getValue(Fee.class);
                    itemList.add(fee);
                    /*String shop_name = map.get("shop_Name");
                    String shop_image = map.get("shop_image");*/
                    progressDialog.dismiss();
                }

                myAdapter = new FeeAdopter(ViewFee.this, itemList);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViewFee.this));
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


    private void showMonthly(){

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        final ArrayList<Fee> monthlyList=new ArrayList<>();
        final String month=edMonth.getSelectedItem().toString();

        edMonth.setVisibility(View.GONE);
        edAnnual.setVisibility(View.GONE);
        header.setVisibility(View.GONE);
        headerLayout.setVisibility(View.VISIBLE);
        annualReportButton.setVisibility(View.GONE);
        monthlyeReportButton.setVisibility(View.GONE);
        allExpense.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    Fee fee=childSnapshot.getValue(Fee.class);
                    itemList.add(fee);
                    progressDialog.dismiss();
                }

                for(Fee item: itemList){
                   String dateText=item.getDate().toString();
                    //dateText=dateText.substring(0, 2);
                    String month1=month;
                    month1=month1.substring(0,3);

                    if(dateText.contains(month1)) {
                        monthlyList.add(item);
                    }

                }
                myAdapter = new FeeAdopter(ViewFee.this, monthlyList);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViewFee.this));
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

    private void showAnnual(){

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        final ArrayList<Fee> yearList=new ArrayList<>();
        final String year=edAnnual.getSelectedItem().toString();

        edMonth.setVisibility(View.GONE);
        edAnnual.setVisibility(View.GONE);
        header.setVisibility(View.GONE);
        headerLayout.setVisibility(View.VISIBLE);
        annualReportButton.setVisibility(View.GONE);
        monthlyeReportButton.setVisibility(View.GONE);
        allExpense.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    Fee fee=childSnapshot.getValue(Fee.class);
                    itemList.add(fee);
                    progressDialog.dismiss();
                }

                for(Fee item: itemList){
                    String dateText=item.getDate().toString();

                    if(dateText.contains(year)) {
                        yearList.add(item);
                    }

                }
                myAdapter = new FeeAdopter(ViewFee.this, yearList);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViewFee.this));
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
}



