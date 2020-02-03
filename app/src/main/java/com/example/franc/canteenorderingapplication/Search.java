package com.example.franc.canteenorderingapplication;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.Model.Category;
import com.example.franc.canteenorderingapplication.Model.Food;
import com.example.franc.canteenorderingapplication.View.CategoryViewHolder;
import com.example.franc.canteenorderingapplication.View.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Search extends android.support.v4.app.Fragment implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference ref;
    RecyclerView menu;
    RecyclerView.LayoutManager lm;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> fra;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_search, container, false);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Category");

        android.support.v7.widget.SearchView sv = (android.support.v7.widget.SearchView) rootView.findViewById (R.id.search_bar);

        //retrieve the search query
        String query = sv.getQuery().toString();

        //act on query submission
        sv.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent showResults = new Intent(getActivity(), SearchResults.class);
                showResults.putExtra("query", query);
                startActivity(showResults);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        //Set recycler view
        menu = (RecyclerView) rootView.findViewById(R.id.cat_recycler);
        menu.setHasFixedSize(true);
        lm = new GridLayoutManager(getContext(),2);
        menu.setLayoutManager(lm);

        //Initiate category Recycler View
        initMenu();
        return rootView;
    }

    private void initMenu() {
        fra = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class, R.layout.category_card, CategoryViewHolder.class, ref) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, Category model, final int position) {

                //Set name
                viewHolder.name.setText(model.getName());

                //Set image
                Picasso.with(getActivity().getBaseContext()).load(model.getImage()).into(viewHolder.picture);

                //Handle click
                final Category clicked = model;
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
    };
}
