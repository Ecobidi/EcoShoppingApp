package com.example.obidi.eco_shopping_mall.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.obidi.eco_shopping_mall.MainActivity;
import com.example.obidi.eco_shopping_mall.R;
import com.example.obidi.eco_shopping_mall.viewModels.LoginFragmentViewModel;


public class LoginFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "Login Fragment";

    private static final String LOGIN_URI_PATH = "http://10.0.3.2:3000/signin";
    private static final String PROTOCOL = "http";
    private static final String DOMAIN = "10.0.3.2";
    private static final int PORT = 3000;
    private static final String PATH = "signin";

    private LoginFragmentViewModel mViewModel;
    private View mView;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(LoginFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);
        // wire up widgets
        Button loginButton = mView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        mViewModel.getLoginSuccessful().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean loginSuccess) {
                if (loginSuccess) {
                    Log.d(TAG, "User is signed in, redirecting to main activity");
                    startActivity(MainActivity.createIntent(getActivity()));
                } else {
                    Log.d(TAG, "User signin was not successful");
                }
            }
        });
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_button: {
                EditText usernameField = mView.findViewById(R.id.username);
                EditText passwordField = mView.findViewById(R.id.password);
                final String username = usernameField.getEditableText().toString();
                final String password = passwordField.getEditableText().toString();
                mViewModel.signin(getContext(), username, password);
                return;
            }
            case R.id.signup_button: {
                hostSignupFragment();
                return;
            }
        }
    }

    private void hostSignupFragment() {
        SignupFragment fragment = SignupFragment.newInstance();
        getFragmentManager().beginTransaction()
                .replace(R.id.authentication_activity_fragment_container, fragment)
                .commit();
    }
}
