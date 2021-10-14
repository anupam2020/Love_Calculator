package com.sbdev.lovecalculator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.lovecalculator.R;

public class AboutUs extends Fragment {

    private FrameLayout frameLayout;

    private TextView email;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        frameLayout=view.findViewById(R.id.aboutUsFrameLayout);

        email=view.findViewById(R.id.aboutUsText5);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email.clearFocus();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater.getContext().setTheme(R.style.AppTheme);

        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }
}