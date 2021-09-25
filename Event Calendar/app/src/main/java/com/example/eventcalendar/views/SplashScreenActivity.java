package com.example.eventcalendar.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.eventcalendar.databinding.ActivitySplashScreenBinding;


public class SplashScreenActivity extends AppCompatActivity {


    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        init();
    }

    private void init() {

    }
}