package com.example.franc.canteenorderingapplication.Model;

public class VoucherModel {

    private String PercentOff;
    private String Title;
    private String Details;
    private String TimeExpiration;
    private String DateExpiration;
    private String User;

    public VoucherModel() {
    }

    public VoucherModel(String percentOff, String title, String details, String timeExpiration, String dateExpiration) {
        PercentOff = percentOff;
        Title = title;
        Details = details;
        TimeExpiration = timeExpiration;
        DateExpiration = dateExpiration;
    }

    public VoucherModel(String percentOff, String title, String details, String timeExpiration, String dateExpiration, String user) {
        PercentOff = percentOff;
        Title = title;
        Details = details;
        TimeExpiration = timeExpiration;
        DateExpiration = dateExpiration;
        User = user;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getPercentOff() {
        return PercentOff;
    }

    public void setPercentOff(String percentOff) {
        PercentOff = percentOff;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getTimeExpiration() {
        return TimeExpiration;
    }

    public void setTimeExpiration(String timeExpiration) {
        TimeExpiration = timeExpiration;
    }

    public String getDateExpiration() {
        return DateExpiration;
    }

    public void setDateExpiration(String dateExpiration) {
        DateExpiration = dateExpiration;
    }
}
