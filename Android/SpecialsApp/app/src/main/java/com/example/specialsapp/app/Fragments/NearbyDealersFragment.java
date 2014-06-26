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
import com.example.specialsapp.app.Cards.HomeVehicleCard;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.Models.Special;
import com.example.specialsapp.app.R;
import com.example.specialsapp.app.Rest.SpecialsRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_nearby_dealers_, container, false);
        inflater.inflate(R.layout.dealer_card, container, false);
        getActivity().setTitle("Dealers");

        ActionBar actionBar = ((HomeActivity) getActivity()).getActionBar();
        actionBar.setHomeButtonEnabled(true);

        // Get location upon opening app, returning to Dealers
        GPS gps = new GPS(getActivity());
        lat = gps.getLatitude();
        longi = gps.getLongitude();

        getDealers();

        return homeView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void getDealers() {
        String latt = String.valueOf(lat);
        String longg = String.valueOf(longi);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("lng", longg);
        param.put("lat", latt);
        RequestParams params = new RequestParams(param);

        SpecialsRestClient.get("dealers", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray request) {
                ArrayList<Dealer> dealers = new ArrayList<Dealer>();
                try {
                    for (int i = 0; i < request.length(); i++) {
                        JSONObject outer = (JSONObject) request.get(i);
                        JSONObject dealerObject = (JSONObject) outer.get("content");
                        Dealer dealer = new Dealer();
                        dealer.setCity((String)dealerObject.get("city"));
                        dealer.setState((String)dealerObject.get("state"));
                        dealer.setName((String) dealerObject.get("name"));
                        dealers.add(dealer);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayList<Card> cards = new ArrayList<Card>();
                cards = createDealers(dealers, cards);

                CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

                cardListView = (CardListView) homeView.findViewById(R.id.myList);
                if (cardListView != null) {
                    cardListView.setAdapter(mCardArrayAdapter);
                }
            }

        });

    }

    public ArrayList<Card> createDealers(ArrayList<Dealer> dealers, ArrayList<Card> cards){
        for (int i = 0; i <dealers.size(); i++){
            DealerCard card = new DealerCard(getActivity(), R.layout.dealer_card);
            card.setDealer(dealers.get(i).getName());
            card.setCityState(dealers.get(i).getCity() + ", " + dealers.get(i).getState());
            cards.add(card);
        }
        return cards;
    }
}
