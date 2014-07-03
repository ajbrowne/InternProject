package com.example.specialsapp.app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.specialsapp.app.Activities.SearchActivity;
import com.example.specialsapp.app.Cards.HomeVehicleCard;
import com.example.specialsapp.app.R;
import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class HomeFragment extends Fragment {

    private static final String Trending = "See What's Trending";
    private static final String TrendingDescription = "Most Popular Deals";
    private static final String TopDiscounts = "Top Discounts";
    private static final String TopDescription = "Best Deals Available";
    private static final String NewArrivals = "New Arrivals";
    private static final String NewDecscription = "Fresh On The Lot";
    private ArrayList<Card> newVehicles;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Home");

        newVehicles = new ArrayList<Card>();
        HomeVehicleCard card = new HomeVehicleCard(getActivity());

        for (int i = 0; i < 3; i++){
            newVehicles.add(card);
        }

        createCards(homeView.findViewById(R.id.firstWidget), Trending, TrendingDescription);
        createCards(homeView.findViewById(R.id.secondWidget), TopDiscounts, TopDescription);
        createCards(homeView.findViewById(R.id.thirdWidget), NewArrivals, NewDecscription);

        return homeView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.search){
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra("tab", 0);
            startActivity(intent);

        }
        return true;
    }

    private void createCards(View view, String title, String description){
        TextView theTitle = (TextView)view.findViewById(R.id.newVehicles);
        TextView theDescription = (TextView)view.findViewById(R.id.descrip);
        theTitle.setText(title);
        theDescription.setText(description);

        CardGridArrayAdapter  cardGridArrayAdapter = new CardGridArrayAdapter(getActivity(), newVehicles);
        CardGridView gridView = (CardGridView) view.findViewById(R.id.newGrid);
        if (gridView != null){
            gridView.setAdapter(cardGridArrayAdapter);
        }
    }

}
