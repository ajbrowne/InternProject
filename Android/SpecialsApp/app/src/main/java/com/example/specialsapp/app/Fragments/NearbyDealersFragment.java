package com.example.specialsapp.app.Fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.specialsapp.app.Activities.DealerDetail;
import com.example.specialsapp.app.Activities.HomeActivity;
import com.example.specialsapp.app.Activities.SearchActivity;
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
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by brownea on 6/12/14.
 */
public class NearbyDealersFragment extends Fragment implements OnRefreshListener{

    private View homeView;
    private CardListView cardListView;
    private Double lat;
    private Double longi;
    private PullToRefreshLayout mPullToRefreshLayout;

    public static NearbyDealersFragment newInstance(int index) {
        NearbyDealersFragment f = new NearbyDealersFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_nearby_dealers_, container, false);
        inflater.inflate(R.layout.dealer_card, container, false);
        getActivity().setTitle("Dealers");
        setHasOptionsMenu(true);

        mPullToRefreshLayout = (PullToRefreshLayout)homeView.findViewById(R.id.carddemo_extra_ptr_layout1);
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
        RequestParams params = new RequestParams(param);

        dealersAsync(params);

    }

    private void dealersAsync(RequestParams params) {
        SpecialsRestClient.get("dealers", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray request) {
                ArrayList<Dealer> dealers = new ArrayList<Dealer>();
                try {
                    for (int i = 0; i < request.length(); i++) {
                        JSONObject outer = (JSONObject) request.get(i);
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

        });
    }

    private void addCards(ArrayList<Dealer> dealers) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards = createDealers(dealers, cards);

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        cardListView = (CardListView) homeView.findViewById(R.id.myList);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
        }

        if (mPullToRefreshLayout != null) {
            mPullToRefreshLayout.setRefreshComplete();
        }
    }

    public ArrayList<Card> createDealers(ArrayList<Dealer> dealers, ArrayList<Card> cards){
        for (Dealer dealer: dealers){
            DealerCard card = new DealerCard(getActivity(), R.layout.dealer_card, dealer.getLatitude(), dealer.getLongitude());
            card.setDealer(dealer.getName());
            card.setCityState(dealer.getCity() + ", " + dealer.getState());
            Double distance = distance(dealer.getLatitude(), dealer.getLongitude(), lat, longi);
            distance = (double)Math.round(distance *10)/10;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.search){
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra("tab", 2);
            startActivity(intent);
        }
        return true;
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
}
