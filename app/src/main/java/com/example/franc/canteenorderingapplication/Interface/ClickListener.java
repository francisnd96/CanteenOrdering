package com.example.franc.canteenorderingapplication.Interface;

import android.view.View;


//Interface used to click items generated by the FirebaseRecyclerAdapter
public interface ClickListener {
        void onClick(View v, int selection, boolean longClick);
    }
