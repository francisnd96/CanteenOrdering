package com.example.franc.canteenorderingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.franc.canteenorderingapplication.Interface.ClickListener;
import com.example.franc.canteenorderingapplication.Model.Food;
import com.example.franc.canteenorderingapplication.Model.VoucherModel;
import com.example.franc.canteenorderingapplication.View.TrolleyAdapter;
import com.example.franc.canteenorderingapplication.View.VoucherViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SelectVoucher extends AppCompatActivity {

    Button addVoucher;
    FirebaseDatabase database;
    DatabaseReference ref;
    RecyclerView menu;
    RecyclerView.LayoutManager lm;
    FirebaseRecyclerAdapter<VoucherModel, VoucherViewHolder> fra;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_voucher);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Voucher");
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //Set recycler view
        menu = (RecyclerView) findViewById(R.id.cat_recycler);
        menu.setHasFixedSize(true);
        lm = new LinearLayoutManager(SelectVoucher.this);
        menu.setLayoutManager(lm);

        //Initiate category Recycler View


        addVoucher = (Button) findViewById(R.id.add_voucher);

        addVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectVoucher.this,AddVoucher.class));
            }
        });
        initMenu();
    }

    private void initMenu() {
        fra = new FirebaseRecyclerAdapter<VoucherModel,VoucherViewHolder>(VoucherModel.class, R.layout.voucher_card, VoucherViewHolder.class, ref.orderByChild("user").equalTo(user)){

            @Override
            protected void populateViewHolder(VoucherViewHolder viewHolder, final VoucherModel model, final int position) {
                viewHolder.name.setText(model.getTitle());
                viewHolder.info.setText(model.getDateExpiration() + " " + model.getTimeExpiration());

                //Handle click
                final VoucherModel clicked = model;
                viewHolder.setItemClickListener(new ClickListener() {
                    @Override
                    public void onClick(View v, int selection, boolean longClick) {




                        //Create intent to move to Item lists
                        Intent showItems = new Intent(SelectVoucher.this, MainActivity.class);
                        showItems.putExtra("toBasket","yes");
                        showItems.putExtra("discount", model.getPercentOff());
                        showItems.putExtra("voucherKey",fra.getRef(position).getKey());
                        startActivity(showItems);
                    }
                });

            }
        };
        menu.setAdapter(fra);
    }
}
