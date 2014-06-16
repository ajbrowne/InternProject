package com.example.specialsapp.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
        login = (Button)loginView.findViewById(R.id.loginButton);
        signup = (Button)loginView.findViewById(R.id.signUpButton);
        username = (EditText)loginView.findViewById(R.id.email);
        password = (EditText)loginView.findViewById(R.id.password);

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

        loadSavedPreferences();

        return loginView;
    }

    /*
        Fires the asyncCheck when login is clicked
     */
    private View.OnClickListener loginClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString().toLowerCase(Locale.US);
                String pass = password.getText().toString().toLowerCase(Locale.US);
                savePreferences("stored", true);
                savePreferences("User", user);
                savePreferences("Password", pass);
                ((MainActivity)getActivity()).asyncCheck(user, pass, false);
            }
        };
    }

    private void loadSavedPreferences(){
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sUser = shared.getString("User", "");
        String sPass = shared.getString("Password", "");
        boolean check = shared.getBoolean("stored", false);
        if(check){
            ((MainActivity)getActivity()).asyncCheck(sUser, sPass, check);
        }


    }

    private void savePreferences(String key, String value){
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getActivity());

        SharedPreferences.Editor edit = shared.edit();
        edit.putString(key, value);
        edit.commit();
    }

    private void savePreferences(String key, boolean value){
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getActivity());

        SharedPreferences.Editor edit = shared.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }


}
