package com.example.skitchen;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skitchen.activities.MealDetailActivity;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;
import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MyViewHolder> {

    List<Meal> meals;
    Context context;
    FirebaseUser currentUser;

    public MealsAdapter(Context context, ArrayList<Meal> meals, FirebaseUser currentUser){
        this.context = context;
        this.meals = meals;
        this.currentUser = currentUser;

        /*int pos = 0;
        while ( pos < this.meals.size() ) {
            if (meals.get(pos).getUser() == currentUser.getUid()) {
                this.meals.remove(pos);
            } else {
                pos += 1;
            }
        }*/
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.meal_element, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Meal meal = meals.get(position);
        holder.name.setText(meal.getName());
        //holder.desc.setText(meal.getDescription());

        //Picasso.get().load(meal.getImageUrl()).placeholder(R.drawable.ic_image_placeholder).into(holder.image);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MealDetailActivity.class);
                intent.putExtra("name", meal.getName());
                intent.putExtra("description", meal.getDescription());
                intent.putExtra("gluten", meal.getGlutenFree());
                intent.putExtra("veggie", meal.getVegetarian());
                intent.putExtra("category", meal.getCategory().toString());
                intent.putExtra("user", meal.getUser());
                intent.putExtra("image", meal.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ConstraintLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvMealName);
            layout = itemView.findViewById(R.id.mealElement);
        }
    }
}
