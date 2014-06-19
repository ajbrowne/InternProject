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

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupZipFragment extends Fragment {

    private Button next;
    private EditText zip;
    private String zipCode;

    public SignupZipFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup_zip, container, false);
        next = (Button)view.findViewById(R.id.flow1_button);
        zip = (EditText)view.findViewById(R.id.flow1_zip);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zipCode = zip.getText().toString();
                ((MainActivity)getActivity()).setZip(zipCode);
                SignupNameFragment fragment = new SignupNameFragment();

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

    public static SignupZipFragment newInstance(String text) {
        SignupZipFragment zip = new SignupZipFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", text);

        zip.setArguments(bundle);
        return zip;
    }

}
