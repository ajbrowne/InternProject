package com.example.specialsapp.app.Fragments;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.specialsapp.app.Activities.MainActivity;
import com.example.specialsapp.app.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupZipFragment extends Fragment {

    private Button next;
    private EditText zip;
    private String zipCode;
    private TextView signin;

    public SignupZipFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup_zip, container, false);
        next = (Button)view.findViewById(R.id.flow1_button);
        zip = (EditText)view.findViewById(R.id.flow1_zip);
        signin = (TextView)view.findViewById(R.id.flow1_signin);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipCode = zip.getText().toString();
                ((MainActivity)getActivity()).setZip(zipCode);
                SignupNameFragment fragment = new SignupNameFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment, "signupZipFragment").addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment fragment = new LoginFragment();
                Bundle bundle = new Bundle();
                bundle.putString("clear", "clear");
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();fragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public static SignupZipFragment newInstance(String text) {
        SignupZipFragment zip = new SignupZipFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", text);

        zip.setArguments(bundle);
        return zip;
    }

}
