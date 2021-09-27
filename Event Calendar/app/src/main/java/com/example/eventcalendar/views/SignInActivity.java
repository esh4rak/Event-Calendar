package com.example.eventcalendar.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.eventcalendar.R;
import com.example.eventcalendar.databinding.ActivitySignInBinding;
import com.example.eventcalendar.viewmodels.SignInViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;


public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;

    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;


    //private FirebaseAuth mAuth;
    private SignInViewModel signInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        intiSignInViewModel();
        signInMethod();


        binding.signInButton.setOnClickListener(view1 -> {
            signIn();
        });
    }

    private void intiSignInViewModel() {
        signInViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.
                getInstance(this.getApplication())).get(SignInViewModel.class);
    }


    private void signInMethod() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if (account != null) {
                    getGoogleAuthCredential(account);
                }
            } catch (ApiException e) {

                Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void getGoogleAuthCredential(GoogleSignInAccount account) {

        String googleTokenId = account.getIdToken();
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleTokenId, null);
        signInWithGoogle(authCredential);
    }

    private void signInWithGoogle(AuthCredential authCredential) {
        signInViewModel.signInWithGoogle(authCredential);
        signInViewModel.authenticateUserLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(SignInActivity.this, "" + s, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}