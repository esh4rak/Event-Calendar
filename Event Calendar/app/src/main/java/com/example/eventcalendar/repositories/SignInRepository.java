package com.example.eventcalendar.repositories;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.example.eventcalendar.models.SignInUser;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInRepository {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final SignInUser user = new SignInUser();

    // check Authentication in firebase

    public MutableLiveData<SignInUser> checkAuthenticationInFirebase() {

        MutableLiveData<SignInUser> isAuthenticateLiveData = new MutableLiveData<>();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            user.isAuth = false;

        } else {
            user.uId = currentUser.getUid();
            user.isAuth = true;

        }
        isAuthenticateLiveData.setValue(user);
        return isAuthenticateLiveData;
    }



    //collect user info  from authentication

    public MutableLiveData<SignInUser> collectUserData() {
        MutableLiveData<SignInUser> collectUserMutableLiveData = new MutableLiveData<>();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String uId = currentUser.getUid();
            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            Uri getImageUrl = currentUser.getPhotoUrl();
            String imageUrl = getImageUrl.toString();
            SignInUser user = new SignInUser(uId, name, email, imageUrl);
            collectUserMutableLiveData.setValue(user);
        }
        return collectUserMutableLiveData;
    }



    //firebase sign in with google

    public MutableLiveData<String> firebaseSignInWithGoogle(AuthCredential authCredential) {

        final MutableLiveData<String> authMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithCredential(authCredential).addOnSuccessListener(authResult -> {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            String uId = currentUser.getUid();
            authMutableLiveData.setValue(uId);


        }).addOnFailureListener(e -> authMutableLiveData.setValue(e.toString()));
        return authMutableLiveData;
    }

}
