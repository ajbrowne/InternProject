package com.example.specialsapp.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


public class SignUpFragment extends Fragment {

    private EditText username;
    private EditText password;
    private EditText fullName;
    private Button signUp;
    private View signUpView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        signUpView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        username = (EditText)signUpView.findViewById(R.id.email1);
        password = (EditText)signUpView.findViewById(R.id.password1);
        fullName = (EditText)signUpView.findViewById(R.id.name1);
        signUp = (Button)signUpView.findViewById(R.id.signup);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConsumerHomeFragment consumerHomeFragment = new ConsumerHomeFragment();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer2, consumerHomeFragment);
                fragmentTransaction.commit();
            }
        });

        // Inflate the layout for this fragment
        return signUpView;
    }



}
