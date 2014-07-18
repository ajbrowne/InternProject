package com.example.specialsapp.app.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.specialsapp.app.Activities.DealerDetail;
import com.example.specialsapp.app.Cards.DealerCard;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.Models.LocationObject;
import com.example.specialsapp.app.R;

import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Displays all dealers nearest to your current location/entered zip
 */
public class NearbyDealersFragment extends Fragment implements OnRefreshListener {

    private static final String baseUrl = "http://192.168.168.235:8080/v1/specials/dealers?";
    private View homeView;
    private Double lat;
    private Double longi;
    private PullToRefreshLayout mPullToRefreshLayout;
    private RequestQueue queue;
    private GPS gps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_nearby_dealers_, container, false);
        inflater.inflate(R.layout.dealer_card, container, false);
        getActivity().setTitle("Dealers");
        setHasOptionsMenu(true);
        gps = new GPS(getActivity());

        AbstractHttpClient client = new DefaultHttpClient();
        queue = Volley.newRequestQueue(getActivity(), new HttpClientStack(client));

        mPullToRefreshLayout = (PullToRefreshLayout) homeView.findViewById(R.id.carddemo_extra_ptr_layout1);
        ActionBarPullToRefresh.from(this.getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        LocationObject location = gps.checkLocationSettings();
        lat = location.getLatitude();
        longi = location.getLongitude();

        getDealers();

        return homeView;
    }

    /**
     * Starts the process of getting dealers by setting the lat/long parameters
     */
    public void getDealers() {
        String latt = String.valueOf(lat);
        String longg = String.valueOf(longi);

        HashMap<String, String> param = new HashMap<>();
        param.put("lng", longg);
        param.put("lat", latt);

        String url = generateUrl(param);
        JsonArrayRequest searchRequest = new JsonArrayRequest(url, new ResponseListener(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Dealer request failed!!!!!!!!!!!!!!!!!!!!!!");
            }
        });

        queue.add(searchRequest);
    }

    /**
     * Calls createDealers and then sets the adapter for the array of cards.
     * TODO: This method is pretty much the same in 3 different classes. Please make this method only once.
     * TODO: DRY (Don't Repeat Yourself) code is what we strive for - that way debugging doesn't go through
     * TODO: multiple places that do the same exact place, you get cleaner code, and you feel like a champ.
     * @param dealers - dealers that will have cards made for them
     */
    private void addCards(ArrayList<Dealer> dealers) {
        ArrayList<Card> cards = new ArrayList<>();
        cards = createDealers(dealers, cards);

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        CardListView cardListView = (CardListView) homeView.findViewById(R.id.myList);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
        }

        if (mPullToRefreshLayout != null) {
            mPullToRefreshLayout.setRefreshComplete();
        }
    }

    /**
     * Creates a card for each dealer.
     * @param dealers - dealers that will have cards made for them
     * @param cards - cards to be made
     * @return - the ArrayList of cards
     */
    public ArrayList<Card> createDealers(ArrayList<Dealer> dealers, ArrayList<Card> cards) {
        for (Dealer dealer : dealers) {
            DealerCard card = new DealerCard(getActivity(), R.layout.dealer_card, dealer.getLatitude(), dealer.getLongitude());
            Double distance = distance(dealer.getLatitude(), dealer.getLongitude(), lat, longi);
            distance = (double) Math.round(distance * 10) / 10;

            card.setDealer(dealer.getName());
            card.setCityState(dealer.getCity() + ", " + dealer.getState());
            card.setDistance(String.valueOf(distance) + " mi");
            card.setNumSpecials(String.valueOf(dealer.getNumSpecials()) + " deals currently running");
            card.setOnClickListener(getOnClickListener(dealer, distance));
            cards.add(card);
        }
        return cards;
    }

    /**
     * Card listener that holds info for the dealer detailed view.
     * @param dealer - the dealer whose card was clicked
     * @param distance - the distance from the dealer to the user
     * @return - the listener
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
                getActivity().startActivity(intent);
            }
        };
    }

    @Override
    public void onRefreshStarted(View view) {
        gps.checkLocationSettings();
        LocationObject location = gps.checkLocationSettings();
        lat = location.getLatitude();
        longi = location.getLongitude();
        getDealers();
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
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
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
    private String generateUrl(HashMap<String, String> parameters) {
        return baseUrl + "lng=" + parameters.get("lng") + "&lat=" + parameters.get("lat");
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
                    JSONObject dealerObject = (JSONObject) response.get(i);
                    Dealer dealer = new Dealer();
                    dealer.setCity(dealerObject.get("city").toString());
                    dealer.setState(dealerObject.get("state").toString());
                    dealer.setName(dealerObject.get("name").toString());
                    JSONObject loc = (JSONObject) dealerObject.get("loc");
                    JSONArray coords = (JSONArray) loc.get("coordinates");
                    dealer.setLongitude(coords.getDouble(1));
                    dealer.setLatitude(coords.getDouble(0));
                    dealer.setNumSpecials(dealerObject.getInt("numSpecials"));
                    dealers.add(dealer);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            addCards(dealers);
        }

    }
}
