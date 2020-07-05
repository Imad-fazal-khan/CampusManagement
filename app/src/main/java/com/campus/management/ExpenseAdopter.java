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

public class ExpenseAdopter extends RecyclerView.Adapter<ExpenseAdopter.MyViewHolder>  {

    private Context mcontext;
    private List<Expense> expenses;
    //private List<Shop> shopList;
    //constructor
    public ExpenseAdopter(Context mcontext, List<Expense> expenses) {
        this.mcontext = mcontext;
        this.expenses = expenses;
        // shopList = new ArrayList<>(mshop);

    }

    // View Holder Class which get the id from other layout to bind data
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView expId, expAmount, expDate, expDoneBy ;
       // CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            expId = (TextView) view.findViewById(R.id.recycler_expenseid_id);
            expAmount=(TextView)view.findViewById(R.id.recycler_expense_amount_id);
            expDate = (TextView) view.findViewById(R.id.recycler_expense_date_id);
            expDoneBy=view.findViewById(R.id.recycler_expense_madeby_id);
            //expDesc=(TextView) view.findViewById(R.id.recycler_expense_description_id);


         //   cardView = (CardView) view.findViewById(R.id.shop_cardView);

        }


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_card_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.expId.setText(expenses.get(position).getExpId());
        holder.expAmount.setText(expenses.get(position).getExpAmount());
        holder.expDate.setText(expenses.get(position).getExpDate());
        holder.expDoneBy.setText(expenses.get(position).getMadeBy());
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
        return expenses.size();
    }



    public void updateList(List<Expense> expenseList)
    {
        expenses=new ArrayList<>();
        expenses.addAll(expenseList);
        notifyDataSetChanged();
    }
}