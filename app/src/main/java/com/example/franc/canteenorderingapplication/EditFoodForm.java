package com.example.franc.canteenorderingapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.franc.canteenorderingapplication.Model.Food;
import com.example.franc.canteenorderingapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class EditFoodForm extends AppCompatActivity {

    EditText itemName, imageURL, longDescription, shortDescription, price, timeToPrepare;
    AutoCompleteTextView containsNuts,vegetarian;
    Button submit;
    FirebaseDatabase db;
    DatabaseReference req;
    String foodId;

    String[] options = {"Yes","No"};
    Food foodItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food_form);

        if (getIntent() != null) {
            //Retrieve the Item ID
            foodId = getIntent().getStringExtra("FoodID");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice,options);

        itemName = findViewById(R.id.item_name);
        imageURL = findViewById(R.id.image_url);
        longDescription = findViewById(R.id.long_description);
        shortDescription = findViewById(R.id.short_description);
        price = findViewById(R.id.new_price);
        timeToPrepare = findViewById(R.id.time_to_prepare);
        containsNuts = findViewById(R.id.contains_nuts);
        containsNuts.setAdapter(adapter);
        containsNuts.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    containsNuts.showDropDown();
                }
            }
        });
        containsNuts.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                containsNuts.showDropDown();
                return false;
            }
        });
        vegetarian = findViewById(R.id.vegetarian);
        vegetarian.setAdapter(adapter);
        vegetarian.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    vegetarian.showDropDown();
            }
        });
        vegetarian.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                vegetarian.showDropDown();
                return false;
            }
        });
        submit = findViewById(R.id.submit);

        db = FirebaseDatabase.getInstance();
        req = db.getReference("Food");

        //retrieve all details regarding a food item
        req.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodItem = dataSnapshot.getValue(Food.class);

                itemName.setText(foodItem.getName());
                imageURL.setText(foodItem.getImage());
                longDescription.setText(foodItem.getLongDescription());
                shortDescription.setText(foodItem.getShortDescription());
                price.setText(foodItem.getPrice());
                timeToPrepare.setText(foodItem.getTimeToPrepare());
                containsNuts.setText(foodItem.getContainsNuts());
                vegetarian.setText(foodItem.getVegetarian());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = itemName.getText().toString().trim();
                String image = imageURL.getText().toString().trim();
                String longDesc = longDescription.getText().toString().trim();
                String shortDesc = shortDescription.getText().toString().trim();
                String cost = price.getText().toString().trim();
                String ttp = timeToPrepare.getText().toString().trim();
                String nuts = containsNuts.getText().toString().trim();
                String veg = vegetarian.getText().toString().trim();

                Food food = new Food(nuts,image,longDesc,shortDesc,name,cost,ttp,veg,"01");
                req.child(foodId).setValue(food);
            }
        });
    }

}
