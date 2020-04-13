package com.example.obidi.eco_shopping_mall;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.obidi.eco_shopping_mall.Utils.UserSession;
import com.example.obidi.eco_shopping_mall.fragments.LoginFragment;
import com.example.obidi.eco_shopping_mall.fragments.SignupFragment;
import com.example.obidi.eco_shopping_mall.viewModels.LoginFragmentViewModel;

public class AuthenticationActivity extends AppCompatActivity {
    private static final String TAG = "AuthenticationActivity";

    public static Intent createIntent(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        LoginFragmentViewModel viewModel = ViewModelProviders.of(this).get(LoginFragmentViewModel.class);
        viewModel.getLoginSuccessful().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean loginSuccess) {
                if (loginSuccess) {
                    Log.d(TAG, "User is signed in, redirecting to main activity");
                    startActivityForResult(MainActivity.createIntent(AuthenticationActivity.this), 0);
                } else {
                    Log.d(TAG, "User reauthentication Failed");
                    hostLoginFragment();
                }
            }
        });

        UserSession session = UserSession.getInstance(this);
        String username = session.getUsername();
        String password = session.getPassword();
        boolean alreadyLoggedIn = session.getLoginStatus();
        if (!alreadyLoggedIn || username == null || password == null) {
            Toast.makeText(this, "You must login first!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "User has not already signed in, hosting login fragment");
            hostLoginFragment();
        } else {
            // retrieve the username and password from UserSession and reAuthenticate the user
            Log.d(TAG, "User already signed in, reauthenticating user");
            viewModel.signin(this, session.getUsername(), session.getPassword());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

    private void hostLoginFragment() {
        Fragment loginFragment = LoginFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.authentication_activity_fragment_container, loginFragment)
                .commit();
    }

    private void hostSignupFragment() {
        Fragment signupFragment = SignupFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.authentication_activity_fragment_container, signupFragment)
                .commit();
    }
}
