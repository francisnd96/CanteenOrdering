package com.example.franc.canteenorderingapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.franc.canteenorderingapplication.Model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewFoodForm extends AppCompatActivity {

    EditText itemName, imageURL, longDescription, shortDescription, price, timeToPrepare;
    AutoCompleteTextView containsNuts,vegetarian;
    Button submit;
    FirebaseDatabase db;
    DatabaseReference req;

    String[] options = {"Yes","No"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food_form);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice,options);

        itemName = findViewById(R.id.item_name);
        imageURL = findViewById(R.id.image_url);
        longDescription = findViewById(R.id.long_description);
        shortDescription = findViewById(R.id.short_description);
        price = findViewById(R.id.new_price);
        timeToPrepare = findViewById(R.id.time_to_prepare);
        containsNuts = findViewById(R.id.contains_nuts);
        containsNuts.setAdapter(adapter);
        containsNuts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containsNuts.showDropDown();
            }
        });
        vegetarian = findViewById(R.id.vegetarian);
        vegetarian.setAdapter(adapter);
        vegetarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vegetarian.showDropDown();
            }
        });
        submit = findViewById(R.id.submit);

        db = FirebaseDatabase.getInstance();
        req = db.getReference("Food");

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
                req.child(new Date().toString()).setValue(food);
            }
        });
    }

}
