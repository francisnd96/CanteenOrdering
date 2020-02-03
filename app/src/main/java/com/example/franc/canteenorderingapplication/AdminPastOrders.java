package com.example.franc.canteenorderingapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.Model.PreviousOrders;
import com.example.franc.canteenorderingapplication.View.PreviousOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class AdminPastOrders extends android.support.v4.app.Fragment {

    FirebaseDatabase database;
    DatabaseReference ref;
    RecyclerView menu;
    RecyclerView.LayoutManager lm;
    FirebaseRecyclerAdapter<PreviousOrders, PreviousOrderViewHolder> fra;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_admin_past_orders, container, false);

        //retrieve database
        database = FirebaseDatabase.getInstance();

        //point at database location entitled Requests
        ref = database.getReference("Requests");

        //Set recycler view
        menu = (RecyclerView) rootView.findViewById(R.id.recycler_menu);
        menu.setHasFixedSize(true);
        lm = new LinearLayoutManager(getContext());
        menu.setLayoutManager(lm);

        //Initiate category Recycler View
        initMenu();

        return rootView;
    }

    private void initMenu() {
        fra = new FirebaseRecyclerAdapter<PreviousOrders,PreviousOrderViewHolder>(PreviousOrders.class, R.layout.admin_past_order_card, PreviousOrderViewHolder.class, ref.orderByChild("complete").equalTo("no")){

            @Override
            protected void populateViewHolder(PreviousOrderViewHolder viewHolder, final PreviousOrders model, final int position) {
                viewHolder.title.setText("Order #" + Integer.toString(position));
                viewHolder.date.setText("Date: " + model.getDate());
                viewHolder.price.setText("Cost: " + model.getTotal());
                viewHolder.time.setText("Time: "+ model.getTime());

                //when admin clicks complete
                viewHolder.complete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final DatabaseReference complete = FirebaseDatabase.getInstance().getReference("Requests").child(model.getTimeAndDate()).child("complete");
                        complete.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                //set the value of complete in the database to yes
                                complete.setValue("yes");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });



                final PreviousOrders clicked = model;
                viewHolder.setItemClickListener(new ClickListener() {
                    @Override
                    public void onClick(View v, int selection, boolean longClick) {

                        //Create intent to move to Item lists
                        Intent showItems = new Intent(getActivity(), PastOrderDetail.class);
                        showItems.putExtra("pastOrder", (Serializable) model.getOrder());
                        startActivity(showItems);
                    }
                });
            }
        };
        menu.setAdapter(fra);
    }

    }

