package com.campus.management;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FeeAdopter extends RecyclerView.Adapter<FeeAdopter.MyViewHolder>  {

    private Context mcontext;
    private List<Fee> fees;
    //private List<Shop> shopList;
    //constructor
    public FeeAdopter(Context mcontext, List<Fee> fees) {
        this.mcontext = mcontext;
        this.fees = fees;
        // shopList = new ArrayList<>(mshop);

    }

    // View Holder Class which get the id from other layout to bind data
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sName, sClass, feeType, feeAmount ;
        // CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            sName = (TextView) view.findViewById(R.id.fee_recycler_name_id);
            sClass=(TextView)view.findViewById(R.id.fee_recycler_class_id);
            feeType = (TextView) view.findViewById(R.id.fee_recycler_feetype_id);
            feeAmount = (TextView) view.findViewById(R.id.fee_recycler_amount_id);
            //expDesc=(TextView) view.findViewById(R.id.recycler_expense_description_id);


            //   cardView = (CardView) view.findViewById(R.id.shop_cardView);

        }


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fee_card_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.sName.setText(fees.get(position).getStudName());
        holder.sClass.setText(fees.get(position).getStdClass());
        holder.feeType.setText(fees.get(position).getFeeType());
        holder.feeAmount.setText(fees.get(position).getSubAmount());
        //holder.expDesc.setText(expenses.get(position).getExpDescription());
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
        return fees.size();
    }

}