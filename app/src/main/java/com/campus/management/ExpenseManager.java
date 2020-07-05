package com.campus.management;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class ExpenseManager extends AppCompatActivity {

    private static final int READ_EXTERNAL_INT = 22;
    private EditText  edExpDes, edExpAmount, edExpDate;
    private TextView addExpenseButton, chooseFile, expToolbar;
    private Spinner edExpID;
    private Expense expense;
    FirebaseStorage storage;
    private SharedPreferences sharedPreferences;
    StorageReference storageReference;
    private CardView addExpenseSwitch, viewExpenseSwitch;
    private LinearLayout layout1, layout2;
    private DatabaseReference myRef;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        String campus=Campus.getCampusName();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Data").child(campus).child("Expenses");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        initExpense();
        sharedPreferences=getSharedPreferences("myPref", MODE_PRIVATE);
        String campusName=sharedPreferences.getString("campus","");

        expToolbar.setText("Exp: "+campusName);


        String[] expCats= new String[]{"Computer", "Furniture", "Fans", "University Security", "Examinations",
                "Software", "Stationary", "Entertainment", "vehicle", "Advertisment", "Office Salaries", "Maintainance",
                "General Expense", "traveling Expense", "Postage", "Building Rent", "Printing Expense",
                "Electricity", "TelePhone Bills", "Bank Charges", "Internet Bills", "Phone Expense", "Fee Refund", "University expense"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> idAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, expCats);
//set the spinners adapter to the previously created one.
        edExpID.setAdapter(idAdopter);

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

                edExpDate.setText(sdf.format(myCalendar.getTime()));

            }

        };

        chooseFile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_INT);
                }else{

                    if(chooseFile.getText().equals("Upload Image")){
                        uploadImage();
                    }else {
                        chooseImage();
                    }
                }

            }
        });

        edExpDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ExpenseManager.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addExpenseSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
            }
        });

        viewExpenseSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ExpenseReport.class));
              //  finish();
            }
        });

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                expense=new Expense();
                SharedPreferences sharedPreferences=getSharedPreferences("myPref",MODE_PRIVATE);

                Gson gson = new Gson();
                String json = sharedPreferences.getString("user", "");
                final Employee obj = gson.fromJson(json, Employee.class);

                String expDoneBy=obj.getEmpName().toString();
                String expId=edExpID.getSelectedItem().toString();
                String expDes=edExpDes.getText().toString();
                String expAmount=edExpAmount.getText().toString();
                String expDate=edExpDate.getText().toString();

                if(TextUtils.isEmpty(expDes)){
                    edExpDes.setError("Expense Description Required");
                    edExpDes.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(expAmount)){
                    edExpAmount.setError("Expense Amount Required");
                    edExpAmount.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(expDate)){
                    edExpDate.setError("Expense Date Required");
                    edExpDate.requestFocus();
                    return;
                }

                expense.setExpId(expId);
                expense.setExpAmount(expAmount);
                expense.setExpDescription(expDes);
                expense.setExpDate(expDate);
                expense.setMadeBy(expDoneBy);
                //         getExpense();

                String str1=edExpDate.getText().toString();
                String str2=edExpID.getSelectedItem().toString();
                String expeseId=str1+str2;
                final ProgressDialog dialog = new ProgressDialog(ExpenseManager.this);
                dialog.setCancelable(false);
                dialog.setMessage("Please wait...");
                dialog.show();

                myRef.child(expeseId).setValue(expense).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        Toast.makeText(ExpenseManager.this, "Expense added", Toast.LENGTH_SHORT).show();
                        clearLayout();
                    }
                });
            }
        });

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    private void clearLayout() {
        //edExpID.setText("");
        edExpDes.setText("");
        edExpAmount.setText("");
        edExpDate.setText("");
    }

    private void initExpense() {
        chooseFile=findViewById(R.id.choose_image_id);
        edExpID=findViewById(R.id.exp_expid_id);
        edExpDes=findViewById(R.id.exp_desc_id);
        edExpAmount=findViewById(R.id.exp_amount_id);
        edExpDate=findViewById(R.id.exp_date_id);
        expToolbar=findViewById(R.id.expense_toolbar_id);
        addExpenseButton=findViewById(R.id.addexpense_id);
        layout1=findViewById(R.id.expense_layout1_id);
        layout2=findViewById(R.id.expense_layout2_id);
        addExpenseSwitch=findViewById(R.id.addexpense_switch_id);
        viewExpenseSwitch=findViewById(R.id.viewexpense_switch_id);
    }

    private void getExpense(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                chooseFile.setText("Upload Image");
                //imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            chooseFile.setText("Image Uploaded");
                            Toast.makeText(ExpenseManager.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ExpenseManager.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
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
