package com.example.franc.canteenorderingapplication.Model;

public class Food {

    private String ContainsNuts;
    private String Image;
    private String LongDescription;
    private String ShortDescription;
    private String Name;
    private String Price;
    private String SuggestionId;
    private String TimeToPrepare;
    private String Vegetarian;

    public Food() {
    }

    public Food(String containsNuts, String image, String longDescription, String shortDescription, String name, String price, String timeToPrepare, String vegetarian, String suggestionId) {
        ContainsNuts = containsNuts;
        Image = image;
        LongDescription = longDescription;
        ShortDescription = shortDescription;
        Name = name;
        Price = price;
        TimeToPrepare = timeToPrepare;
        Vegetarian = vegetarian;
        SuggestionId = suggestionId;
    }

    public String getContainsNuts() {
        return ContainsNuts;
    }

    public void setContainsNuts(String containsNuts) {
        ContainsNuts = containsNuts;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLongDescription() {
        return LongDescription;
    }

    public void setLongDescription(String longDescription) {
        LongDescription = longDescription;
    }

    public String getShortDescription() {
        return ShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        ShortDescription = shortDescription;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getVegetarian() {
        return Vegetarian;
    }

    public void setVegetarian(String vegetarian) {
        Vegetarian = vegetarian;
    }

    public String getSuggestionId() {
        return SuggestionId;
    }

    public void setSuggestionId(String suggestionId) {
        SuggestionId = suggestionId;
    }
}
