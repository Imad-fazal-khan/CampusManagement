package com.campus.management;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ExpenseReport extends AppCompatActivity {

    private static final int READ_EXTERNAL_INT = 65;
    private TextView monthlyeReportButton, annualReportButton, allExpense, byName;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private EditText edByName;
    private ArrayList<Expense> itemList;
    private RecyclerView recyclerView;
    private LinearLayout tempLayout;
    private ExpenseAdopter myAdapter;
    private Spinner edMonth, edAnnual;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_report);

        initExpenseReport();

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

        byName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=edByName.getText().toString();
                if(TextUtils.isEmpty(name)){
                    edByName.setError("Enter Name");
                    edByName.requestFocus();
                    return;
                }

                showByName(name);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_INT);
                }else{
                fab.setVisibility(View.GONE);
                takeScreenshot();
                }
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
                tempLayout.setVisibility(View.VISIBLE);
                //fab.setVisibility(View.VISIBLE);
                monthlyeReportButton.setVisibility(View.GONE);
                annualReportButton.setVisibility(View.GONE);
                allExpense.setVisibility(View.GONE);
                edMonth.setVisibility(View.GONE);
                edByName.setVisibility(View.GONE);
                byName.setVisibility(View.GONE);
                edAnnual.setVisibility(View.GONE);

                showAllData();
            }
        });
    }

    private void showByName(String name) {

            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Loading");
            progressDialog.show();
            final ArrayList<Expense> monthlyList=new ArrayList<>();
            final String na=edByName.getText().toString();

            edMonth.setVisibility(View.GONE);
            edAnnual.setVisibility(View.GONE);
            edByName.setVisibility(View.GONE);
            byName.setVisibility(View.GONE);
            //fab.setVisibility(View.VISIBLE);
            tempLayout.setVisibility(View.VISIBLE);
            annualReportButton.setVisibility(View.GONE);
            monthlyeReportButton.setVisibility(View.GONE);
            allExpense.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            ref.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                        Expense expense=childSnapshot.getValue(Expense.class);
                        itemList.add(expense);
                        progressDialog.dismiss();
                    }

                    for(Expense item: itemList){
                        String nametxt=item.getMadeBy();
                        if(nametxt.equalsIgnoreCase(na)) {
                            monthlyList.add(item);
                        }

                    }
                    myAdapter = new ExpenseAdopter(ExpenseReport.this, monthlyList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ExpenseReport.this));
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

    private void initExpenseReport() {
        byName=findViewById(R.id.expense_report_byname_id);
        edByName=findViewById(R.id.ed_byname_expense_id);
        fab=findViewById(R.id.fab);
        tempLayout=findViewById(R.id.temp_layout1);
        database= FirebaseDatabase.getInstance();
        String campus=Campus.getCampusName();
        ref=database.getReference().child("Data").child(campus).child("Expenses");
        itemList=new ArrayList<>();
        monthlyeReportButton=findViewById(R.id.expense_report_monthly_id);
        annualReportButton=findViewById(R.id.expense_report_annual_id);
        allExpense=findViewById(R.id.expense_report_all_id);
        recyclerView=findViewById(R.id.expense_recycler_view);
        edMonth=findViewById(R.id.ed_monthly_expense_id);
        edAnnual=findViewById(R.id.ed_annual_expense_id);
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }


    private void showAllData(){

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        //fab.setVisibility(View.VISIBLE);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    //Map<String, String> map = (Map<String, String>) childSnapshot.getValue();
                    // Shop user = childSnapshot.getValue(Shop.class);
                    Expense expense=childSnapshot.getValue(Expense.class);
                    itemList.add(expense);
                    /*String shop_name = map.get("shop_Name");
                    String shop_image = map.get("shop_image");*/
                    progressDialog.dismiss();
                }

                myAdapter = new ExpenseAdopter(ExpenseReport.this, itemList);
                recyclerView.setLayoutManager(new LinearLayoutManager(ExpenseReport.this));
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
        final ArrayList<Expense> monthlyList=new ArrayList<>();
        final String month=edMonth.getSelectedItem().toString();

        edMonth.setVisibility(View.GONE);
        edAnnual.setVisibility(View.GONE);
        edByName.setVisibility(View.GONE);
        byName.setVisibility(View.GONE);
        //fab.setVisibility(View.VISIBLE);
        tempLayout.setVisibility(View.VISIBLE);
        annualReportButton.setVisibility(View.GONE);
        monthlyeReportButton.setVisibility(View.GONE);
        allExpense.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    Expense expense=childSnapshot.getValue(Expense.class);
                    itemList.add(expense);
                    progressDialog.dismiss();
                }

                for(Expense item: itemList){
                    String dateText=item.getExpDate().toString();
                     //dateText=dateText.substring(0, 2);
                    String month1=month;
                    month1=month1.substring(0,3);

                    if(dateText.contains(month1)) {
                        monthlyList.add(item);
                    }

                }
                myAdapter = new ExpenseAdopter(ExpenseReport.this, monthlyList);
                recyclerView.setLayoutManager(new LinearLayoutManager(ExpenseReport.this));
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
        final ArrayList<Expense> yearList=new ArrayList<>();
        final String year=edAnnual.getSelectedItem().toString();

        edMonth.setVisibility(View.GONE);
        edAnnual.setVisibility(View.GONE);
        edByName.setVisibility(View.GONE);
        byName.setVisibility(View.GONE);
        //fab.setVisibility(View.VISIBLE);
        annualReportButton.setVisibility(View.GONE);
        monthlyeReportButton.setVisibility(View.GONE);
        allExpense.setVisibility(View.GONE);
        tempLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    Expense expense=childSnapshot.getValue(Expense.class);
                    itemList.add(expense);
                    progressDialog.dismiss();
                }

                for(Expense item: itemList){
                    String dateText=item.getExpDate().toString();
                    //dateText=dateText.substring(0, 2);

                    if(dateText.contains(year)) {
                        yearList.add(item);
                    }

                }
                myAdapter = new ExpenseAdopter(ExpenseReport.this, yearList);
                recyclerView.setLayoutManager(new LinearLayoutManager(ExpenseReport.this));
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_INT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_LONG).show();
                /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQ);*/
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
