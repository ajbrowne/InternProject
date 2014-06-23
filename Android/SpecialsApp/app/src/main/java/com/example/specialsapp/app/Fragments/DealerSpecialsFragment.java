package com.example.specialsapp.app.Fragments;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.specialsapp.app.Activities.HomeActivity;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.Models.Special;
import com.example.specialsapp.app.R;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

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
        actionBar.setDisplayHomeAsUpEnabled(true);

        getActivity().setTitle("Specials");

        // Get location upon opening app, returning to Dealers
        dealers = new ArrayList<Dealer>();
        final GPS gps = new GPS(getActivity());
        Double latitiude = gps.getLatitude();
        Double longitude = gps.getLongitude();

        try {
            dealers = ((HomeActivity)getActivity()).getDealers(longitude, latitiude, homeView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return homeView;
    }


}
