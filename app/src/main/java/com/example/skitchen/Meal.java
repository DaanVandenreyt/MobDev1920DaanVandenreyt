package com.example.skitchen;

import android.net.Uri;

public class Meal {

    private String user;
    private String name;
    private String description;
    private MealCategory category;
    private Boolean glutenFree;
    private Boolean vegetarian;
    private String image;


    public Meal() {
        //empty constructor needed
    }

    public Meal(String mealName, String mealDescription, String ownerId) {
        if (mealName.trim().equals("")) setName("Standard Meal");
        if (mealDescription.trim().equals("")) setDescription("No description given");

        name = mealName;
        description = mealDescription;
        user = ownerId;
    }

    //getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUser() {
        return user;
    }

    public MealCategory getCategory() { return category; }

    public Boolean getGlutenFree() { return glutenFree; }

    public Boolean getVegetarian() { return vegetarian; }

    public String getImage() {
        return image;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGlutenFree(Boolean glutenFree) { this.glutenFree = glutenFree; }

    public void setVegetarian(Boolean vegetarian) { this.vegetarian = vegetarian; }

    public void setCategory(MealCategory category) { this.category = category; }

    public void setImage(String image) {
        this.image = image;
    }
}
