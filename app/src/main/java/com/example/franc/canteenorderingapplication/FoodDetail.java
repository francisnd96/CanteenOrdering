package com.example.franc.canteenorderingapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.franc.canteenorderingapplication.Database.Database;
import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.Model.Food;
import com.example.franc.canteenorderingapplication.Model.Order;
import com.example.franc.canteenorderingapplication.View.FoodViewHolder;
import com.example.franc.canteenorderingapplication.View.SuggestionViewHolder;
import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class FoodDetail extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView name,price,description, sugName;
    ImageView image, sugImage;
    CollapsingToolbarLayout ctl;
    FloatingActionButton trolley;
    NumberPicker np;

    static String quantity;
    String foodId = "";
    FirebaseDatabase db;
    DatabaseReference ref;
    Food foodItem;
    Food foodItem2;


    RecyclerView suggestions;
    RecyclerView.LayoutManager lm;
    FirebaseRecyclerAdapter<Food, SuggestionViewHolder> fra;
    NavigationView nav1;

    CallbackManager cbm;
    ShareDialog sd;

    Target target = new Target(){

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto pic = new SharePhoto.Builder().setBitmap(bitmap).build();
            if(ShareDialog.canShow(SharePhotoContent.class)){
                SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(pic).build();
                sd.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Food");

        cbm = CallbackManager.Factory.create();
        sd = new ShareDialog(this);


        trolley = (FloatingActionButton) findViewById(R.id.trolley);
        description = (TextView) findViewById(R.id.description);
        name = (TextView) findViewById(R.id.itemDetailName);
        price = (TextView) findViewById(R.id.itemPrice);
        image = (ImageView) findViewById(R.id.itemPicture);
        sugImage = findViewById(R.id.suggestion_image);
        sugName = findViewById(R.id.suggestion_name);

        if (getIntent() != null) {
            //Retrieve the Item ID
            foodId = getIntent().getStringExtra("FoodID");
        }

        nav1 = (NavigationView) findViewById(R.id.fav_button);
        nav1.setNavigationItemSelectedListener(this);


        final TextView tv = (TextView) findViewById(R.id.tv);
        np = (NumberPicker) findViewById(R.id.np);

        //Populate NumberPicker values from minimum and maximum value range
        //Set the minimum value of NumberPicker
        np.setMinValue(1);
        //Specify the maximum value/number of NumberPicker
        np.setMaxValue(10);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        np.setWrapSelectorWheel(true);

        //Display the value of the number picker
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
                quantity = Integer.toString(newVal);
                tv.setText("Quantity: " + newVal);
            }
        });




        ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);



        trolley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //If no quantity has been selected
                if (quantity == null) {
                    Toast.makeText(FoodDetail.this, "Please select a quantity", Toast.LENGTH_SHORT).show();
                } else {
                    //Add to database
                    new Database(getBaseContext()).putInCart(new Order(
                            foodId, foodItem.getName(), quantity, foodItem.getPrice(),foodItem.getTimeToPrepare()), FirebaseAuth.getInstance().getCurrentUser().getUid()
                    );

                    Toast.makeText(FoodDetail.this, "Added to your cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ref.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foodItem = dataSnapshot.getValue(Food.class);

                //Set Image
                Picasso.with(getBaseContext()).load(foodItem.getImage()).into(image);

                //Set Price
                price.setText(foodItem.getPrice());

                //Set Name
                name.setText(foodItem.getName());

                //Set Description
                description.setText(foodItem.getLongDescription());

                ref.child(foodItem.getSuggestionId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        foodItem2 = dataSnapshot.getValue(Food.class);
                        Picasso.with(getBaseContext()).load(foodItem2.getImage()).into(sugImage);
                        sugName.setText(foodItem2.getName());

                        sugImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent showItems = new Intent(getIntent());
                                showItems.putExtra("FoodID", foodItem.getSuggestionId());
                                startActivity(showItems);
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.navigation_share:
                Picasso.with(getApplicationContext()).load(foodItem.getImage()).into(target);
                break;
        }
        return true;
    }
}
