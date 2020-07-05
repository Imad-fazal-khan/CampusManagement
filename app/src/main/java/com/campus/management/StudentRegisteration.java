package com.campus.management;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.Calendar;
import java.util.Locale;

public class StudentRegisteration extends AppCompatActivity {

    private EditText edRegNo, edName, edFName, edSection, edDob, edPlaceDob,
    edAddress, edPhone,   edfOccupation,    edNationality;
    //,edFlang, edMCnic,edMName, edFCnin,edAddOW,edDesignation,
    private TextView registerButton, searchStudentButton, toolbar;
    private CardView switchRegisterStudents, switchViewStudents;
    private Student student;
    private Spinner edClass, edGender, edReligion, classSelectionSpinner;
    private LinearLayout layout2, searchLayout;
    private RelativeLayout layout1;
    boolean bol;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registeration);

        initReg();


        final String campus=Campus.getCampusName();
        final Calendar myCalendar = Calendar.getInstance();

        SharedPreferences sharedPreferences=getSharedPreferences("myPref", MODE_PRIVATE);
        String campusName=sharedPreferences.getString("campus","");
        toolbar.setText("Student Reg: "+campusName);

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

                edDob.setText(sdf.format(myCalendar.getTime()));

            }

        };

        edDob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(StudentRegisteration.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //String[] classes = new String[]{"M.A Urdu", "M.A eng", "Msc Math", "MCS", "B.ed", "ADE", "BS sports science",
                //"BS eng", "BS math", "BS computer science", "BS islamic studies", "BBA", "DVAS 2years", "DVAS 3years"};
        String[] religions= new String[]{"Islam", "Hinduism", "Christianity", "Ahmadi", "Other religions"};
        String[] genders= new String[]{"Male", "Female", "Other"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        //ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
        ArrayAdapter<String> religionAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, religions);
        ArrayAdapter<String> gendersAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
//set the spinners adapter to the previously created one.

        if(campus.equalsIgnoreCase("National Institute of Education and Management")) {
            String[] classes = new String[]{"M.A Urdu", "M.A eng", "Msc Math", "MCS", "B.ed", "ADE", "BS sports science",
                    "BS eng", "BS math", "BS computer science", "BS islamic studies", "BBA", "DVAS 2years", "DVAS 3years"};
            ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
            edClass.setAdapter(classAdopter);
            classSelectionSpinner.setAdapter(classAdopter);

        }else if(campus.equalsIgnoreCase("Fatima Academy")){
            String[] classes = new String[]{"7th Male","8th Male","9th Male","10th Male","Fsc1 Male","Fsc11 Male","Bsc Male","7th Female",
                    "8th Female","9th Female","10th Female","Fsc1 Female","Fsc11 Female","Bsc Female"};
            ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
            edClass.setAdapter(classAdopter);
            classSelectionSpinner.setAdapter(classAdopter);
        }else if(campus.equalsIgnoreCase("The Knowledge school")){
            String[] classes = new String[]{"PG","Nursery", "Prep", "One", "Two", "Three", "4th", "5th", "6th boys", "6th girls",
                    "7th boys", "7th girls", "8th boys", "8th girls", "9th boys", "9th girls","10th boys", "10th girls"};
            ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
            edClass.setAdapter(classAdopter);
            classSelectionSpinner.setAdapter(classAdopter);
        }else if(campus.equalsIgnoreCase("School of Nursing Education Bhakkar")){
            String[] classes = new String[]{"RN","RM","LHV","CMW","CNA","LPA","FWW"};
            ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
            edClass.setAdapter(classAdopter);
            classSelectionSpinner.setAdapter(classAdopter);
        }else if(campus.equalsIgnoreCase("GU Campus Bhakkar")) {
            String[] classes = new String[]{"M.A Urdu", "M.A eng", "Msc Math", "MCS", "B.ed", "ADE", "BS sports science",
                    "BS eng", "BS math", "BS computer science", "BS islamic studies", "BBA", "DVAS 2years", "DVAS 3years"};
            ArrayAdapter<String> classAdopter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
            edClass.setAdapter(classAdopter);
            classSelectionSpinner.setAdapter(classAdopter);

        }

        edGender.setAdapter(gendersAdopter);
        edReligion.setAdapter(religionAdopter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                student=new Student();
                getStudent();
                String clas=edClass.getSelectedItem().toString();
                if(clas.equals("M.A Urdu")){
                    clas="MA Urdu";
                }else if(clas.equals("M.A eng")){
                    clas="MA eng";
                }else if(clas.equals("B.ed")){
                    clas="Bed";
                }else{

                }
                myRef = database.getReference().child("Data").child(campus).child("Students").child(clas);

                final ProgressDialog dialog = new ProgressDialog(StudentRegisteration.this);
                dialog.setCancelable(false);
                dialog.setMessage("Please wait...");
                dialog.show();

                String st=edRegNo.getText().toString();
                boolean bol1=isUnique(myRef,st);

                if(bol1==true){
                 edRegNo.setError("Already Present");
                 edRegNo.requestFocus();
                 dialog.dismiss();
                 return;
                }else{
                    String str1=edName.getText().toString();
                    //String str2=edFCnin.getText().toString();
                    String str2=edPhone.getText().toString();
                    String userKey=str1+str2;

                    myRef.child(userKey).setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            resetLaout();
                            Toast.makeText(StudentRegisteration.this, "Student Successfully Registered", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });

        switchViewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.GONE);
                searchLayout.setVisibility(View.VISIBLE);
                /*startActivity(new Intent(getApplicationContext(), ViewStudents.class));*/
            }
        });

        searchStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ViewStudents.class);
                String str=classSelectionSpinner.getSelectedItem().toString();
                intent.putExtra("className",str);
                startActivity(intent);
                finish();
            }
        });

        switchRegisterStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getStudent(){
        String sClass=edClass.getSelectedItem().toString();
        String regNo=edRegNo.getText().toString();
        String Name=edName.getText().toString();
        String fName=edFName.getText().toString();
        String section=edSection.getText().toString();
        String dob=edDob.getText().toString();
        String gender=edGender.getSelectedItem().toString();
        String placeOB=edPlaceDob.getText().toString();
        String religion=edReligion.getSelectedItem().toString();
        String address=edAddress.getText().toString();
        String phone=edPhone.getText().toString();
       // String mName=edMName.getText().toString();
        //String fCnic=edFCnin.getText().toString();
       // String designation=edDesignation.getText().toString();
        String fOccupation=edfOccupation.getText().toString();
        //String mCnic=edMCnic.getText().toString();
        //String addOW=edAddOW.getText().toString();
        String nationality=edNationality.getText().toString();
        //String fLang=edFlang.getText().toString();


        if(regNo.isEmpty()){
            edRegNo.setError("Requires Regiteration Number!");
            edRegNo.requestFocus();
            return;
        }
        if(Name.isEmpty()){
            edName.setError("Requires Name!");
            edName.requestFocus();
            return;
        }
        if(fName.isEmpty()){
            edFName.setError("Requires Father's Name!");
            edFName.requestFocus();
            return;
        }
        if(section.isEmpty()){
            edSection.setError("Requires Section!");
            edSection.requestFocus();
            return;
        }
        if(dob.isEmpty()){
            edDob.setError("Requires Date of Birth!");
            edDob.requestFocus();
            return;
        }

        if(placeOB.isEmpty()){
            edPlaceDob.setError("Requires place of birth!");
            edPlaceDob.requestFocus();
            return;
        }

        if(address.isEmpty()){
            edAddress.setError("Enter Address!");
            edAddress.requestFocus();
            return;
        }
        if(phone.isEmpty()){
            edPhone.setError("Requires Phone");
            edPhone.requestFocus();
            return;
        }
/*        if(mName.isEmpty()){
            edMName.setError("Requires Mother's Name!");
            edMName.requestFocus();
            return;
        }
        if(fCnic.isEmpty()){
            edFCnin.setError("Requires Father's CNIC!");
            edFCnin.requestFocus();
            return;
        }
        if(designation.isEmpty()){
            edDesignation.setError("Requires Designation!");
            edDesignation.requestFocus();
            return;
        }*/
        if(fOccupation.isEmpty()){
            edfOccupation.setError("Need father's Occupation!");
            edfOccupation.requestFocus();
            return;
        }
/*        if(mCnic.isEmpty()){
            edMCnic.setError("Requires Mother's CNIC!");
            edMCnic.requestFocus();
            return;
        }  // fOccupation, mCnic, addOW, nationality, fLang
        if(addOW.isEmpty()){
            edAddOW.setError("Need Work Address!");
            edAddOW.requestFocus();
            return;
        }*/
        if(nationality.isEmpty()){
            edNationality.setError("Need Nationality!");
            edNationality.requestFocus();
            return;
        }
  /*      if(fLang.isEmpty()){
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
/*
        if(!(sClass.isEmpty())&& !(Name.isEmpty())&& !(fName.isEmpty())&& !(section.isEmpty())&& !(dob.isEmpty())&& !(gender.isEmpty())
                && !(placeOB.isEmpty())&& !(religion.isEmpty())&& !(address.isEmpty())&& !(mName.isEmpty())&& !(fCnic.isEmpty())
                && !(designation.isEmpty())&& !(fOccupation.isEmpty())&& !(mCnic.isEmpty())&& !(addOW.isEmpty())&& !(nationality.isEmpty())&& !(fLang.isEmpty())) {*/
            //student = new Student(sClass, Name, fName, section, dob, gender, placeOB, religion, address, phone, mName,
        // fCnic, designation, fOccupation, mCnic, addOW, nationality, fLang);
        //}
    }
    private void initReg() {
        classSelectionSpinner=findViewById(R.id.select_class_spinner_id);
        searchStudentButton=findViewById(R.id.student_search_button_id);
        searchLayout=findViewById(R.id.registeration_search_layout);
        edClass=findViewById(R.id.std_reg_class_id);
        edRegNo=findViewById(R.id.std_reg_regno_id);
        edName=findViewById(R.id.std_reg_name_id);
        edFName=findViewById(R.id.std_reg_fname_id);
        edSection=findViewById(R.id.std_reg_section_id);
        edDob=findViewById(R.id.std_reg_dateofbirth_id);
        edGender=findViewById(R.id.std_reg_gender_id);
        edPlaceDob=findViewById(R.id.std_reg_placeofbirth_id);
        edReligion=findViewById(R.id.std_reg_religion_id);
        edAddress=findViewById(R.id.std_reg_address_id);
        edPhone=findViewById(R.id.std_reg_phone_id);
        toolbar=findViewById(R.id.student_reg_toolbar_id);
        //edFCnin=findViewById(R.id.std_reg_fnic_id);
        //edMCnic=findViewById(R.id.std_reg_mnic_id);
        //edMName=findViewById(R.id.std_reg_mname_id);
        //edDesignation=findViewById(R.id.std_reg_designation_id);
        edfOccupation=findViewById(R.id.std_reg_foccupation_id);
        //edAddOW=findViewById(R.id.std_reg_addofwork_id);
        edNationality=findViewById(R.id.std_reg_par_nationality_id);
        //edFlang=findViewById(R.id.std_reg_first_language_id);

        registerButton=findViewById(R.id.register_student_id);
        switchRegisterStudents=findViewById(R.id.register_student_switch_id);
        switchViewStudents=findViewById(R.id.view_student_switch_id);

        layout1=findViewById(R.id.student_layoutt1);
        layout2=findViewById(R.id.student_layoutt2);
    }

    private void resetLaout(){
                edRegNo.setText("");
                edName.setText("");
                edFName.setText("");
                edSection.setText("");
                edDob.setText("");
                edPlaceDob.setText("");
                edAddress.setText("");
                edPhone.setText("");
          //      edMName.setText("");
            //    edFCnin.setText("");
              //  edDesignation.setText("");
                edfOccupation.setText("");
                //edMCnic.setText("");
                //edAddOW.setText("");
                edNationality.setText("");
                //edFlang.setText("");
    }

    private boolean isUnique(DatabaseReference reference, final String st) {

        bol=false;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    //Map<String, String> map = (Map<String, String>) childSnapshot.getValue();
                    //Shop user = childSnapshot.getValue(Shop.class);
                    Student student = childSnapshot.getValue(Student.class);
                    if (student.getRegNo().equals(st)) {
                        bol=true;
                    }
                    //   progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

                //progressDialog.dismiss();
            }
        });
    return bol;
}
}