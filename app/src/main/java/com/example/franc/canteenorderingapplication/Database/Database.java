package com.example.franc.canteenorderingapplication.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.franc.canteenorderingapplication.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

//SQLiteAssetHelper helps us to connect our database file to our Android project
public class Database extends SQLiteAssetHelper {

    //The name of the database we will access
    private static final String DB_NAME="CanteenDB3.db";

    //Version of the database
    private static final int DB_VER=1;

    //Database class constructor
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    //Method to retrieve Shopping Trolley
    public List<Order> getCarts(String user){

        //Retrieve Database
        SQLiteDatabase db = getReadableDatabase();

        //Used to build SQL queries
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        //A string array with the names of the fields we will be accessing in the database
        String [] sqlSelect ={"ID","ProductName","ProductId","Quantity", "Price","TimeToPrepare","User"};

        //The name of the SQL table
        String sqlTable = "OrderDetail";

        //Set the query to operate on this table
        qb.setTables(sqlTable);

        //Cursor used to iterate through results of query
        //This query will select all the items detailed in the string array for a particular user
        Cursor c = qb.query(db,sqlSelect,"User=?",new String[]{user},null,null,null);

        //Create list to house each individual item the user has put in his cart
        final List<Order> result = new ArrayList<>();

        //Iterate through query results and create new Order objects with the results
        if(c.moveToFirst()){
            do{
                result.add(new Order(
                        c.getInt(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("TimeToPrepare")),
                        c.getString(c.getColumnIndex("User"))));
            }while (c.moveToNext());
        }
        return result;
    }

    //Method used to add items to cart
    public void putInCart(Order order, String user){

        //Retrieve readable database
        SQLiteDatabase db = getReadableDatabase();

        //SQL Query to insert item into database
        String query = String.format("Insert or replace into OrderDetail(ProductId,ProductName,Quantity,Price,TimeToPrepare,User) values " +
                "('%s','%s','%s','%s','%s','%s');",order.getProductId(),order.getProductName(),order.getQuantity(),order.getPrice(),order.getTimeToPrepare(),user);

        //Execute the query
        db.execSQL(query);
    }

    //Method used to empty cart
    public void emptyCart(String user){

        //Retrieve readable database
        SQLiteDatabase db = getReadableDatabase();

        //SQL query to remove from database
        String query = String.format("delete from OrderDetail where User = '%s'",user);

        //Execute the query
        db.execSQL(query);
    }

    public void updateTrolley(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("update OrderDetail set Quantity = %s where id = %d",order.getQuantity(),order.getID());
        db.execSQL(query);

    }

    public void deleteTrolleyItem(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("delete from OrderDetail where id = %d",order.getID());
        db.execSQL(query);

    }
}
