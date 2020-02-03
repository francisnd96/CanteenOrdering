package com.example.franc.canteenorderingapplication.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name;
    public ImageView picture;

    private ClickListener cl;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        name = (TextView)itemView.findViewById(R.id.cat_name);
        picture = (ImageView)itemView.findViewById(R.id.cat_image);


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
