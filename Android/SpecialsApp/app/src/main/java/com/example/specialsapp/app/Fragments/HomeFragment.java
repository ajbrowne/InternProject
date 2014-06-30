package com.example.specialsapp.app.Fragments;



import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.specialsapp.app.Activities.HomeActivity;
import com.example.specialsapp.app.Activities.SearchActivity;
import com.example.specialsapp.app.Cards.HomeVehicleCard;
import com.example.specialsapp.app.R;

import org.w3c.dom.Text;

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
        setHasOptionsMenu(true);
        getActivity().setTitle("Home");

        ArrayList<Card> newVehicles = new ArrayList<Card>();
        HomeVehicleCard card = new HomeVehicleCard(getActivity());

        for (int i = 0; i < 3; i++){
            newVehicles.add(card);
        }

        View view = (View)homeView.findViewById(R.id.firstWidget);

        View view2 = (View)homeView.findViewById(R.id.secondWidget);
        TextView title2 = (TextView)view2.findViewById(R.id.newVehicles);
        title2.setText("Top Discounts");

        View view3 = (View)homeView.findViewById(R.id.thirdWidget);
        TextView title3 = (TextView)view3.findViewById(R.id.newVehicles);
        title3.setText("New Arrivals");

        CardGridArrayAdapter  cardGridArrayAdapter = new CardGridArrayAdapter(getActivity(), newVehicles);
        CardGridView gridView = (CardGridView) view.findViewById(R.id.newGrid);
        if (gridView != null){
            gridView.setAdapter(cardGridArrayAdapter);
        }

        CardGridArrayAdapter  cardGridArrayAdapter2 = new CardGridArrayAdapter(getActivity(), newVehicles);
        CardGridView gridView2 = (CardGridView) view2.findViewById(R.id.newGrid);
        if (gridView2 != null){
            gridView2.setAdapter(cardGridArrayAdapter2);
        }

        CardGridArrayAdapter  cardGridArrayAdapter3 = new CardGridArrayAdapter(getActivity(), newVehicles);
        CardGridView gridView3 = (CardGridView) view3.findViewById(R.id.newGrid);
        if (gridView3 != null){
            gridView3.setAdapter(cardGridArrayAdapter3);
        }

        return homeView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.search){
            SearchActivity searchActivity = new SearchActivity();
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);

        }
        return false;
    }


}
