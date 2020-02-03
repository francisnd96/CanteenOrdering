package com.example.franc.canteenorderingapplication.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.R;

public class OrderDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name, quantity, price;

    private ClickListener cl;

    public OrderDetailViewHolder(View itemView) {
        super(itemView);

        name = (TextView)itemView.findViewById(R.id.order_detail_name);
        quantity = (TextView)itemView.findViewById(R.id.order_detail_quantity);
        price = (TextView)itemView.findViewById(R.id.order_detail_price);


        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ClickListener cl){
        this.cl = cl;
    }

    @Override
    public void onClick(View v) {
        cl.onClick(v,getAdapterPosition(),false);
    }
}
