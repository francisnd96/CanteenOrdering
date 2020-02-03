package com.example.franc.canteenorderingapplication;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.franc.canteenorderingapplication.Model.StarRating;
import com.facebook.FacebookSdk;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RatingDialogListener {

    private static final int RC_SIGN_IN = 123;
    FirebaseDatabase db;
    DatabaseReference ratingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        db = FirebaseDatabase.getInstance();
        ratingRef = db.getReference("StarRating");

        if(getIntent().getStringExtra("toBasket") != null){
            Bundle b = new Bundle();
            b.putString("discount",getIntent().getStringExtra("discount"));
            b.putString("voucherKey",getIntent().getStringExtra("voucherKey"));
            Trolley t = new Trolley();
            t.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,t).commit();
        }else if(getIntent().getStringExtra("toTimer") != null){
            Timer t = new Timer();
            Bundle b = new Bundle();
            b.putString("times",getIntent().getStringExtra("TimeToPrepare"));
            b.putString("id",getIntent().getStringExtra("date"));
            t.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,t).commit();
        }else if(getIntent().getStringExtra("toBasketFromPastOrder") != null){
            Trolley t = new Trolley();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,t).commit();
        }else {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() == null) {
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()
                );

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);
            }


            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FoodList()).commit();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.e("name",user.getUid());
                if(user.getUid().equals("fo0SYjcJ75WWJyvfglmUMoZ1DEx1")){
                    startActivity(new Intent(MainActivity.this,AdminActivity.class));
                }
                                //startActivity(new Intent(MainActivity.this, FoodList.class));
                //finish();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Toast.makeText(this,"Failed",Toast.LENGTH_LONG);
            }
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.support.v4.app.Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                     selectedFragment = new FoodList();
                     break;
                case R.id.navigation_dashboard:
                    selectedFragment = new Timer();
                    break;
                case R.id.navigation_search:
                    selectedFragment = new Search();
                    break;
                case R.id.navigation_orders:
                    selectedFragment = new Trolley();
                    break;
                case R.id.navigation_account:
                    selectedFragment = new AccountActivity();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

            return true;
        }
    };

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(final int i, @NotNull String s) {
        final StarRating rate = new StarRating(FirebaseAuth.getInstance().getCurrentUser().getUid(),"",String.valueOf(i),s);

        //Save it in the database with its date/time
        ratingRef.child(new Date().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ratingRef.child(new Date().toString()).setValue(rate);
                Toast.makeText(MainActivity.this,"Thank you for your feedback!",Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FoodList()).commit();
                Timer.times="";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
