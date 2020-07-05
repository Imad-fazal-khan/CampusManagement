package com.campus.management;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentProfile extends AppCompatActivity {

    private Student student, sendObj;
    private EditText edClass, edRegNo, edFName, edSection, edDob, edGender, edPlaceDob, edReligion,
            edAddress, edfOccupation,   edNationality;
    //,edFlang, edMName,  edDesignation,edMCnic, edAddOW,edFCnin
    private TextView updateButton , edName, edPhone;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        initProfile();

        Intent intent=getIntent();
        sendObj=(Student) intent.getSerializableExtra("profile");

        setProfData();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog dialog = new ProgressDialog(StudentProfile.this);
                dialog.setCancelable(false);
                dialog.setMessage("Please wait...");
                dialog.show();

                student=new Student();
                getStudent();

                String clas=edClass.getText().toString();
                if(clas.equals("M.A Urdu")){
                    clas="MA Urdu";
                }else if(clas.equals("M.A eng")){
                    clas="MA eng";
                }else if(clas.equals("B.ed")){
                    clas="Bed";
                }else{

                }

                String campus=Campus.getCampusName();
                myRef = database.getReference().child("Data").child(campus).child("Students").child(clas);

                String str1=edName.getText().toString();
                //String str2=edFCnin.getText().toString();
                String str2=edPhone.getText().toString();
                String userKey=str1+str2;

                myRef.child(userKey).setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        Toast.makeText(StudentProfile.this, "Student Record Updated", Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), ViewStudents.class);
                        String str=sendObj.getSClass().toString();
                        intent.putExtra("className",str);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });
    }

    private void setProfData() {
        edClass.setText(sendObj.getSClass().toString());
        edRegNo.setText(sendObj.getRegNo().toString());
        edName.setText(sendObj.getName().toString());
        edFName.setText(sendObj.getFName().toString());
        edSection.setText(sendObj.getSection().toString());
        edDob.setText(sendObj.getDob().toString());
        edGender.setText(sendObj.getGender().toString());
        edPlaceDob.setText(sendObj.getPlaceDob().toString());
        edReligion.setText(sendObj.getReligion().toString());
        edAddress.setText(sendObj.getAddress().toString());
        edPhone.setText(sendObj.getPhone().toString());
        //edMName.setText(sendObj.getMName().toString());
        //edFCnin.setText(sendObj.getFCnin().toString());
        //edDesignation.setText(sendObj.getDesignation().toString());
        edfOccupation.setText(sendObj.getfOccupation().toString());
        //edMCnic.setText(sendObj.getMCnic().toString());
        //edAddOW.setText(sendObj.getAddOW().toString());
        edNationality.setText(sendObj.getNationality().toString());
        //edFlang.setText(sendObj.getFlang());
    }

    private void initProfile() {
        edClass=findViewById(R.id.prof_class_id);
        edRegNo=findViewById(R.id.prof_regno_id);
        edName=findViewById(R.id.prof_name_id);
        edFName=findViewById(R.id.prof_fname_id);
        edSection=findViewById(R.id.prof_section_id);
        edDob=findViewById(R.id.prof_dateofbirth_id);
        edGender=findViewById(R.id.prof_gender_id);
        edPlaceDob=findViewById(R.id.prof_placeofbirth_id);
        edReligion=findViewById(R.id.prof_religion_id);
        edAddress=findViewById(R.id.prof_address_id);
        edPhone=findViewById(R.id.prof_phone_id);
        //edFCnin=findViewById(R.id.prof_fnic_id);
        //edMCnic=findViewById(R.id.prof_mnic_id);
        //edMName=findViewById(R.id.prof_mname_id);
        //edDesignation=findViewById(R.id.prof_designation_id);
        edfOccupation=findViewById(R.id.prof_foccupation_id);
        //edAddOW=findViewById(R.id.prof_addofwork_id);
        edNationality=findViewById(R.id.prof_par_nationality_id);
        //edFlang=findViewById(R.id.prof_first_language_id);

        updateButton=findViewById(R.id.update_student_id);

    }


    private void getStudent() {
        String sClass = edClass.getText().toString();
        String regNo = edRegNo.getText().toString();
        String Name = edName.getText().toString();
        String fName = edFName.getText().toString();
        String section = edSection.getText().toString();
        String dob = edDob.getText().toString();
        String gender = edGender.getText().toString();
        String placeOB = edPlaceDob.getText().toString();
        String religion = edReligion.getText().toString();
        String address = edAddress.getText().toString();
        String phone = edPhone.getText().toString();
        //String mName = edMName.getText().toString();
        //String fCnic = edFCnin.getText().toString();
        //String designation = edDesignation.getText().toString();
        String fOccupation = edfOccupation.getText().toString();
        //String mCnic = edMCnic.getText().toString();
        //String addOW = edAddOW.getText().toString();
        String nationality = edNationality.getText().toString();
        //String fLang = edFlang.getText().toString();

        if (sClass.isEmpty()) {
            edClass.setError("Requires Class!");
            edClass.requestFocus();
            return;
        }
        if (regNo.isEmpty()) {
            edRegNo.setError("Requires Regiteration Number!");
            edRegNo.requestFocus();
            return;
        }
        if (Name.isEmpty()) {
            edName.setError("Requires Name!");
            edName.requestFocus();
            return;
        }
        if (fName.isEmpty()) {
            edFName.setError("Requires Father's Name!");
            edFName.requestFocus();
            return;
        }
        if (section.isEmpty()) {
            edSection.setError("Requires Section!");
            edSection.requestFocus();
            return;
        }
        if (dob.isEmpty()) {
            edDob.setError("Requires Date of Birth!");
            edDob.requestFocus();
            return;
        }
        if (gender.isEmpty()) {
            edGender.setError("Requires Gender!");
            edGender.requestFocus();
            return;
        }
        if (placeOB.isEmpty()) {
            edPlaceDob.setError("Requires place of birth!");
            edPlaceDob.requestFocus();
            return;
        }
        if (religion.isEmpty()) {
            edReligion.setError("Requires Religion!");
            edReligion.requestFocus();
            return;
        }
        if (address.isEmpty()) {
            edAddress.setError("Enter Address!");
            edAddress.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            edPhone.setError("Requires Phone");
            edPhone.requestFocus();
            return;
        }
/*        if (mName.isEmpty()) {
            edMName.setError("Requires Mother's Name!");
            edMName.requestFocus();
            return;
        }
        if (fCnic.isEmpty()) {
            edFCnin.setError("Requires Father's CNIC!");
            edFCnin.requestFocus();
            return;
        }
        if (designation.isEmpty()) {
            edDesignation.setError("Requires Designation!");
            edDesignation.requestFocus();
            return;
        }*/
        if (fOccupation.isEmpty()) {
            edfOccupation.setError("Need father's Occupation!");
            edfOccupation.requestFocus();
            return;
        }
/*        if (mCnic.isEmpty()) {
            edMCnic.setError("Requires Mother's CNIC!");
            edMCnic.requestFocus();
            return;
        }  // fOccupation, mCnic, addOW, nationality, fLang
        if (addOW.isEmpty()) {
            edAddOW.setError("Need Work Address!");
            edAddOW.requestFocus();
            return;
        }*/
        if (nationality.isEmpty()) {
            edNationality.setError("Need Nationality!");
            edNationality.requestFocus();
            return;
        }
  /*      if (fLang.isEmpty()) {
            edFlang.setError("Need First Language!");
            edFlang.requestFocus();
            return;
        }
*/
        student.setSClass(sClass);
        student.setRegNo(regNo);
        student.setName(Name);
        student.setFName(fName);
        student.setSection(section);
        student.setDob(dob);
        student.setGender(gender);
        student.setPlaceDob(placeOB);
        student.setReligion(religion);
        student.setAddress(address);
        student.setPhone(phone);
  //      student.setMName(mName);
    //    student.setFCnin(fCnic);
      //  student.setDesignation(designation);
        student.setfOccupation(fOccupation);
        //student.setMCnic(mCnic);
        //student.setAddOW(addOW);
        student.setNationality(nationality);
        //student.setFlang(fLang);
    }
}
