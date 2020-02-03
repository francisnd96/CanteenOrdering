package com.example.franc.canteenorderingapplication.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.R;

public class PreviousOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView date, time, price, title, detail;
    public Button complete;

    private ClickListener cl;

    public PreviousOrderViewHolder(View itemView) {
        super(itemView);

        date = (TextView)itemView.findViewById(R.id.past_order_date);
        time = (TextView)itemView.findViewById(R.id.past_order_time);
        price = (TextView)itemView.findViewById(R.id.past_order_price);
        title = itemView.findViewById(R.id.past_order_title);
        complete = itemView.findViewById(R.id.complete);


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
