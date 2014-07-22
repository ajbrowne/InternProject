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
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.specialsapp.app.Activities.DealerDetail;
import com.example.specialsapp.app.Cards.DealerCard;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public abstract class BaseDealerFragment extends Fragment {

    private TextView mResultsNone;
    private double lat;
    private double longi;
    private View view;

    public BaseDealerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_base_dealer, container, false);
    }

    /**
     * Puts the lat/long into a HashMap of parameters
     * @return - the HashMap of parameters
     */
    public HashMap<String, String> createParams() {
        HashMap<String, String> param = new HashMap<>();
        param.put("lng", String.valueOf(longi));
        param.put("lat", String.valueOf(lat));
        if (!getActivity().getIntent().getStringExtra("make").equals("All")){
            param.put("make", getActivity().getIntent().getStringExtra("make"));
        }
        return param;
    }

    /**
     * Creates the ArrayList for the cards and calls createDealerCards to make the cards.
     * Then sets the adapter for the cards, making them visible.
     * @param dealers - Any dealers that will have cards made for them.
     */
    public abstract void addCards(ArrayList<Dealer> dealers);

    /**
     * Calculates distance from dealer and then adds all info to the dealer cards.
     * @param dealers -  The dealers for which cards are being created
     * @param cards - The cards that will be created
     */
    public void createDealerCards(ArrayList<Dealer> dealers, ArrayList<Card> cards) {
        for (Dealer dealer : dealers) {
            // Calculate distance to dealer
            Double distance = distance(dealer.getLatitude(), dealer.getLongitude(), lat, longi);
            distance = (double) Math.round(distance * 10) / 10;

            DealerCard card = new DealerCard(getActivity(), R.layout.dealer_card, dealer.getLatitude(), dealer.getLongitude());
            card.setDealer(dealer.getName());
            card.setCityState(dealer.getCity() + ", " + dealer.getState());
            card.setDistance(String.valueOf(distance) + " mi");
            card.setNumSpecials(String.valueOf(dealer.getNumSpecials()) + " deals currently running");
            card.setOnClickListener(getOnClickListener(dealer, distance));
            cards.add(card);
        }
    }

    /**
     * Sets the listener for each dealer card that holds info to be sent to the detailed view.
     * @param dealer - The dealer for which the listener is made
     * @param distance - The distance from the user/entered zip
     * @return - The listener for the card
     */
    private Card.OnCardClickListener getOnClickListener(final Dealer dealer, final Double distance) {
        return new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Intent intent = new Intent(getActivity(), DealerDetail.class);
                intent.putExtra("name", dealer.getName());
                intent.putExtra("lat", dealer.getLatitude());
                intent.putExtra("long", dealer.getLongitude());
                intent.putExtra("dist", distance.toString());
                startActivity(intent);
            }
        };
    }

    /**
     * Calculates the distance between the lats/longs,
     * here from the dealer to the user/entered zip.
     * @param lat1 - dealer latitude
     * @param lon1 - dealer longitude
     * @param lat2 - user latitude
     * @param lon2 - user longitude
     * @return - the distance
     */
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    /**
     * Helper method for finding distance, converting degrees to radians.
     * @param deg - number in degrees
     * @return - number in radians
     */
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /**
     * Helper method for finding distance, converting radians to degrees.
     * @param rad - number in radians
     * @return - number in degrees
     */
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    /**
     * Builds the url for the GET request.
     * @param parameters - map of parameters
     * @return - the generated url
     */
    public abstract String generateUrl(HashMap<String, String> parameters);

    /**
     * Create the HTTP GET request and add it to the queue
     * @param queue - queue of GET requests
     * @param url - url for GET request
     */
    public void makeRequest(RequestQueue queue, String url, double latitude, double longitude, View view, boolean isSearch) {
        this.view = view;
        this.lat = latitude;
        this.longi = longitude;
        if (isSearch){
            mResultsNone =  (TextView) view.findViewById(R.id.second_result);
        }

        JsonArrayRequest searchRequest = new JsonArrayRequest(url, new BaseDealerFragment.ResponseListener(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Error", error.toString());
            }
        });
        queue.add(searchRequest);
    }

    /**
     * Callback for the GET request. Google Volley uses a ResponseListener.
     * This parses the array of dealers and then sends them off to be made
     * into cards.
     */
    private class ResponseListener implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray response) {
            ArrayList<Dealer> dealers = new ArrayList<>();
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject dealerObject = (JSONObject) response.get(i); // a dealer
                    JSONObject loc = (JSONObject) dealerObject.get("loc");
                    JSONArray coordinates = (JSONArray) loc.get("coordinates");

                    Dealer dealer = new Dealer(dealerObject.getString("name"),
                            dealerObject.get("city").toString(), dealerObject.get("state").toString(),
                            dealerObject.getInt("numSpecials"), coordinates.getDouble(0), coordinates.getDouble(1));
                    dealers.add(dealer);
                }

            } catch (JSONException e) {
                Log.e("DealerResultsActivity", "Invalid JSON in response");
            }
            if (dealers.isEmpty()) {
                mResultsNone.setVisibility(View.VISIBLE);
            }
            addCards(dealers);
        }

    }

}
