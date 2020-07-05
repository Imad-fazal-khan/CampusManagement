package com.campus.management;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewStudents extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAdopter myAdapter;
    private List<Student> itemList;
    FirebaseDatabase database;
    DatabaseReference ref;
    private String sentString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

        sentString=getIntent().getStringExtra("className");

        if(sentString.equals("M.A Urdu")){
            sentString="MA Urdu";
        }else if(sentString.equals("M.A eng")){
            sentString="MA eng";
        }else if(sentString.equals("B.ed")){
            sentString="Bed";
        }else{

        }
        database=FirebaseDatabase.getInstance();
        String campus=Campus.getCampusName();
        ref=database.getReference().child("Data").child(campus).child("Students").child(sentString);
        itemList=new ArrayList<>();

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    //Map<String, String> map = (Map<String, String>) childSnapshot.getValue();
                    //Shop user = childSnapshot.getValue(Shop.class);
                    Student student=childSnapshot.getValue(Student.class);
                    //if(student.getSClass().equals(sentString)) {
                        itemList.add(student);
                    //}
                    /*String shop_name = map.get("shop_Name");
                    String shop_image = map.get("shop_image");*/
                    progressDialog.dismiss();
                }

                progressDialog.dismiss();
                recyclerView=findViewById(R.id.students_recycler_id);
                myAdapter = new StudentAdopter(ViewStudents.this, itemList);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViewStudents.this));
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
