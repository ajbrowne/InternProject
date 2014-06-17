package com.example.specialsapp.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * Created by brownea on 6/11/14.
 */
public class LoginFragment extends Fragment {

    private EditText username;
    private EditText password;
    private Button login;
    private Button signup;
    private View loginView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loginView = inflater.inflate(R.layout.fragment_login, container, false);

        // Link elements from xml
        login = (Button) loginView.findViewById(R.id.loginButton);
        signup = (Button) loginView.findViewById(R.id.signUpButton);
        username = (EditText) loginView.findViewById(R.id.email);
        password = (EditText) loginView.findViewById(R.id.password);

        login.setOnClickListener(loginClick());

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpFragment fragment = new SignUpFragment();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "signUpFragment").addToBackStack("login");
                fragmentTransaction.commit();
            }
        });

        ((MainActivity) getActivity()).loadSavedPreferences();

        return loginView;
    }

    /*
        Fires the asyncCheck when login is clicked
     */
    private View.OnClickListener loginClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString().toLowerCase(Locale.US);
                String pass = password.getText().toString().toLowerCase(Locale.US);

                if (user.length() == 0 || pass.length() == 0) {
                    new MyAlertDialog(getActivity(), "Invalid username or password", "Your username or password is incorrect, try again.").show();
                } else {
                    String encrypted = ((MainActivity) getActivity()).computeSHAHash(pass);
                    System.out.println(encrypted);

                    ((MainActivity) getActivity()).savePreferences("stored", true);
                    ((MainActivity) getActivity()).savePreferences("User", user);
                    ((MainActivity) getActivity()).savePreferences("Password", encrypted);
                    ((MainActivity) getActivity()).asyncCheck(user, encrypted, "", false);
                }
            }
        };
    }

}
