package com.example.specialsapp.app.Fragments;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.specialsapp.app.Activities.HomeActivity;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.R;

import org.json.JSONException;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by brownea on 6/12/14.
 */
public class DealerSpecialsFragment extends Fragment {

    private View homeView;
    private ArrayList<Card> cards = new ArrayList<Card>();
    private ArrayList<Dealer> dealers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_dealer_specials, container, false);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        getActivity().setTitle("Specials");

        Spinner spinner = (Spinner) homeView.findViewById(R.id.spinnerSearch);
        Spinner spinner2 = (Spinner) homeView.findViewById(R.id.spinnerLocation);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.search_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.location_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter2);

        // Get location upon opening app, returning to Dealers
        dealers = new ArrayList<Dealer>();
        final GPS gps = new GPS(getActivity());
        Double latitiude = gps.getLatitude();
        Double longitude = gps.getLongitude();

        try {
            ((HomeActivity)getActivity()).getDealerSpecials(longitude, latitiude, homeView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return homeView;
    }


}
