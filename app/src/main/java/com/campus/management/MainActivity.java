package com.campus.management;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private CheckBox accessReg, accessExp, accessFee;
    private EditText edEmpName, edEmpEmail, edEmpPassword1, edEmpPassword2;
    private TextView doneButton;
    private CardView createAccountButton, deleteAccountButton, feeStructure;
    private Employee employee;
    private LinearLayout createLayout;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intitMain();

        String campus=Campus.getCampusName();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Data").child(campus).child("employees");

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EmpSelection.class));
            }
        });
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccountButton.setVisibility(View.GONE);
                deleteAccountButton.setVisibility(View.GONE);
                feeStructure.setVisibility(View.GONE);
                createLayout.setVisibility(View.VISIBLE);
            }
        });
        feeStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FeeStructure.class));
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("Please wait...");
                dialog.show();
                employee=new Employee();
                getEmployee();
                String userName=edEmpName.getText().toString();

                myRef.child(userName).setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        clearLayout();
                        Toast.makeText(MainActivity.this,"Account Created Successfully", Toast.LENGTH_SHORT).show();
                        createAccountButton.setVisibility(View.VISIBLE);
                        deleteAccountButton.setVisibility(View.VISIBLE);
                        feeStructure.setVisibility(View.VISIBLE);
                        createLayout.setVisibility(View.GONE);
                    }
                });
                /*
                myRef.child(userName).setValue(employee);
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
*/
            }
        });

    }

    private void getEmployee(){

        String empName=edEmpName.getText().toString();
        String empEmail=edEmpEmail.getText().toString();
        String empPass1=edEmpPassword1.getText().toString();
        String empPass2=edEmpPassword2.getText().toString();
        if(TextUtils.isEmpty(empName)){
            edEmpName.setError("Enter Name");
            edEmpName.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(empEmail)){
            edEmpEmail.setError("Enter Email");
            edEmpEmail.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(empPass1)){
            edEmpPassword1.setError("Enter Password");
            edEmpPassword1.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(empPass2)){
            edEmpPassword2.setError("Conform Passoword");
            edEmpPassword2.requestFocus();
            return;
        }
        if(!empPass1.equals(empPass2)){
            edEmpPassword2.setError("Passwords Doesn't Match");
            edEmpPassword2.requestFocus();
            return;
        }
        if(accessReg.isChecked()){
            employee.setAcessRegisteration("given");
        }else{
            employee.setAcessRegisteration("NoAccess");
        }
        if(accessExp.isChecked()){
            employee.setAccessExpenses("given");
        }else{
            employee.setAccessExpenses("NoAccess");
        }
        if(accessFee.isChecked()){
            employee.setAccessFee("given");
        }else{
            employee.setAccessFee("NoAccess");
        }

        employee.setEmpName(empName);
        employee.setEmpEmail(empEmail);
        employee.setEmpPassword(empPass1);
    }


    private void intitMain(){
        accessReg=findViewById(R.id.access_registeration_id);
        accessExp=findViewById(R.id.access_expenses_id);
        accessFee=findViewById(R.id.access_fee_id);
        edEmpName=findViewById(R.id.reg_emp_name_id);
        edEmpEmail=findViewById(R.id.reg_emp_email_id);
        edEmpPassword1=findViewById(R.id.reg_emp_pass1_id);
        edEmpPassword2=findViewById(R.id.reg_emp_pass2_id);
        doneButton=findViewById(R.id.register_employee_id);
        createAccountButton=findViewById(R.id.admin_create_account_id);
        deleteAccountButton=findViewById(R.id.admin_delete_account_id);
        feeStructure=findViewById(R.id.admin_fee_structure_id);
        createLayout=findViewById(R.id.main_layout_2);

    }

    private void clearLayout(){

        accessReg.setChecked(false);
        accessExp.setChecked(false);
        accessFee.setChecked(false);
        edEmpName.setText("");
        edEmpEmail.setText("");
        edEmpPassword1.setText("");
        edEmpPassword2.setText("");

    }
}
