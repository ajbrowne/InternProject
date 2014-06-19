package com.example.specialsapp.app.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.specialsapp.app.Activities.HomeActivity;
import com.example.specialsapp.app.Activities.MainActivity;
import com.example.specialsapp.app.AlertDialogs.CustomAlertDialog;
import com.example.specialsapp.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupPasswordFragment extends Fragment {

    private Button next;
    private EditText password;
    private EditText confirm;
    private String userPassword;
    private String confirmPassword;
    private String email;
    private String first;
    private String last;
    private String zip;
    private String phone;

    public SignupPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup_password, container, false);
        next = (Button) view.findViewById(R.id.flow4_button);
        password = (EditText) view.findViewById(R.id.flow4_password);
        confirm = (EditText) view.findViewById(R.id.flow4_verify);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPassword = password.getText().toString();
                confirmPassword = confirm.getText().toString();

                String encrypted = ((MainActivity) getActivity()).computeSHAHash(userPassword);

                email = (String) ((MainActivity) getActivity()).getEmail();
                first = (String) ((MainActivity) getActivity()).getFirstName();
                last = (String) ((MainActivity) getActivity()).getLastName();
                zip = (String) ((MainActivity) getActivity()).getZip();
                phone = (String) ((MainActivity) getActivity()).getPhoneNumber();

                ((MainActivity) getActivity()).setPassword(encrypted);
                if (((MainActivity) getActivity()).asyncCheck(email, encrypted, "", true, first, last, zip, phone) == 1) {
                    new CustomAlertDialog(getActivity(), "Sign Up Success", "Sign up completed successfully. Now go get some deals!").show();
                    Intent intent = new Intent((MainActivity) getActivity(), HomeActivity.class);
                    ((MainActivity) getActivity()).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    startActivity(intent);
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
