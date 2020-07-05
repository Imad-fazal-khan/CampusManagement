package com.campus.management;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FeeManager extends AppCompatActivity {

    private static final int SEND_SMS_INT = 101;
    private FirebaseDatabase database;
    private DatabaseReference feeRef, myRef;
    private FeeStruct feeStruct;
    //private ArrayList<Student> itemList;
    private EditText edDiscount;
    private TextView submitFeeButton, edclassName, edstudentName, edfName, edDate, edamount, feeToolbar;
    private Student sendObj;
    private Spinner feeType;
    private String message;
    private String phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_manager);

        String campus=Campus.getCampusName();
        database= FirebaseDatabase.getInstance();
        feeRef=database.getReference().child("Data").child(campus).child("Fees");
        myRef=database.getReference().child("Data").child(campus).child("Structures");
        initFeeManager();
        SharedPreferences sharedPreferences=getSharedPreferences("myPref", MODE_PRIVATE);
        String campusName=sharedPreferences.getString("campus","");
        feeToolbar.setText("Fee: "+campusName);

        feeStruct=new FeeStruct();

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                edDate.setText(sdf.format(myCalendar.getTime()));

            }

        };

        edDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FeeManager.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        String[] feeTypes= new String[]{"Registration Fee", "Admission Charges", "Monthly Tution Fee", "Security Charges", };
        ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, feeTypes);
        feeType.setAdapter(classAdopter);

        Intent intent=getIntent();
        sendObj=(Student) intent.getSerializableExtra("profile");
        edclassName.setText(sendObj.getSClass().toString());
        edstudentName.setText(sendObj.getName().toString());
        edfName.setText(sendObj.getFName().toString());

        phoneNo=sendObj.getPhone().toString();

        final ProgressDialog progressDialog=new ProgressDialog(FeeManager.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    //Map<String, String> map = (Map<String, String>) childSnapshot.getValue();
                    //Shop user = childSnapshot.getValue(Shop.class);
                    FeeStruct feeSt=childSnapshot.getValue(FeeStruct.class);
                    String className=feeSt.getStructClass().toString();
                    if(className.equals("MA Urdu")){
                        className="M.A Urdu";
                    }else if(className.equals("MA eng")){
                        className="M.A eng";
                    }else if(className.equals("Bed")){
                        className="B.ed";
                    }else{

                    }

                    if(className.equals(sendObj.getSClass())) {
                        feeStruct=feeSt;
                    }
                    /*String shop_name = map.get("shop_Name");
                    String shop_image = map.get("shop_image");*/
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
                progressDialog.dismiss();
            }
        });

        feeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    edamount.setText(feeStruct.getRegistrationFee());
                }else if(position==1){
                    edamount.setText(feeStruct.getAdmissionCharges());
                }else if(position==2){
                    edamount.setText(feeStruct.getMonthlyTutionFee());
                }else if(position==3){
                    edamount.setText(feeStruct.getSecurityCharges());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitFeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFee();
            }
        });
    }

    private void initFeeManager() {
        feeToolbar=findViewById(R.id.fee_toolbar_id);
        edclassName=findViewById(R.id.fee_class_id);
        edDate=findViewById(R.id.fee_date_id);
        edstudentName=findViewById(R.id.fee_name_id);
        edfName=findViewById(R.id.fee_fname_id);
        edamount=findViewById(R.id.fee_amount_id);
        edDiscount=findViewById(R.id.fee_discount_id);
        submitFeeButton=findViewById(R.id.fee_submitfee_id);
        feeType=findViewById(R.id.fee_manager_spinner_id);
    }

    private void submitFee(){

        String className=edclassName.getText().toString();
        String hint=sendObj.getFCnin();
        String studentName=edstudentName.getText().toString();
        String fathersName=edfName.getText().toString();
        String amount=edamount.getText().toString();
        String discount=edDiscount.getText().toString();
        String date=edDate.getText().toString();
        String ftype=feeType.getSelectedItem().toString();

        if(TextUtils.isEmpty(amount)){
            Toast.makeText(getApplicationContext(),"You Cannot Sumbit Fee for the selected Class", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(date)){
            edDate.setError("Select Date");
            edDate.requestFocus();
            return;
        }

        Fee fee=new Fee();
        fee.setStdClass(className);
        fee.setStudName(studentName);
        fee.setStdFName(fathersName);
        fee.setSubAmount(amount);
        fee.setFeeType(ftype);
        fee.setDate(date);
        fee.setFeeDiscount(discount);

        message="Mr "+sendObj.getName()+" have successfully submitted "+amount+" for "+className+" on "+date;
        final ProgressDialog progressDialog=new ProgressDialog(FeeManager.this);
        progressDialog.setMessage("Wait");
        progressDialog.show();

                String userKey=studentName+hint;
                feeRef.child(userKey).setValue(fee).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(FeeManager.this, "Fee Submitted", Toast.LENGTH_SHORT).show();
                        sendSMSMessages();
                        //finish();
                    }
                });

    }

    private void sendSMSMessage(){
        try{
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(phoneNo,null,message,null,null);
            Toast.makeText(FeeManager.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(FeeManager.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void sendSMSMessages() {

        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_INT);
        }else{
            try{
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(phoneNo,null,message,null,null);
                Toast.makeText(FeeManager.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Toast.makeText(FeeManager.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
            }
        }

/*
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNo));
        intent.putExtra("sms_body", message);
        startActivity(intent);
*/

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {

                try{
                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(phoneNo,null,message,null,null);
                    Toast.makeText(FeeManager.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(FeeManager.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }*/
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SEND_SMS_INT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission granted", Toast.LENGTH_LONG).show();
                /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQ);*/
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
