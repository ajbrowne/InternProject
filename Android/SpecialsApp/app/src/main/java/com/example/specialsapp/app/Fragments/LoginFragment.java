package com.example.specialsapp.app.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.specialsapp.app.Activities.MainActivity;
import com.example.specialsapp.app.AlertDialogs.CustomAlertDialog;
import com.example.specialsapp.app.R;
import com.example.specialsapp.app.SignUpFragments.SignupNumberFragment;

import java.util.Locale;

/**
 * Fragment used for when users want to log in to the app
 * <p/>
 * Created by brownea on 6/11/14.
 */
public class LoginFragment extends Fragment {

    int stackVar;
    private EditText username;
    private EditText password;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View loginView = inflater.inflate(R.layout.fragment_login, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();

        stackVar = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < stackVar; i++) {
            Log.i("MainActivity", "popping backstack");
            fragmentManager.popBackStack();
        }

        // Link elements from xml
        Button login = (Button) loginView.findViewById(R.id.loginButton);
        Button signup = (Button) loginView.findViewById(R.id.signUpButton);
        username = (EditText) loginView.findViewById(R.id.email);
        password = (EditText) loginView.findViewById(R.id.password);

        login.setOnClickListener(loginClick());

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupNumberFragment fragment = new SignupNumberFragment();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "number").addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

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

                    ((MainActivity) getActivity()).login(user, encrypted);
                }
            }
        };
    }

}
