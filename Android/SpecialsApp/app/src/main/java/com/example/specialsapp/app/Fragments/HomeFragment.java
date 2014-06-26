package com.example.specialsapp.app.Fragments;



import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.specialsapp.app.Cards.HomeVehicleCard;
import com.example.specialsapp.app.R;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        ArrayList<Card> newVehicles = new ArrayList<Card>();
        HomeVehicleCard card = new HomeVehicleCard(getActivity());

        for (int i = 0; i < 3; i++){
            newVehicles.add(card);
        }

        CardGridArrayAdapter  cardGridArrayAdapter = new CardGridArrayAdapter(getActivity(), newVehicles);
        CardGridView gridView = (CardGridView)homeView.findViewById(R.id.newGrid);
        if (gridView != null){
            gridView.setAdapter(cardGridArrayAdapter);
        }


        return homeView;
    }


}
