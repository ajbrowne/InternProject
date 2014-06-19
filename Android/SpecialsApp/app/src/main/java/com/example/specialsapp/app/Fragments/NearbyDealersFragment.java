package com.example.specialsapp.app.Fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.specialsapp.app.Activities.HomeActivity;
import com.example.specialsapp.app.R;
import com.example.specialsapp.app.Cards.SpecialCard;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_nearby_dealers_, container, false);

        getActivity().setTitle("Dealers");

        ActionBar actionBar = ((HomeActivity)getActivity()).getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        SpecialCard card = new SpecialCard(getActivity(), R.layout.dealer_card);
        card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                DealerSpecialsFragment nearbyDealersFragment = new DealerSpecialsFragment();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(NearbyDealersFragment.this);
                fragmentTransaction.addToBackStack("nearby");
                fragmentTransaction.add(R.id.fragmentContainer2, nearbyDealersFragment);
                fragmentTransaction.commit();
            }
        });

        for (int i = 0; i < 5; i++) {
            cards.add(card);
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        cardListView = (CardListView) homeView.findViewById(R.id.myList);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
        }

        return homeView;
    }
}
