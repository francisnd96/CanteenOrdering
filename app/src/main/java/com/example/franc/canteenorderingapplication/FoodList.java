package com.example.franc.canteenorderingapplication;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.Model.Food;
import com.example.franc.canteenorderingapplication.View.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodList extends android.support.v4.app.Fragment implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference ref;
    private TextView mTextMessage;
    RecyclerView menu;
    RecyclerView.LayoutManager lm;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> fra;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_food_list, container, false);

        mTextMessage = (TextView) rootView.findViewById(R.id.message);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Food");


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
        fra = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_card, FoodViewHolder.class, ref) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, final int position) {

                //Set name
                viewHolder.name.setText(model.getName());
                viewHolder.shortDescription.setText(model.getShortDescription());
                viewHolder.timeToPrepare.setText(model.getTimeToPrepare() + " Mins");

                //Set image
                Picasso.with(getActivity().getBaseContext()).load(model.getImage()).into(viewHolder.picture);

                //Handle click
                final Food clicked = model;
                viewHolder.setItemClickListener(new ClickListener() {
                    @Override
                    public void onClick(View v, int selection, boolean longClick) {

                        //Create intent to move to Item lists
                        Intent showItems = new Intent(getActivity(), FoodDetail.class);
                        showItems.putExtra("FoodID", fra.getRef(position).getKey());
                        startActivity(showItems);
                    }
                });
            }
        };
        menu.setAdapter(fra);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
