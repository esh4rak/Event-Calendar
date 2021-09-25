package com.example.eventcalendar.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.eventcalendar.databinding.ActivitySplashScreenBinding;
import com.example.eventcalendar.models.SignInUser;
import com.example.eventcalendar.viewmodels.SignInViewModel;


public class SplashScreenActivity extends AppCompatActivity {


    private ActivitySplashScreenBinding binding;

    private SignInViewModel signInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        initSplashViewModel();
        checkIfUserIsAuthenticated();


    }


    private void initSplashViewModel() {

        signInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.
                getInstance(this.getApplication())).get(SignInViewModel.class);
    }


    private void checkIfUserIsAuthenticated() {

        signInViewModel.checkAuthentication();
        signInViewModel.checkAuthenticateLiveData.observe(this, new Observer<SignInUser>() {
            @Override
            public void onChanged(SignInUser signInUser) {
                if (!signInUser.isAuth) {
                    goToSignInActivity();
                } else {
                    goToMainActivity();
                }
            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void getUserInformation() {
        signInViewModel.collectUserInfo();
        signInViewModel.collectUserInfoLiveData.observe(this, new Observer<SignInUser>() {
            @Override
            public void onChanged(SignInUser signInUser) {
                //goToMainActivity(signInUser);
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void goToSignInActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}