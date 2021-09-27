package com.example.eventcalendar.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.eventcalendar.databinding.ActivitySplashScreenBinding;
import com.example.eventcalendar.models.SignInUser;
import com.example.eventcalendar.viewmodels.SignInViewModel;
import com.github.ybq.android.spinkit.style.ChasingDots;


public class SplashScreenActivity extends AppCompatActivity {


    private ActivitySplashScreenBinding binding;

    private SignInViewModel signInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        ChasingDots chasingDots = new ChasingDots();
        binding.spinKitView.setIndeterminateDrawable(chasingDots);


        int SPLASH_DISPLAY_LENGTH = 3000;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                initSplashViewModel();
                checkIfUserIsAuthenticated();
            }
        }, SPLASH_DISPLAY_LENGTH);






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


    private void getUserInformation() {
        signInViewModel.collectUserInfo();
        signInViewModel.collectUserInfoLiveData.observe(this, new Observer<SignInUser>() {
            @Override
            public void onChanged(SignInUser signInUser) {
                //goToMainActivity(signInUser);
                Intent intent = new Intent(SplashScreenActivity.this, EventActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, EventActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToSignInActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}