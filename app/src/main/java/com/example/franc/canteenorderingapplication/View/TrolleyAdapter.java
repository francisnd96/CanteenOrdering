package com.example.franc.canteenorderingapplication.View;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.franc.canteenorderingapplication.Database.Database;
import com.example.franc.canteenorderingapplication.FoodList;
import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.Model.Order;
import com.example.franc.canteenorderingapplication.R;
import com.example.franc.canteenorderingapplication.Trolley;
import com.google.firebase.auth.FirebaseAuth;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class TrolleyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView orderItem, orderItemPrice;
    public ElegantNumberButton count;
    public Button delete;
    public Spinner spinner;


    private ClickListener wcl;

    public void setOrderItemName(TextView name){
        orderItem = name;
    }

    public TrolleyViewHolder(View itemView) {
        super(itemView);

        orderItem = (TextView)itemView.findViewById(R.id.orderItem);
        orderItemPrice = (TextView)itemView.findViewById(R.id.orderItemPrice);
        count = (ElegantNumberButton) itemView.findViewById(R.id.number_button);
        delete = (Button) itemView.findViewById(R.id.delete_trolley_item);
    }

    @Override
    public void onClick(View view) {

    }
}

public class TrolleyAdapter extends RecyclerView.Adapter<TrolleyViewHolder> {

    private List<Order> data = new ArrayList<>();
    private Trolley trolley;
    double reduction = 0;

    public TrolleyAdapter(List<Order> data, Trolley trolley) {
        this.data = data;
        this.trolley = trolley ;
    }

    public TrolleyAdapter() {

    }

    @Override
    public TrolleyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(trolley.getContext());
        View itemView = inflater.inflate(R.layout.trolleycard,parent,false);
        return new TrolleyViewHolder(itemView);
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
        Log.e("t",Double.toString(reduction));
    }
    @Override
    public void onBindViewHolder(final TrolleyViewHolder holder, final int position) {
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order1 = data.get(position);
                new Database(trolley.getContext()).deleteTrolleyItem(order1);
                trolley.refreshTrolley();
            }
        });
        holder.count.setNumber(data.get(position).getQuantity());

        holder.count.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = data.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(trolley.getContext()).updateTrolley(order);
                double newPrice = Integer.parseInt(order.getQuantity())* Double.parseDouble(order.getPrice());
                Locale gb = new Locale("en","GB");
                NumberFormat formatting = NumberFormat.getCurrencyInstance(gb);
                holder.orderItemPrice.setText(formatting.format(newPrice));

                double total = 0;
                double beforeVoucherTotal = 0;
                double savings = 0;
                List<Order> orders = new Database(trolley.getContext()).getCarts(FirebaseAuth.getInstance().getCurrentUser().getUid());
                for(Order item: orders){
                    total += (Double.parseDouble(item.getPrice())) * (Integer.parseInt(item.getQuantity()));
                }
                Locale l = new Locale("en","GB");
                NumberFormat format = NumberFormat.getCurrencyInstance(l);
                Trolley t = new Trolley();
                beforeVoucherTotal = total;
                total = total * t.reduction;
                savings = beforeVoucherTotal - total;
                Log.d("e",Double.toString(t.reduction));


                trolley.thePrice.setText(format.format(total));
                if(savings > 0) {
                    trolley.saving.setText("-" + format.format(savings));
                }
            }
        });

        double total = (Double.parseDouble(data.get(position).getPrice()))*(Integer.parseInt(data.get(position).getQuantity()));

        Locale l = new Locale("en","GB");
        NumberFormat format = NumberFormat.getCurrencyInstance(l);

        holder.orderItemPrice.setText(format.format(total));
        holder.orderItem.setText(data.get(position).getProductName());
    }



    public double getReduction() {
        Log.e("yolo",Double.toString(reduction));
        return reduction;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

