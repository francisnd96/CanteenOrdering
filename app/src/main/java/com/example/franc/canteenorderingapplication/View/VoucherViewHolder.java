package com.example.franc.canteenorderingapplication.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.R;

public class VoucherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView provider,name,info;

    private ClickListener cl;

    public VoucherViewHolder(View itemView) {
        super(itemView);

        name = (TextView)itemView.findViewById(R.id.voucher_name);
        provider = itemView.findViewById(R.id.voucher_provider);
        info = itemView.findViewById(R.id.voucher_info);


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
