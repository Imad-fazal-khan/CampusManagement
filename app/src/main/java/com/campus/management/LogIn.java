package com.campus.management;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class LogIn extends AppCompatActivity {

    private EditText edEmail, edPassword;
    private TextView loginButton;
    private Spinner campusSpinner;
    FirebaseDatabase database;
    DatabaseReference ref;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        sharedPreferences=getSharedPreferences("myPref",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        String hint=sharedPreferences.getString("userState", "");
        String campusName=sharedPreferences.getString("campus","");
        Campus.setCampusName(campusName);

        if(hint.equals("loggedIn")){
            startActivity(new Intent(LogIn.this, Splash1.class));
            finish();
        }

        campusSpinner=findViewById(R.id.login_campus_spinner_id);
        edEmail=findViewById(R.id.login_email_id);
        edPassword=findViewById(R.id.login_password_id);
        loginButton=findViewById(R.id.login_id);
        database=FirebaseDatabase.getInstance();
        //ref=database.getReference().child("Data").child("employees");

        final String[] campuses= new String[]{"Fatima Academy","National Institute of Education and Management", "The Knowledge school",
                "School of Nursing Education Bhakkar", "Institute of management", "Para veternary sciences bhakkar", "GU Campus Bhakkar"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, campuses);
//set the spinners adapter to the previously created one.
        campusSpinner.setAdapter(classAdopter);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String campus=campusSpinner.getSelectedItem().toString();
                ref=database.getReference().child("Data").child(campus).child("employees");
                final String email=edEmail.getText().toString();
                final String password=edPassword.getText().toString();

                if (TextUtils.isEmpty(email)){
                    edEmail.setError("Enter Email");
                    edEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    edPassword.setError("Enter Password");
                    edPassword.requestFocus();
                    return;
                }

                final ProgressDialog progressDialog=new ProgressDialog(LogIn.this);
                progressDialog.setMessage("Authenticating");
                progressDialog.show();

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                            //Map<String, String> map = (Map<String, String>) childSnapshot.getValue();
                            //Shop user = childSnapshot.getValue(Shop.class);
                            Employee employee=childSnapshot.getValue(Employee.class);
                            if(employee.getEmpEmail().equals(email) && employee.getEmpPassword().equals(password)) {

                                editor.putString("userState", "loggedIn");
                                editor.putString("campus",campus);
                                Gson gson = new Gson();
                                String json = gson.toJson(employee);
                                editor.putString("user", json);
                                editor.commit();
                                Campus.setCampusName(campus);
                                Toast.makeText(getApplicationContext(),"Log In Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LogIn.this, Splash1.class));
                                finish();
                            progressDialog.dismiss();
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

            }
        });
    }
}
