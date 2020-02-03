package com.example.franc.canteenorderingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.franc.canteenorderingapplication.Database.Database;
import com.example.franc.canteenorderingapplication.Model.Order;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class PastOrderDetail extends AppCompatActivity {

    List<Order> list;
    TextView detail;
    Button reorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_order_detail);

        Intent intent = getIntent();
        list = (List<Order>) intent.getSerializableExtra("pastOrder");

        detail = findViewById(R.id.past_order_detail);

        String s = "";

        for(int i = 0 ; i < list.size(); i++){
            s = s+list.get(i).getProductName()+ "\t" + "x"+list.get(i).getQuantity() + "\n\n";
        }

        detail.setText(s);

        reorder = findViewById(R.id.reorder);
        reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database db = new Database(PastOrderDetail.this);
                db.emptyCart(FirebaseAuth.getInstance().getCurrentUser().getUid());
                for(int i = 0; i < list.size(); i++){
                    db.putInCart(list.get(i),FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
                Intent showItems = new Intent(PastOrderDetail.this, MainActivity.class);
                showItems.putExtra("toBasketFromPastOrder","yes");
                startActivity(showItems);

            }
        });
    }
}
