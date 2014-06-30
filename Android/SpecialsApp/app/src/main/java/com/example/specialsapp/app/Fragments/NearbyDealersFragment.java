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
    private ArrayList<Card> cards = new ArrayList<Card>();
    private CardListView cardListView;
    private TextView dealerName;
    private Double lat;
    private Double longi;
    private PullToRefreshLayout mPullToRefreshLayout;

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

        ActionBar actionBar = ((HomeActivity) getActivity()).getActionBar();

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
                        dealer.setCity(dealerObject.get("city").toString());
                        dealer.setState(dealerObject.get("state").toString());
                        dealer.setName(dealerObject.get("name").toString());
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

                if(mPullToRefreshLayout != null){
                    mPullToRefreshLayout.setRefreshComplete();
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

    @Override
    public void onRefreshStarted(View view) {
        final GPS gps = new GPS(getActivity());
        Double latitiude = gps.getLatitude();
        Double longitude = gps.getLongitude();
        // Call to retrieve dealers to display
        getDealers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.search){
            SearchActivity searchActivity = new SearchActivity();
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra("tab", 2);
            startActivity(intent);
        }
        return true;
    }
}
