package com.example.specialsapp.app.Fragments;

import android.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.specialsapp.app.Activities.HomeActivity;
import com.example.specialsapp.app.Cards.DealerCard;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.R;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by brownea on 6/12/14.
 */
public class NearbyDealersFragment extends Fragment {

    private View homeView;
    private ArrayList<Card> cards = new ArrayList<Card>();
    private CardListView cardListView;
    private TextView dealerName;
    private Double lat;
    private Double longi;
    private ArrayList<Dealer> dealers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_nearby_dealers_, container, false);
        inflater.inflate(R.layout.dealer_card, container, false);
        ((HomeActivity) getActivity()).toggleDrawerOn();
        getActivity().setTitle("Dealers");

        ActionBar actionBar = ((HomeActivity)getActivity()).getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(true);

        // Get location upon opening app, returning to Dealers
        dealers = new ArrayList<Dealer>();
        GPS gps = new GPS(getActivity());
        lat = gps.getLatitude();
        longi = gps.getLongitude();
        //dealers = ((HomeActivity)getActivity()).asyncCheck(lat, longi);

        for (int i = 0; i < dealers.size(); i++){
            DealerCard card = new DealerCard(this.getActivity(), R.layout.dealer_card);
            card.setDealer(dealers.get(i).getName());
            card.setCityState(dealers.get(i).getCity() + ", " + dealers.get(i).getState());
            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    NearbyDealersFragment dealerSpecialsFragment = new NearbyDealersFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(NearbyDealersFragment.this);
                    fragmentTransaction.addToBackStack("nearby");
                    ((HomeActivity) getActivity()).toggleDrawerOff();
                    fragmentTransaction.replace(R.id.fragmentContainer2, dealerSpecialsFragment);
                    fragmentTransaction.commit();
                }
            });
            cards.add(card);
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        cardListView = (CardListView) homeView.findViewById(R.id.myList);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
        }

        return homeView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
