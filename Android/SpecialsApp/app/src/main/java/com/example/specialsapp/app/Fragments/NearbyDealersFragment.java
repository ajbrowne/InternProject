package com.example.specialsapp.app.Fragments;

import android.content.Intent;
import android.os.Bundle;
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
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by brownea on 6/12/14.
 */
public class NearbyDealersFragment extends Fragment implements OnRefreshListener {

    private static final String baseUrl = "http://192.168.170.93:8080/v1/specials/dealers?";
    private View homeView;
    private Double lat;
    private Double longi;
    private PullToRefreshLayout mPullToRefreshLayout;
    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_nearby_dealers_, container, false);
        inflater.inflate(R.layout.dealer_card, container, false);
        getActivity().setTitle("Dealers");
        setHasOptionsMenu(true);

        AbstractHttpClient client = new DefaultHttpClient();
        queue = Volley.newRequestQueue(getActivity(), new HttpClientStack(client));

        mPullToRefreshLayout = (PullToRefreshLayout) homeView.findViewById(R.id.carddemo_extra_ptr_layout1);
        ActionBarPullToRefresh.from(this.getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        // Get location upon opening app, returning to Dealers
        GPS gps = new GPS(getActivity());
        lat = gps.getLatitude();
        longi = gps.getLongitude();

        getDealers();

        return homeView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    public void getDealers() {
        String latt = String.valueOf(lat);
        String longg = String.valueOf(longi);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("lng", longg);
        param.put("lat", latt);
        param.put("extra", "0");

        String url = generateUrl(param);
        JsonArrayRequest searchRequest = new JsonArrayRequest(url, new ResponseListener(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Dealer request failed");
            }
        });

        queue.add(searchRequest);
    }

    private void addCards(ArrayList<Dealer> dealers) {
        ArrayList<Card> cards = new ArrayList<Card>();
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

    public ArrayList<Card> createDealers(ArrayList<Dealer> dealers, ArrayList<Card> cards) {
        for (Dealer dealer : dealers) {
            DealerCard card = new DealerCard(getActivity(), R.layout.dealer_card, dealer.getLatitude(), dealer.getLongitude());
            card.setDealer(dealer.getName());
            card.setCityState(dealer.getCity() + ", " + dealer.getState());
            Double distance = distance(dealer.getLatitude(), dealer.getLongitude(), lat, longi);
            distance = (double) Math.round(distance * 10) / 10;
            card.setDistance(String.valueOf(distance) + " mi");
            card.setNumSpecials(String.valueOf(dealer.getNumSpecials()) + " deals currently running");
            card.setOnClickListener(getOnClickListener(dealer, distance));
            cards.add(card);

        }
        return cards;
    }

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
        final GPS gps = new GPS(getActivity());
        lat = gps.getLatitude();
        longi = gps.getLongitude();
        // Call to retrieve dealers to display
        getDealers();
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private String generateUrl(HashMap<String, String> parameters) {
        return baseUrl + "lng=" + parameters.get("lng") + "&lat=" + parameters.get("lat") + "&extra=" + parameters.get("extra");
    }

    private class ResponseListener implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray response) {
            ArrayList<Dealer> dealers = new ArrayList<Dealer>();
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject outer = (JSONObject) response.get(i);
                    JSONObject dealerObject = (JSONObject) outer.get("content");
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
