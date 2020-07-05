package com.campus.management;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StudentAdopter extends RecyclerView.Adapter<StudentAdopter.MyViewHolder>  {

    private Context mcontext;
    private List<Student> students;
    //private List<Shop> shopList;
    //constructor
    public StudentAdopter(Context mcontext, List<Student> students) {
        this.mcontext = mcontext;
        this.students = students;
        // shopList = new ArrayList<>(mshop);


    }

    // View Holder Class which get the id from other layout to bind data
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView studentName, studentClass, studentFname , studentRegno;
       // CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            studentName = (TextView) view.findViewById(R.id.recycler_student_name_id);
            studentRegno = (TextView) view.findViewById(R.id.recycler_student_regno_id);
            studentFname=(TextView)view.findViewById(R.id.recycler_student_fname_id);
            studentClass = (TextView) view.findViewById(R.id.recycler_student_class_id);

         //   cardView = (CardView) view.findViewById(R.id.shop_cardView);

        }


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_card_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.studentName.setText(students.get(position).getName());
        holder.studentFname.setText(students.get(position).getFName());
        holder.studentClass.setText(students.get(position).getSClass());
        holder.studentRegno.setText(students.get(position).getRegNo());
        final Student student=students.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext, StudentProfile.class);
                intent.putExtra("profile", student);
                mcontext.startActivity(intent);
                ((Activity)mcontext).finish();
               // Toast.makeText(mcontext,student.getRegNo().toString(), Toast.LENGTH_SHORT).show();
               /* Intent intent=new Intent(mcontext, Splash.class);
                mcontext.startActivity(intent);*/
            }
        });
/*
        final String shop_phone = mshop.get(position).getShop_phone();
        final String shop_name = mshop.get(position).getShop_name();
        final String shop_location = mshop.get(position).getShop_location();
        final String shop_type = mshop.get(position).getShp_type();
        final String shop_image = mshop.get(position).getShop_image();
*/


     /*   holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, SecondActivity.class);
                intent.putExtra("PhoneNo", shop_phone);
                intent.putExtra("ShopName", shop_name);
                intent.putExtra("ShopLocation", shop_location);
                intent.putExtra("ShopType", shop_type);
                intent.putExtra("ShopImage", shop_image);

                mcontext.startActivity(intent);

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return students.size();
    }



    public void updateList(List<Student> studentList)
    {
        students=new ArrayList<>();
        students.addAll(studentList);
        notifyDataSetChanged();
    }
}