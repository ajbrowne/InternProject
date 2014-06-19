package com.example.specialsapp.app.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private TextView signin;
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
        signin = (TextView) view.findViewById(R.id.flow4_signin);

        confirm.addTextChangedListener(new MyTextWatcher());
        password.addTextChangedListener(new MyTextWatcher());

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPassword = password.getText().toString();
                confirmPassword = confirm.getText().toString();
                email = ((MainActivity) getActivity()).getEmail();
                first = ((MainActivity) getActivity()).getFirstName();
                last = ((MainActivity) getActivity()).getLastName();
                zip = ((MainActivity) getActivity()).getZip();
                phone = ((MainActivity) getActivity()).getPhoneNumber();

                if (userPassword.compareTo(confirmPassword) != 0) {
                    new CustomAlertDialog(getActivity(), "Passwords do not match", "Please enter your password correctly both times.").show();
                } else if (userPassword.length() == 0 || confirmPassword.length() == 0) {
                    new CustomAlertDialog(getActivity(), "Password of length zero", "Passwords of length zero are not allowed.").show();
                } else {
                    String encrypted = ((MainActivity) getActivity()).computeSHAHash(userPassword);

                    ((MainActivity) getActivity()).setPassword(encrypted);
                    if (((MainActivity) getActivity()).asyncCheck(email, encrypted, "", true, first, last, zip, phone) == 1) {
                        ((MainActivity) getActivity()).savePreferences("stored", true);
                        ((MainActivity) getActivity()).savePreferences("User", email);
                        ((MainActivity) getActivity()).savePreferences("Password", encrypted);
                    }
                }
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
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pass = password.getText().toString();
            String conf = confirm.getText().toString();

            if (pass.compareTo(conf) == 0 && pass.length() != 0 && conf.length() != 0) {
                System.out.println("SAME");
                password.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_confirm));
                confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_confirm));
            } else {
                System.out.println("DIFF");
                password.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_input));
                confirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_input));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
