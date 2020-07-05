package com.campus.management;

import android.app.ProgressDialog;
import android.renderscript.Script;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmpSelection extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private ArrayList<Employee> itemList;
    private RecyclerView recyclerView;
    private EmployeeAdopter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_selection);

        String campus=Campus.getCampusName();
        database= FirebaseDatabase.getInstance();
        ref=database.getReference().child("Data").child(campus).child("employees");
        itemList=new ArrayList<>();

        final ProgressDialog progressDialog=new ProgressDialog(EmpSelection.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    //Map<String, String> map = (Map<String, String>) childSnapshot.getValue();
                    //Shop user = childSnapshot.getValue(Shop.class);
                    Employee employee=childSnapshot.getValue(Employee.class);
                    itemList.add(employee);
                    /*String shop_name = map.get("shop_Name");
                    String shop_image = map.get("shop_image");*/
                    progressDialog.dismiss();
                }


                recyclerView=findViewById(R.id.emp_selection_recycler_id);
                myAdapter = new EmployeeAdopter(EmpSelection.this, itemList);
                recyclerView.setLayoutManager(new LinearLayoutManager(EmpSelection.this));
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

