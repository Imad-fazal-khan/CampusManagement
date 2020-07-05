package com.campus.management;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FeeStructAdopter extends RecyclerView.Adapter<FeeStructAdopter.MyViewHolder1>  {

    private Context mcontext;
    private List<FeeStruct> feeStructs;
    //private List<Shop> shopList;
    //constructor
    public FeeStructAdopter(Context mcontext, List<FeeStruct> students) {
        this.mcontext = mcontext;
        this.feeStructs = students;
        // shopList = new ArrayList<>(mshop);


    }

    // View Holder Class which get the id from other layout to bind data
    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        public TextView tvClass, tvReg, tvAdmission, tvSecurity, tvTution;
        // CardView cardView;

        public MyViewHolder1(View view) {
            super(view);
            tvClass = (TextView) view.findViewById(R.id.struct_class_id);
            tvReg = (TextView) view.findViewById(R.id.struct_reg_fee_id);
            tvAdmission = (TextView) view.findViewById(R.id.struct_adm_charges_id);
            tvSecurity = (TextView) view.findViewById(R.id.struct_security_charges_id);
            tvTution = (TextView) view.findViewById(R.id.struct_monthly_fee_id);

            //   cardView = (CardView) view.findViewById(R.id.shop_cardView);

        }


    }

    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fee_struct_card_layout, parent, false);

        return new MyViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder1 holder, final int position) {

        holder.tvClass.setText(feeStructs.get(position).getStructClass());
        holder.tvAdmission.setText(feeStructs.get(position).getAdmissionCharges());
        holder.tvSecurity.setText(feeStructs.get(position).getSecurityCharges());
        holder.tvReg.setText(feeStructs.get(position).getRegistrationFee());
        holder.tvTution.setText(feeStructs.get(position).getMonthlyTutionFee());
       // final Student student=feeStructs.get(position);

        final FeeStruct employee=feeStructs.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Intent intent=new Intent(mcontext, UpdateStruct.class);
                        intent.putExtra("sentStruct", employee);
                        mcontext.startActivity(intent);
                        ((Activity)mcontext).finish();

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
        return feeStructs.size();
    }



/*    public void updateList(List<Student> studentList)
    {
        students=new ArrayList<>();
        students.addAll(studentList);
        notifyDataSetChanged();
    }*/
}