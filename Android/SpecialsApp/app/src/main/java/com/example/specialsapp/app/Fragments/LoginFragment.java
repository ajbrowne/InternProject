package com.example.specialsapp.app.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.specialsapp.app.Activities.MainActivity;
import com.example.specialsapp.app.AlertDialogs.CustomAlertDialog;
import com.example.specialsapp.app.R;

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
                SignupNumberFragment fragment = new SignupNumberFragment();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
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
                    new CustomAlertDialog(getActivity(), "Invalid username or password", "Your username or password is incorrect, try again.").show();
                } else {
                    String encrypted = ((MainActivity) getActivity()).computeSHAHash(pass);
                    System.out.println(encrypted);


                    int check = ((MainActivity) getActivity()).asyncCheck(user, encrypted, "login", false, "", "", "", "");
                    if (check == 1) {
                        ((MainActivity) getActivity()).savePreferences("stored", true);
                        ((MainActivity) getActivity()).savePreferences("User", user);
                        ((MainActivity) getActivity()).savePreferences("Password", encrypted);
                    }
                }
            }
        };
    }

}
