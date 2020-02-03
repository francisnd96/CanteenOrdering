package com.example.franc.canteenorderingapplication.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView containsNuts;
    public TextView price;
    public TextView vegetarian;
    public TextView timeToPrepare;
    public TextView longDesc;
    public TextView shortDescription;
    public TextView name;
    public ImageView picture;
    public Button edit,delete,promo;

    private ClickListener cl;

    public FoodViewHolder(View itemView) {
        super(itemView);

        name = (TextView)itemView.findViewById(R.id.menu_name);
        shortDescription = (TextView)itemView.findViewById(R.id.menu_shortDesc);
        picture = (ImageView)itemView.findViewById(R.id.menu_image);
        timeToPrepare = (TextView)itemView.findViewById(R.id.menu_timeToPrepare);
        edit = itemView.findViewById(R.id.edit_item);
        delete = itemView.findViewById(R.id.delete_item);
        promo = itemView.findViewById(R.id.promo);

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
