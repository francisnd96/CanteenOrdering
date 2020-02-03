package com.example.franc.canteenorderingapplication.Model;

import java.io.Serializable;

public class Order implements Serializable {
    private String ProductId,ProductName,Quantity,Price,TimeToPrepare,User;
    private int ID;

    public Order() {
    }

    public Order(String productId, String productName, String quantity, String price, String timeToPrepare) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        TimeToPrepare = timeToPrepare;
    }

    public Order(int ID, String productId, String productName, String quantity, String price, String timeToPrepare) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        TimeToPrepare = timeToPrepare;
        this.ID = ID;
    }

    public Order(int ID, String productId, String productName, String quantity, String price, String timeToPrepare,String user) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        TimeToPrepare = timeToPrepare;
        this.ID = ID;
        User = user;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getProductId() {
        return ProductId;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTimeToPrepare() {
        return TimeToPrepare;
    }

    public void setTimeToPrepare(String timeToPrepare) {
        TimeToPrepare = timeToPrepare;
    }
}
