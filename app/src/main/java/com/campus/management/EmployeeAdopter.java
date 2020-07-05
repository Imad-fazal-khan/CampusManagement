package com.campus.management;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdopter extends RecyclerView.Adapter<EmployeeAdopter.EmpViewHolder>  {

    private Context mcontext;
    private List<Employee> employees;
    //constructor
    public EmployeeAdopter(Context mcontext, List<Employee> employees) {
        this.mcontext = mcontext;
        this.employees = employees;
    }

    // View Holder Class which get the id from other layout to bind data
    public class EmpViewHolder extends RecyclerView.ViewHolder {
        public TextView empName, empEmail;

        public EmpViewHolder(View view) {
            super(view);
            empName = (TextView) view.findViewById(R.id.recycler_emp_name_id);
            empEmail = (TextView) view.findViewById(R.id.recycler_emp_email_id);
        }


    }

    @NonNull
    @Override
    public EmpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emp_card_layout, parent, false);

        return new EmpViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EmpViewHolder holder, final int position) {

        holder.empName.setText(employees.get(position).getEmpName());
        holder.empEmail.setText(employees.get(position).getEmpEmail());

        final Employee employee=employees.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext, UpdateEmployee.class);
                intent.putExtra("sentEmployee", employee);
                mcontext.startActivity(intent);
                ((Activity)mcontext).finish();

                /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mcontext);
                alertDialogBuilder.setTitle("Are You Sure!");
                alertDialogBuilder.setMessage("You Wants to Delete This User?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

}