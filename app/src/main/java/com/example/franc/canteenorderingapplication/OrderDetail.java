package com.example.franc.canteenorderingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.Model.Order;
import com.example.franc.canteenorderingapplication.Model.PreviousOrders;
import com.example.franc.canteenorderingapplication.View.OrderDetailViewHolder;
import com.example.franc.canteenorderingapplication.View.PreviousOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderDetail extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference ref;
    RecyclerView menu;
    RecyclerView.LayoutManager lm;
    FirebaseRecyclerAdapter<Order, OrderDetailViewHolder> fra;
    String orderId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        //retrieve the orderID that was sent with the intent to this Actviity
        if (getIntent() != null) {
            //Retrieve the Item ID
            orderId = getIntent().getStringExtra("OrderID");
        }

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Requests");

        //Set recycler view
        menu = (RecyclerView) findViewById(R.id.recycler_menu);
        menu.setHasFixedSize(true);
        lm = new LinearLayoutManager(OrderDetail.this);
        menu.setLayoutManager(lm);

        //Initiate category Recycler View
        initMenu();
    }

    private void initMenu() {
        fra = new FirebaseRecyclerAdapter<Order,OrderDetailViewHolder>(Order.class, R.layout.past_order_card, OrderDetailViewHolder.class, ref.orderByChild("order")){

            @Override
            protected void populateViewHolder(OrderDetailViewHolder viewHolder, final Order model, final int position) {

                viewHolder.name.setText(model.getProductName());
                viewHolder.quantity.setText("x"+model.getQuantity());
                viewHolder.price.setText(model.getPrice());


            }
        };
        menu.setAdapter(fra);
    }
}
