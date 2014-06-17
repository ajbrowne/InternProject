package com.example.specialsapp.app;


import android.app.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import java.util.Locale;


public class SignUpFragment extends Fragment {

    private EditText username;
    private EditText password;
    private EditText confirm;
    private Button signUp;
    private View signUpView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        signUpView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        username = (EditText) signUpView.findViewById(R.id.email1);
        password = (EditText) signUpView.findViewById(R.id.password1);
        confirm = (EditText) signUpView.findViewById(R.id.confirm);
        signUp = (Button) signUpView.findViewById(R.id.signup);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString().toLowerCase(Locale.US);
                String pass = password.getText().toString();
                String conf = confirm.getText().toString().toLowerCase(Locale.US);

                if (conf.compareTo(pass) != 0) {
                    new MyAlertDialog(getActivity(),  "Passwords do not match", "Please enter your password correctly both times.").show();
                } else if (conf.length() == 0 || pass.length() == 0) {
                    new MyAlertDialog(getActivity(),  "Password of length zero", "Passwords of length zero are not allowed.").show();
                } else if (user.length() == 0) {
                    new MyAlertDialog(getActivity(),  "No email entered", "Please enter an email address.").show();
                } else {
                    String encrypted = ((MainActivity) getActivity()).computeSHAHash(pass);
                    System.out.println(encrypted);

                    ((MainActivity) getActivity()).savePreferences("stored", true);
                    ((MainActivity) getActivity()).savePreferences("User", user);
                    ((MainActivity) getActivity()).savePreferences("Password", encrypted);
                    ((MainActivity) getActivity()).asyncCheck(user, encrypted, "", true);
                }
            }
        });

        confirm.addTextChangedListener(new MyTextWatcher());
        password.addTextChangedListener(new MyTextWatcher());

        ((MainActivity) getActivity()).loadSavedPreferences();


        // Inflate the layout for this fragment
        return signUpView;
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
