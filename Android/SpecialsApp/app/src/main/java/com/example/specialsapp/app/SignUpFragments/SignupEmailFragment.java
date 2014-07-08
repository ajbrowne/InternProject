package com.example.specialsapp.app.SignUpFragments;

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
import android.widget.Toast;

import com.example.specialsapp.app.Activities.MainActivity;
import com.example.specialsapp.app.Fragments.LoginFragment;
import com.example.specialsapp.app.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SignupEmailFragment extends Fragment {

    private EditText email;
    private String emailAddress;

    public SignupEmailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup_email, container, false);
        Button next = (Button)view.findViewById(R.id.flow3_button);
        TextView signIn = (TextView)view.findViewById(R.id.flow3_signin);
        email = (EditText)view.findViewById(R.id.flow3_email);
        if(getActivity().getIntent().getBooleanExtra("submit", false)){
            next.setText("Submit");
            signIn.setVisibility(View.GONE);
        }
//        Gets email from logged in Google account to populate field (use this or ???)
//        Account[] accounts = AccountManager.get(getActivity()).getAccountsByType("com.google");
//        String e = accounts[0].name;
//        email.setText(e);


            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity().getIntent().getBooleanExtra("submit", false)){
                        Toast.makeText(getActivity().getApplicationContext(), "Info Submitted", Toast.LENGTH_LONG).show();
                    }else {
                        emailAddress = email.getText().toString();
                        ((MainActivity) getActivity()).getUser().setEmail(emailAddress);
                        SignupPasswordFragment fragment = new SignupPasswordFragment();

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                        fragmentTransaction.replace(R.id.fragmentContainer, fragment, "signupEmailFragment").addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment fragment = new LoginFragment();
                Bundle bundle = new Bundle();
                bundle.putString("clear", "clear");
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragment.setArguments(bundle);
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
