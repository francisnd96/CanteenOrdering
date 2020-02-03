package com.example.franc.canteenorderingapplication.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.R;

public class FoodDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView containsNuts;
    public TextView price;
    public TextView vegetarian;
    public TextView timeToPrepare;
    public TextView longDesc;
    public TextView shortDescription;
    public TextView name;
    public ImageView picture;

    private ClickListener cl;

    public FoodDetailViewHolder(View itemView) {
        super(itemView);

        name = (TextView)itemView.findViewById(R.id.itemDetailName);
        price = (TextView)itemView.findViewById(R.id.itemPrice);
        picture = (ImageView)itemView.findViewById(R.id.itemPicture);


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
