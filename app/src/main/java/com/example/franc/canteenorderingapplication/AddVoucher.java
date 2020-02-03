package com.example.franc.canteenorderingapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.franc.canteenorderingapplication.Model.PreviousOrders;
import com.example.franc.canteenorderingapplication.Model.VoucherModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddVoucher extends AppCompatActivity {

    EditText voucher;
    Button submit;
    FirebaseDatabase db;
    DatabaseReference req;
    String expiryDate;
    Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voucher);

        //retrieve instance of database
        db = FirebaseDatabase.getInstance();

        //point to the location in the database entitled "Voucher"
        req = db.getReference("Voucher");

        //define date format
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yy");

        //retrieve current date
        Date date = new Date();
        cal.setTime(date);

        //add 30 days to date
        cal.add(Calendar.DATE,30);

        //set expiry date
        expiryDate = sdf2.format(cal.getTime());

        voucher = (EditText) findViewById(R.id.voucher_code);

        submit = (Button) findViewById(R.id.submit_voucher);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = voucher.getText().toString().trim().toUpperCase();

                //if code is correct
                if(code.equals("SAVE10")){

                    //create voucher
                    VoucherModel voucher = new VoucherModel("10","Save 10% off order. Conditions apply","Save 10% off any combination of food","23:59",expiryDate,FirebaseAuth.getInstance().getCurrentUser().getUid());

                    //add to database
                    req.child(new Date().toString()).setValue(voucher);

                    //success dialog box
                    final AlertDialog alertDialog = new AlertDialog.Builder(AddVoucher.this).create();
                    alertDialog.setTitle("Success");
                    alertDialog.setMessage("Your voucher has successfully been added");

                    alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                            startActivity(new Intent(AddVoucher.this,SelectVoucher.class));
                        }
                    });
                    alertDialog.show();
                }
            }
        });
    }
}
