package com.example.franc.canteenorderingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.Model.Food;
import com.example.franc.canteenorderingapplication.View.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchResults extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference ref;
    RecyclerView menu;
    RecyclerView.LayoutManager lm;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> fra;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Food");

        if(getIntent() != null){
            //Retrieve the Item ID
            query = getIntent().getStringExtra("query");
        }

        Log.d("here",query);
        //Set recycler view
        menu = (RecyclerView) findViewById(R.id.recycler_menu);
        menu.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        menu.setLayoutManager(lm);

        //Initiate category Recycler View
        initMenu();
    }

    private void initMenu() {
        fra = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_card, FoodViewHolder.class, ref.orderByChild("Name").startAt(query.toUpperCase()).endAt(query.toLowerCase()+"\uf8ff")) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, final int position) {

                //Set name
                viewHolder.name.setText(model.getName());
                viewHolder.shortDescription.setText(model.getShortDescription());
                viewHolder.timeToPrepare.setText(model.getTimeToPrepare() + " Mins");

                //Set image
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.picture);

                //Handle click
                final Food clicked = model;
                viewHolder.setItemClickListener(new ClickListener() {
                    @Override
                    public void onClick(View v, int selection, boolean longClick) {

                        //Create intent to move to Item lists
                        Intent showItems = new Intent(SearchResults.this, FoodDetail.class);
                        showItems.putExtra("FoodID", fra.getRef(position).getKey());
                        startActivity(showItems);
                    }
                });
            }
        };
        menu.setAdapter(fra);
    }
}
