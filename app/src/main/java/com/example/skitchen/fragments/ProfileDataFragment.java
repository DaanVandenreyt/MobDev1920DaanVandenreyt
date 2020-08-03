package com.example.skitchen.fragments;

import android.gesture.GestureOverlayView;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.skitchen.R;
import com.example.skitchen.activities.ProfileActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDataFragment extends Fragment {

    private GoogleSignInAccount gAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gAccount = GoogleSignIn.getLastSignedInAccount(Objects.requireNonNull(getContext()));

        TextView tvNameData = getView().findViewById(R.id.tvNameData);
        tvNameData.setText(gAccount.getDisplayName());

        TextView tvMailData = getView().findViewById(R.id.tvMailData);
        tvMailData.setText(gAccount.getEmail());
    }
}