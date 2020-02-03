package com.example.franc.canteenorderingapplication;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AccountActivity extends android.support.v4.app.Fragment implements NavigationView.OnNavigationItemSelectedListener {


    NavigationView nav1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_account, container, false);

        nav1 = (NavigationView) rootView.findViewById(R.id.accountOne);

        nav1.setNavigationItemSelectedListener(this);

        return rootView;
    }

    //listeners for the different options
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){

            case R.id.navigation_orders:
                startActivity(new Intent(getContext(),PastOrders.class));
            break;

            case R.id.navigation_vouchers:
                    startActivity(new Intent(getContext(),Voucher.class));
                break;

            case R.id.navigation_about:
                Toast.makeText(getContext(),"Canteen Ordering App by Francis. 2019",Toast.LENGTH_LONG).show();
                break;

            case R.id.navigation_logout:
                AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getActivity(),MainActivity.class));
                    }
                });
                break;


        }

        return true;
    }
}
