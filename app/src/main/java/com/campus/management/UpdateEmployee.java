package com.campus.management;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateEmployee extends AppCompatActivity {


    private CheckBox accessReg, accessExp, accessFee;
    private EditText edEmpEmail, edEmpPassword1, edEmpPassword2;
    private TextView updateButton, deleteButton, edEmpName;
    private Employee employee;
    private String key;
    private DatabaseReference myRef, myRef2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employee);

        initUpdate();
        Intent intent=getIntent();

        employee=new Employee();
        employee=(Employee) intent.getSerializableExtra("sentEmployee");
        if(!employee.getAccessExpenses().toString().isEmpty()){
            accessExp.setChecked(true);
        }
        if(!employee.getAccessFee().toString().isEmpty()){
            accessFee.setChecked(true);
        }
        if(!employee.getAcessRegisteration().toString().isEmpty()){
            accessReg.setChecked(true);
        }

        edEmpName.setText(employee.getEmpName().toString());
        edEmpEmail.setText(employee.getEmpEmail().toString());
        edEmpPassword1.setText(employee.getEmpPassword());
        edEmpPassword2.setText(employee.getEmpPassword());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String campus=Campus.getCampusName();
        myRef = database.getReference().child("Data").child(campus).child("employees");
        myRef2 = database.getReference().child("Data").child(campus).child("employees");

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(UpdateEmployee.this);
                dialog.setCancelable(false);
                dialog.setMessage("Please wait...");
                dialog.show();
               // findAndDelete(employee.getEmpName(), employee.getEmpEmail());
    //            deleteEmployee(key);
                getEmployee();
                String userName=edEmpName.getText().toString();

                myRef.child(userName).setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        Toast.makeText(UpdateEmployee.this, "Record Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAndDelete(employee.getEmpName(), employee.getEmpEmail());
                Toast.makeText(UpdateEmployee.this, "Record Deleted", Toast.LENGTH_SHORT).show();
                finish();
  //              deleteEmployee(key);
            }
        });
    }

    private void initUpdate(){
        accessReg=findViewById(R.id.update_access_registeration_id);
        accessExp=findViewById(R.id.update_access_expenses_id);
        accessFee=findViewById(R.id.update_access_fee_id);
        edEmpName=findViewById(R.id.update_emp_name_id);
        edEmpEmail=findViewById(R.id.update_emp_email_id);
        edEmpPassword1=findViewById(R.id.update_emp_pass1_id);
        edEmpPassword2=findViewById(R.id.update_emp_pass2_id);
        updateButton=findViewById(R.id.update_register_employee_id);
        deleteButton=findViewById(R.id.update_delete_employee_id);
    }

    private void findAndDelete(String name, String email){
        final ProgressDialog dialog = new ProgressDialog(UpdateEmployee.this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.show();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    //Map<String, String> map = (Map<String, String>) childSnapshot.getValue();
                    //Shop user = childSnapshot.getValue(Shop.class);
                    Employee emp=childSnapshot.getValue(Employee.class);
                    if(emp.getEmpName().equals(employee.getEmpName()) && emp.getEmpEmail().equals(employee.getEmpEmail())){
                    key=childSnapshot.getKey().toString();
                        myRef2.child(key).removeValue();

                    }
                    /*String shop_name = map.get("shop_Name");
                    String shop_image = map.get("shop_image");*/
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            dialog.dismiss();
            }
        });
    }
    private void getEmployee(){

        String empName=edEmpName.getText().toString();
        String empEmail=edEmpEmail.getText().toString();
        String empPass1=edEmpPassword1.getText().toString();
        String empPass2=edEmpPassword2.getText().toString();

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

/*    private void deleteEmployee(String kk){
        myRef.child(kk).removeValue();
    }*/
}
