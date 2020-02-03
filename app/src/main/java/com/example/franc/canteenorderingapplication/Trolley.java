package com.example.franc.canteenorderingapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.franc.canteenorderingapplication.Database.Database;
import com.example.franc.canteenorderingapplication.Model.Order;
import com.example.franc.canteenorderingapplication.Model.PreviousOrders;
import com.example.franc.canteenorderingapplication.PayPal.PayPalConfig;
import com.example.franc.canteenorderingapplication.View.TrolleyAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class Trolley extends Fragment {

    FirebaseDatabase db;
    DatabaseReference req,req2;
    RecyclerView rv;
    RecyclerView.LayoutManager lm;
    public TextView thePrice, orderItemPrice, notice, savingText, saving;
    Button submitOrder;
    Button clearCart;
    FirebaseAuth auth;
    private static final int REQ_CODE = 1;
    Button voucher;
    Button removeVoucher;
    List<Order> orders = new ArrayList<>();
    TrolleyAdapter tAdapter;
    double totalPrice;
    String maxTime;
    String minTime;
    String voucherKey = "";
    public static double discount = 0;
    public static double reduction = 1;
    FirebaseUser user = null;

    private static PayPalConfiguration configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(PayPalConfig.CLIENTID);
    private String timeToPrepare;
    private double priceBeforeReduction;
    private double savings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_trolley, container, false);

        Intent intent = new Intent(getContext(),PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
        getActivity().startService(intent);

        Bundle b = this.getArguments();
        if(b != null) {
            discount = Integer.parseInt(b.getString("discount"));
            reduction = (100-discount)/100;
            voucherKey = b.getString("voucherKey");

        }


        db = FirebaseDatabase.getInstance();
        req = db.getReference("Requests");
        req2 = db.getReference("Voucher");

        rv = (RecyclerView)rootView.findViewById(R.id.orderList);
        rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        thePrice = (TextView) rootView.findViewById(R.id.totalCost);
        submitOrder = (Button) rootView.findViewById(R.id.submitOrder);
        clearCart = (Button) rootView.findViewById(R.id.clear_cart);
        orderItemPrice = (TextView) rootView.findViewById(R.id.orderItemPrice);
        voucher = rootView.findViewById(R.id.select_voucher);
        removeVoucher = rootView.findViewById(R.id.remove_voucher);
        notice = rootView.findViewById(R.id.voucher_notice);
        savingText = rootView.findViewById(R.id.saving_text);
        saving = rootView.findViewById(R.id.saving);

        voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),SelectVoucher.class));
            }
        });

        removeVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale l = new Locale("en","GB");
                NumberFormat format = NumberFormat.getCurrencyInstance(l);
                reduction = 1;
                discount = 0;
                notice.setText("");
                savingText.setText("");
                saving.setText("");
                totalPrice = 0;
                for(Order o : orders){
                    if(o.getQuantity() == null) {

                    }else{
                        totalPrice = totalPrice + (Double.parseDouble(o.getPrice())) * (Integer.parseInt(o.getQuantity()));
                    }
                }

                thePrice.setText(format.format(totalPrice));
            }
        });

        clearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database db = new Database(getActivity());

                //Call empty cart method
                db.emptyCart(FirebaseAuth.getInstance().getCurrentUser().getUid());

                //Restart activity
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(Trolley.this).attach(Trolley.this).commit();
            }
        });

        submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount = thePrice.getText().toString().replace("£","");

                PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount),"GBP","Payment for your order",PayPalPayment.PAYMENT_INTENT_SALE);

                //Start PayPal payment
                Intent pay = new Intent(getContext(), PaymentActivity.class);
                pay.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
                pay.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
                startActivityForResult(pay,REQ_CODE);



            }
        });

        initOrders();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE && resultCode == RESULT_OK){
            android.app.AlertDialog.Builder builder = null;
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if(confirm != null){
                try{
                    String payment = confirm.toJSONObject().toString(4);
                    JSONObject jsonObject = new JSONObject(payment);

                    builder = new android.app.AlertDialog.Builder(getContext());
                    builder.setTitle("Success");
                    Log.e("y",jsonObject.toString());
                        builder.setMessage("You have sucessfully paid " + thePrice.getText().toString() + "\n\nTransctionID: " + jsonObject.getJSONObject("response").getString("id") + "\n\n" +
                                "State: " + jsonObject.getJSONObject("response").getString("state") );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                builder.setCancelable(true);

                final String currentDate = new Date().toString();
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Database db = new Database(getActivity());
                        db.emptyCart(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(Trolley.this).attach(Trolley.this).commit();
                        Intent i = new Intent(getContext(),MainActivity.class);
                        i.putExtra("toTimer","toTimer");
                        i.putExtra("date", currentDate);
                        i.putExtra("TimeToPrepare",timeToPrepare);
                        startActivity(i);
                    }
                });

                android.app.AlertDialog a1 = builder.create();
                a1.show();
                req2.child(voucherKey).removeValue();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();



                PreviousOrders previousOrder = new PreviousOrders(user.getUid(),thePrice.getText().toString(),sdf.format(date),sdf2.format(date),orders,new Date().toString(),"no");
                req.child(currentDate).setValue(previousOrder);
                reduction = 1;
                discount = 0;
                notice.setText("");

            }else if(resultCode == Activity.RESULT_CANCELED){

            }else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){

            }
            
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initOrders() {

        //Retrieve order information
        orders = new Database(getActivity()).getCarts(FirebaseAuth.getInstance().getCurrentUser().getUid());


        tAdapter = new TrolleyAdapter(orders,this);
        rv.setAdapter(tAdapter);

        totalPrice = 0;
        timeToPrepare = "";


        //Iterate through order items and add up the prices
        for(Order o : orders){
            if(o.getQuantity() == null) {

            }else{
                totalPrice = totalPrice + (Double.parseDouble(o.getPrice())) * (Integer.parseInt(o.getQuantity()));
                timeToPrepare = timeToPrepare + " " + o.getTimeToPrepare();
            }
        }


        //Set the price with the correct locale information
        Locale l = new Locale("en","GB");
        NumberFormat format = NumberFormat.getCurrencyInstance(l);
        priceBeforeReduction = totalPrice;
        totalPrice = totalPrice* reduction;
        savings = priceBeforeReduction - totalPrice;
        DecimalFormat df = new DecimalFormat("#.00");
        String formattedSaving = df.format(savings);

        if(discount > 0){
            String s = Double.toString(discount);
            notice.setText(s+"% " + "discount applied");
            savingText.setText("Savings:  ");
            saving.setText("-£"+formattedSaving);
        }else{
            notice.setText("");
            savingText.setText("");
            saving.setText("");
        }

        thePrice.setText(format.format(totalPrice));
    }

    public double getReduction() {
        return reduction;
    }

    public void refreshTrolley(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(Trolley.this).attach(Trolley.this).commit();
    }

    public void applyVoucher(String s){
        double discount = Integer.parseInt(s);
        double reduction = (100-discount)/100;
        totalPrice = totalPrice*reduction;
        Locale l = new Locale("en","GB");
        NumberFormat format = NumberFormat.getCurrencyInstance(l);
        thePrice.setText(format.format(totalPrice));
    }
}
