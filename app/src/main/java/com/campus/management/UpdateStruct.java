package com.campus.management;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateStruct extends AppCompatActivity {

    private TextView tvClass, doneButton;
    private EditText edRegFee, edAdmCharges, edSecurity, edMonthlyTution;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FeeStruct feeStruct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_struct);

        initUpateStructure();

        Intent intent=getIntent();

        feeStruct=new FeeStruct();
        feeStruct=(FeeStruct) intent.getSerializableExtra("sentStruct");

        edRegFee.setText(feeStruct.getRegistrationFee().toString());
        edAdmCharges.setText(feeStruct.getAdmissionCharges().toString());
        edSecurity.setText(feeStruct.getSecurityCharges().toString());
        edMonthlyTution.setText(feeStruct.getMonthlyTutionFee().toString());
        tvClass.setText(feeStruct.getStructClass().toString());

        database= FirebaseDatabase.getInstance();
        String campus=Campus.getCampusName();
        ref=database.getReference().child("Data").child(campus).child("Structures");

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regFee=edRegFee.getText().toString();
                String admiCharges=edAdmCharges.getText().toString();
                String securityCharges=edSecurity.getText().toString();
                String monthlyFee=edMonthlyTution.getText().toString();
                String clas= tvClass.getText().toString();

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

                final ProgressDialog dialog = new ProgressDialog(UpdateStruct.this);
                dialog.setCancelable(false);
                dialog.setMessage("Please wait...");
                dialog.show();

                ref.child(clas).setValue(feeStruct).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        Toast.makeText(UpdateStruct.this, "Updated Successfully", Toast.LENGTH_LONG).show();
                    }
                });            }
        });
    }


    private void initUpateStructure() {

        tvClass=findViewById(R.id.update_structure_class_id);
        edRegFee=findViewById(R.id.update_structure_regis_fee_id);
        edAdmCharges=findViewById(R.id.update_structure_admission_charges_id);
        edSecurity=findViewById(R.id.update_structure_security_id);
        edMonthlyTution=findViewById(R.id.update_structure_tution_fee_id);
        doneButton=findViewById(R.id.update_structure_done_id);
    }
}
