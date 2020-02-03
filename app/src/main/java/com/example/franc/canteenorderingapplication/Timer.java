package com.example.franc.canteenorderingapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.Model.Food;
import com.example.franc.canteenorderingapplication.Model.PreviousOrders;
import com.example.franc.canteenorderingapplication.Model.StarRating;
import com.example.franc.canteenorderingapplication.View.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Timer extends android.support.v4.app.Fragment implements RatingDialogListener {

    private TextView countdownText, text;
    private Button rate, back;
    private CountDownTimer cdt;
    private long timeLeftInMilliseconds = 600000;
    static String times = "";
    String id = "";
    TextView highTime, lowTime, welcomeText, dash;
    Calendar cal = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    static int low = 0;
    static int high = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.timer_activity, container, false);

        highTime = (TextView) rootView.findViewById(R.id.high_time);
        lowTime = (TextView) rootView.findViewById(R.id.low_time);
        welcomeText = rootView.findViewById(R.id.welcome_text);
        dash = rootView.findViewById(R.id.dash);
        text = rootView.findViewById(R.id.text);
        rate = rootView.findViewById(R.id.rate);
        back = rootView.findViewById(R.id.back);

        //on back pressed, return to the initial screen and reset the times string
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),MainActivity.class));
                times="";
            }
        });

        //define date format
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        Date date = new Date();

        cal.setTime(date);
        cal2.setTime(date);

        //retrieve arguments sent in bundle from MainActivity
        Bundle b = this.getArguments();
        if (b != null) {
            times = b.getString("times");
            id = b.getString("id");
        }

        //If an order is complete/not complete
        final DatabaseReference complete = FirebaseDatabase.getInstance().getReference("Requests").child(id).child("complete");
        complete.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String complete = dataSnapshot.getValue(String.class);
                if(complete != null){
                Log.e("y",complete);
                if(complete.equals("yes")) {
                    welcomeText.setText("Order Complete");
                    highTime.setText("");
                    lowTime.setText("");
                    dash.setText("");

                    //show rate or back buttons
                    text.setText("Please rate or go back to menu");
                    rate.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);

                    //app rating dialog box opens
                    rate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AppRatingDialog.Builder().setPositiveButtonText("Rate").setNegativeButtonText("Cancel").setNoteDescriptions(Arrays.asList("Terrible", "Not Good", "OK", "Very Good", "Fantastic")).setDefaultRating(3).setTitle("Rate this product").setDescription("Please rate this item give your feedback").setCommentBackgroundColor(R.color.colorPrimary).setHint("Comment here...").setWindowAnimation(R.style.RatingAnimation).create(getActivity()).show();
                        }
                    });
                }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(times.equals("")){
            welcomeText.setText("No pending order");
            text.setText("");

            //remove rate and back buttons
            rate.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            highTime.setText("");
            lowTime.setText("");
            dash.setText("");
        }else {

            //parse the list of times
            String[] split = times.split(" ");
            for (int i = 0; i < split.length; i++) {
                if (split[i].contains("-")) {
                    String min = split[i].substring(0, split[i].indexOf("-"));
                    low += Integer.parseInt(min);
                    String max = split[i].substring(3);
                    high += Integer.parseInt(max);
                    times = "done";
                }
            }

            //set the min and max expected times
            cal.add(Calendar.MINUTE, low);
            cal2.add(Calendar.MINUTE, high);
            text.setText("");
            rate.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            highTime.setText(sdf2.format(cal.getTime()));
            lowTime.setText(sdf2.format(cal2.getTime()));
        }
        return rootView;
    }


    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(final int star, @NotNull String s) {
//Create a star rating object

    }
}

