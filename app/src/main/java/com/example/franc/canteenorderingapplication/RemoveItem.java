package com.example.franc.canteenorderingapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.franc.canteenorderingapplication.Database.Database;
import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.Model.Food;
import com.example.franc.canteenorderingapplication.View.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RemoveItem extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference ref;
    private TextView mTextMessage;
    RecyclerView menu;
    RecyclerView.LayoutManager lm;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> fra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Food");


        //Set recycler view
        menu = (RecyclerView) findViewById(R.id.recycler_menu);
        menu.setHasFixedSize(true);
        lm = new LinearLayoutManager(RemoveItem.this);
        menu.setLayoutManager(lm);

        //Initiate category Recycler View
        initMenu();
    }

    private void initMenu() {
        fra = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.remove_food_card, FoodViewHolder.class, ref) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, final int position) {

                //Set name
                viewHolder.name.setText(model.getName());
                viewHolder.shortDescription.setText(model.getShortDescription());
                viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent showItems = new Intent(RemoveItem.this, EditFoodForm.class);
                        showItems.putExtra("FoodID", fra.getRef(position).getKey());
                        startActivity(showItems);
                    }
                });

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        android.app.AlertDialog.Builder builder = null;
                        builder = new android.app.AlertDialog.Builder(RemoveItem.this);
                        builder.setTitle("Alert");
                        builder.setMessage("Are you sure you wish to remove this item. This cannot be undone.");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fra.getRef(position).removeValue();
                                dialog.cancel();
                                startActivity(getIntent());
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        android.app.AlertDialog a1 = builder.create();
                        a1.show();

                    }
                });

                //Set image
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.picture);

                //Handle click

            }
        };
        menu.setAdapter(fra);
    }
}
