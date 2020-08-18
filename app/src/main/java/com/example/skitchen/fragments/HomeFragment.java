package com.example.skitchen.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.skitchen.Meal;
import com.example.skitchen.MealsAdapter;
import com.example.skitchen.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView rvMeals;

    private FirebaseUser currentUser;

    private List<Meal> mealList;
    private MealsAdapter mealsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mealList = new ArrayList<>();

        rvMeals = getView().findViewById(R.id.rvMealsHome);

        new GetMealsTask().execute();
    }

    private class GetMealsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("meals/");
            databaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapchot : snapshot.getChildren()) {
                        Meal meal = postSnapchot.getValue(Meal.class);
                        mealList.add(meal);
                    }
                    mealList = filterMeals(mealList);
                    mealsAdapter = new MealsAdapter(getContext(), (ArrayList<Meal>) mealList, currentUser);
                    rvMeals.setAdapter(mealsAdapter);
                    rvMeals.setHasFixedSize(true);
                    rvMeals.setVerticalScrollBarEnabled(false);
                    rvMeals.setLayoutManager(new LinearLayoutManager(getContext()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            return null;
        }

        private List<Meal> filterMeals(List<Meal> mealList) {

            List<Meal> newMeals = new ArrayList<>();
            for (Meal meal : mealList) {
                if (!currentUser.getUid().equals(meal.getUser())){
                    newMeals.add(meal);
                }
            }

            return newMeals;
        }
    }
}