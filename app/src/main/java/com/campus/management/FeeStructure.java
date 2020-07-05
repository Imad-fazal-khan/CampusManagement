package com.campus.management;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;

public class FeeStructure extends AppCompatActivity {

    private CardView addFeeStruc, viewFeeStruc;
    private Spinner classSpinner;
    private TextView doneButton, temp;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private EditText edRegFee, edAdmCharges, edSecurity, edMonthlyTution;
    private LinearLayout addStructureLayout;
    private List<FeeStruct> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_structure);

        initFeeStructure();
        itemList=new ArrayList<>();

        String campus=Campus.getCampusName();
        database=FirebaseDatabase.getInstance();
        ref=database.getReference().child("Data").child(campus).child("Structures");


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

        addFeeStruc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFeeStruc.setVisibility(View.GONE);
                viewFeeStruc.setVisibility(View.GONE);
                addStructureLayout.setVisibility(View.VISIBLE);
            }
        });

        viewFeeStruc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                temp.setVisibility(View.GONE);
                addFeeStruc.setVisibility(View.GONE);
                viewFeeStruc.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                final ProgressDialog progressDialog=new ProgressDialog(FeeStructure.this);
                progressDialog.setMessage("Loading");
                progressDialog.show();

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            //Map<String, String> map = (Map<String, String>) childSnapshot.getValue();
                            // Shop user = childSnapshot.getValue(Shop.class);
                            FeeStruct feeS=childSnapshot.getValue(FeeStruct.class);
                                itemList.add(feeS);

                                /*String shop_name = map.get("shop_Name");
                    String shop_image = map.get("shop_image");*/
                            progressDialog.dismiss();
                        }

                        FeeStructAdopter myAdapter = new FeeStructAdopter(FeeStructure.this, itemList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(FeeStructure.this));
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


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regFee=edRegFee.getText().toString();
                String admiCharges=edAdmCharges.getText().toString();
                String securityCharges=edSecurity.getText().toString();
                String monthlyFee=edMonthlyTution.getText().toString();
                String clas=classSpinner.getSelectedItem().toString();

                if(clas.equals("M.A Urdu")){
                    clas="MA Urdu";
                }else if(clas.equals("M.A eng")){
                    clas="MA eng";
                }else if(clas.equals("B.ed")){
                    clas="Bed";
                }else{

                }
                if(TextUtils.isEmpty(regFee)){
                    edRegFee.setError("Enter Registration Fee");
                    edRegFee.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(admiCharges)){
                    edAdmCharges.setError("Enter Admission Charges");
                    edAdmCharges.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(securityCharges)){
                    edSecurity.setError("Enter Security Charges");
                    edSecurity.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(monthlyFee)){
                    edMonthlyTution.setError("Enter Monthly Tution Fee");
                    edMonthlyTution.requestFocus();
                    return;
                }

                FeeStruct feeStruct=new FeeStruct();
                feeStruct.setAdmissionCharges(admiCharges);
                feeStruct.setMonthlyTutionFee(monthlyFee);
                feeStruct.setRegistrationFee(regFee);
                feeStruct.setSecurityCharges(securityCharges);
                feeStruct.setStructClass(clas);

                final ProgressDialog dialog = new ProgressDialog(FeeStructure.this);
                dialog.setCancelable(false);
                dialog.setMessage("Please wait...");
                dialog.show();

                ref.child(clas).setValue(feeStruct).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        resetLaout();
                        Toast.makeText(FeeStructure.this, "Task Successfully Completed", Toast.LENGTH_LONG).show();
                    }
                });            }
        });
    }

    private void resetLaout() {

    }

    private void initFeeStructure() {

        temp=findViewById(R.id.temp);
        addFeeStruc=findViewById(R.id.admin_add_fee_structure_id);
        viewFeeStruc=findViewById(R.id.admin_view_fee_structure_id);
        edRegFee=findViewById(R.id.structure_regis_fee_id);
        edAdmCharges=findViewById(R.id.structure_admission_charges_id);
        edSecurity=findViewById(R.id.structure_security_id);
        edMonthlyTution=findViewById(R.id.structure_tution_fee_id);
        addStructureLayout=findViewById(R.id.fee_structure_add_layout_id);
        classSpinner=findViewById(R.id.structure_classes_spinner_id);
        doneButton=findViewById(R.id.structure_done_id);
        recyclerView=findViewById(R.id.fee_structure_recycler_view_id);

    }
}
