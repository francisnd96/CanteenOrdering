package com.example.franc.canteenorderingapplication.Model;

public class StarRating {
    String userId;
    String id;
    String rating;
    String comment;

    public StarRating(String userId, String id, String rating, String comment) {
        this.userId = userId;
        this.id = id;
        this.rating = rating;
        this.comment = comment;
    }

    public StarRating() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
