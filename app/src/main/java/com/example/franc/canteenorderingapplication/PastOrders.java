package com.example.franc.canteenorderingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.Model.Food;
import com.example.franc.canteenorderingapplication.Model.PreviousOrders;
import com.example.franc.canteenorderingapplication.View.FoodViewHolder;
import com.example.franc.canteenorderingapplication.View.PreviousOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class PastOrders extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference ref;
    RecyclerView menu;
    RecyclerView.LayoutManager lm;
    String user;
    FirebaseRecyclerAdapter<PreviousOrders, PreviousOrderViewHolder> fra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_orders);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Requests");

        //Set recycler view
        menu = (RecyclerView) findViewById(R.id.recycler_menu);
        menu.setHasFixedSize(true);
        lm = new LinearLayoutManager(PastOrders.this);
        menu.setLayoutManager(lm);

        user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Initiate category Recycler View
        initMenu();
    }

    private void initMenu() {
        fra = new FirebaseRecyclerAdapter<PreviousOrders,PreviousOrderViewHolder>(PreviousOrders.class, R.layout.past_order_card, PreviousOrderViewHolder.class, ref.orderByChild("name").equalTo(user)){

            @Override
            protected void populateViewHolder(PreviousOrderViewHolder viewHolder, final PreviousOrders model, final int position) {
                viewHolder.title.setText("Order #" + Integer.toString(position));
                viewHolder.date.setText("Date: " + model.getDate());
                viewHolder.price.setText("Time: " + model.getTotal());
                viewHolder.time.setText("Cost: "+ model.getTime());



                final PreviousOrders clicked = model;
                viewHolder.setItemClickListener(new ClickListener() {
                    @Override
                    public void onClick(View v, int selection, boolean longClick) {

                        //Create intent to move to Item lists
                        Intent showItems = new Intent(PastOrders.this, PastOrderDetail.class);
                        showItems.putExtra("pastOrder", (Serializable) model.getOrder());
                        startActivity(showItems);
                    }
                });
            }
        };
        menu.setAdapter(fra);
    }
}
