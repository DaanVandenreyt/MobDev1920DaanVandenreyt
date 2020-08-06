package com.example.skitchen;

import android.content.Context;
import android.widget.Toast;

import com.example.skitchen.activities.AddRecipeActivity;

public class MealUpload {

    String name;
    String description;
    String imageUrl;

    public MealUpload() {
        //empty constructor needed
    }

    public MealUpload(String mealName, String mealDescription, String mealImageUrl) {
        if (mealName.trim().equals("")) mealName = "Standard meal";
        if (mealDescription.trim().equals("")) mealDescription = "No description given.";

        name = mealName;
        description = mealDescription;
        imageUrl = mealImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
