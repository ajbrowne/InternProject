package com.example.specialsapp.app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.specialsapp.app.Activities.DealerDetail;
import com.example.specialsapp.app.Cards.DealerCard;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.HomeFragments.BaseHomeFragment;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.Models.LocationObject;
import com.example.specialsapp.app.R;

import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class DealerResultsFragment extends BaseDealerFragment {

    private static final String baseUrl = "http://192.168.169.252:8080/v1/specials/dealers?";
    private TextView mResultsNone;
    private double lat;
    private double longi;
    private View resultsView;

    public DealerResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        resultsView = inflater.inflate(R.layout.fragment_dealer_results, container, false);
        mResultsNone = (TextView) resultsView.findViewById(R.id.second_result);
        AbstractHttpClient client = new DefaultHttpClient();
        RequestQueue queue = Volley.newRequestQueue(getActivity(), new HttpClientStack(client));

        // Get location by zip or current location
        GPS gps = new GPS(getActivity());
        LocationObject location = gps.checkLocationSettings();
        lat = location.getLatitude();
        longi = location.getLongitude();

        HashMap<String, String> param = createParams();

        // Create the url for and execute the GET request
        String url = generateUrl(param);
        makeRequest(queue, url, lat, longi, resultsView, true);
        return resultsView;
    }

    /**
     * Builds the url for the GET request.
     * @param parameters - map of parameters
     * @return - the generated url
     */
    public String generateUrl(HashMap<String, String> parameters) {
        return baseUrl + "lng=" + parameters.get("lng") + "&lat=" + parameters.get("lat") +
                "&make=" + parameters.get("make");
    }

    /**
     * Creates the ArrayList for the cards and calls createDealerCards to make the cards.
     * Then sets the adapter for the cards, making them visible.
     * @param dealers - Any dealers that will have cards made for them.
     */
    public void addCards(ArrayList<Dealer> dealers) {
        ArrayList cards = new ArrayList();
        createDealerCards(dealers, cards);

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        CardListView cardListView = (CardListView) resultsView.findViewById(R.id.dealer_search_results);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
        }

    }
}
