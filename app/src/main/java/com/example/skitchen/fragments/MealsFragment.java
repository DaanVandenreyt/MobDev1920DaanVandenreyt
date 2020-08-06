package com.example.skitchen.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.skitchen.MealsAdapter;
import com.example.skitchen.R;
import com.example.skitchen.activities.AddRecipeActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealsFragment extends Fragment {

    Button btnAddMeal;

    RecyclerView recyclerView;
    String meals[], descs[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvMeals);
        btnAddMeal = view.findViewById(R.id.btnAddMeal);

        meals = getResources().getStringArray(R.array.meal_names);
        descs = getResources().getStringArray(R.array.meal_descriptions);

        MealsAdapter mealsAdapter = new MealsAdapter(getContext(), meals, descs);
        recyclerView.setAdapter(mealsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddRecipeActivity.class);
                startActivity(intent);
            }
        });
    }
}