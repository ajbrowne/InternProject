package com.example.specialsapp.app.Fragments;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.specialsapp.app.Activities.HomeActivity;
import com.example.specialsapp.app.Cards.DealerCard;
import com.example.specialsapp.app.Cards.SpecialCard;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.Models.Special;
import com.example.specialsapp.app.R;

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
    private CardListView cardListView;
    private ArrayList<Dealer> dealers;
    private ArrayList<Special> specials;
    private double lat;
    private double longi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_dealer_specials, container, false);

        ActionBar actionBar = ((HomeActivity) getActivity()).getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ((HomeActivity) getActivity()).setTitle("Specials");

        // Get location upon opening app, returning to Dealers
        dealers = new ArrayList<Dealer>();
        specials = new ArrayList<Special>();
        GPS gps = new GPS(getActivity());
        lat = gps.getLatitude();
        longi = gps.getLongitude();
        dealers = ((HomeActivity)getActivity()).asyncCheck(lat, longi);
        specials = ((HomeActivity)getActivity()).asyncCheck(dealers.get(0).getName());

        for (int i = 0; i < specials.size(); i++) {
            SpecialCard card = new SpecialCard(this.getActivity(), R.layout.special_card);
            card.setTitle(specials.get(i).getTitle());
            card.setDescription(specials.get(i).getDescription());
            card.setDealer(specials.get(i).getDealer());
            card.setSpecialType(specials.get(i).getType());
//            card.setOnClickListener(new Card.OnCardClickListener() {
//                @Override
//                public void onClick(Card card, View view) {
//                    NearbyDealersFragment dealerSpecialsFragment = new NearbyDealersFragment();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.fragmentContainer2, dealerSpecialsFragment);
//                    fragmentTransaction.commit();
//                }
//            });
            cards.add(card);

        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        cardListView = (CardListView) homeView.findViewById(R.id.myList1);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
        }

        return homeView;
    }



}
