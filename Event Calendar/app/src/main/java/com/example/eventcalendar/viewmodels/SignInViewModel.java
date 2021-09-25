package com.example.eventcalendar.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.eventcalendar.models.SignInUser;
import com.example.eventcalendar.repositories.SignInRepository;
import com.google.firebase.auth.AuthCredential;

public class SignInViewModel extends AndroidViewModel {

    private SignInRepository repository;
    public LiveData<SignInUser> checkAuthenticateLiveData;
    public LiveData<SignInUser> collectUserInfoLiveData;
    public LiveData<String> authenticateUserLiveData;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        repository = new SignInRepository();
    }


    // check Authentication in firebase
    public void checkAuthentication() {
        checkAuthenticateLiveData = repository.checkAuthenticationInFirebase();
    }

    //collect user info  from authentication
    public void collectUserInfo() {
        collectUserInfoLiveData = repository.collectUserData();
    }

    //firebase sign in with google
    public void signInWithGoogle(AuthCredential authCredential) {
        authenticateUserLiveData = repository.firebaseSignInWithGoogle(authCredential);
    }


}
