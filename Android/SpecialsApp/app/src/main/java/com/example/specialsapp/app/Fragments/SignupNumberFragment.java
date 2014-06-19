package com.example.specialsapp.app.Fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.specialsapp.app.Activities.MainActivity;
import com.example.specialsapp.app.R;

import java.util.EmptyStackException;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SignupNumberFragment extends Fragment {

    private Button next;
    private EditText number;
    private String phoneNumber;

    public SignupNumberFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup_number, container, false);
        next = (Button)view.findViewById(R.id.flow_button);
        number = (EditText)view.findViewById(R.id.flow_number);
        phoneNumber = number.getText().toString();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setPhoneNumber(phoneNumber);
                SignupZipFragment fragment = new SignupZipFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
