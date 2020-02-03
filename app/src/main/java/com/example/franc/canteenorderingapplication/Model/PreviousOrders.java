package com.example.franc.canteenorderingapplication.Model;

import java.util.List;

public class PreviousOrders {
    private String TimeAndDate;
    private String Name;
    private String Total;
    private String Date;
    private String Time;
    private List<Order> Order;
    private String Complete;

    public PreviousOrders() {
    }

    public PreviousOrders(String name, String total, String date, String time, List<Order> order, String TimeAndDate, String complete) {
        this.Name = name;
        this.Total = total;
        this.Date = date;
        this.Time = time;
        this.Order = order;
        this.TimeAndDate = TimeAndDate;
        this.Complete = complete;
    }


    public String getComplete() {
        return Complete;
    }

    public void setComplete(String complete) {
        Complete = complete;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        this.Total = total;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public List<Order> getOrder() {
        return Order;
    }

    public void setOrder(List<Order> order) {
        this.Order = order;
    }

    public String getTimeAndDate() {
        return TimeAndDate;
    }

    public void setTimeAndDate(String timeAndDate) {
        TimeAndDate = timeAndDate;
    }
}
