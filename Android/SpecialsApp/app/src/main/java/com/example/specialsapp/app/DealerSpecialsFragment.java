package com.example.specialsapp.app;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_dealer_specials, container, false);

        ActionBar actionBar = ((HomeActivity)getActivity()).getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ((HomeActivity)getActivity()).setTitle("Specials for this dealer");

        SpecialCard card  = new SpecialCard(getActivity(), R.layout.deal_card);

        for (int i = 0; i < 5; i++){
            cards.add(card);
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        cardListView = (CardListView) homeView.findViewById(R.id.myList1);
        if (cardListView != null){
            cardListView.setAdapter(mCardArrayAdapter);
        }

        return homeView;
    }
}
