package com.example.specialsapp.app.Fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.example.specialsapp.app.Activities.SearchActivity;
import com.example.specialsapp.app.Activities.SpecialDetail;
import com.example.specialsapp.app.Cards.SpecialCard;
import com.example.specialsapp.app.GPS.GPS;
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
 *
 * Currently the home view that displays all specials from the dealer
 * that is closest to your current location.
 *
 * Created by brownea on 6/12/14.
 */
public class DealerSpecialsFragment extends Fragment implements OnRefreshListener, AbsListView.OnScrollListener{

    private View homeView;
    private PullToRefreshLayout mPullToRefreshLayout;
    private CardArrayAdapter mCardArrayAdapter;
    private ArrayList<Special> specials;
    private ArrayList<Card> cards;
    private int currIndex, returnSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_dealer_specials, container, false);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Specials");

        setHasOptionsMenu(true);

        mPullToRefreshLayout = (PullToRefreshLayout)homeView.findViewById(R.id.carddemo_extra_ptr_layout);
        ActionBarPullToRefresh.from(this.getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        // Get location upon opening app, returning to Dealers
        final GPS gps = new GPS(getActivity());
        Double latitude = gps.getLatitude();
        Double longitude = gps.getLongitude();

        // Call to retrieve specials to display
        try {
            getDealerSpecials(longitude, latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return homeView;
    }


    @Override
    public void onRefreshStarted(View view) {
        final GPS gps = new GPS(getActivity());
        Double latitude = gps.getLatitude();
        Double longitude = gps.getLongitude();
        // Call to retrieve specials to display
        try {
           getDealerSpecials(longitude, latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds nearest dealers (x determined in api) to given lat and long
     *
     * @param lng  - longitude
     * @param lat  - latitude
     * @throws JSONException
     */
    public void getDealerSpecials(Double lng, Double lat) throws JSONException {

        String latt = String.valueOf(lat);
        String longg = String.valueOf(lng);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("lng", longg);
        param.put("lat", latt);
        param.put("make", "");
        RequestParams params = new RequestParams(param);

        System.out.println(params);

        specialsAsync(params);
    }

    private void specialsAsync(RequestParams params) {
        SpecialsRestClient.get("vehicle", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray request) {
                ArrayList<Special> specials = new ArrayList<Special>();
                try {
                    JSONObject dealer = (JSONObject) request.get(0);
                    JSONArray specialArray = (JSONArray) dealer.get("specials");
                    for (int i = 0; i < specialArray.length(); i++) {
                        Special special = new Special();
                        JSONObject spec = (JSONObject) specialArray.get(i);
                        JSONArray vehicles = (JSONArray) spec.get(("vehicleId"));
                        if (vehicles.length() == 1) {
                            JSONArray vehicles2 = (JSONArray) dealer.get("vehicles");
                            for (int j = 0; j < vehicles2.length(); j++) {
                                JSONObject vehicle = (JSONObject) vehicles2.get(j);
                                JSONArray ids = (JSONArray) spec.get("vehicleId");
                                if (vehicle.getString("id").compareTo((String) ids.get(0)) == 0) {
                                    special.setPrice(vehicle.getInt("price"));
                                    special.setAmount(spec.getString("amount"));
                                }
                            }
                        } else {
                            special.setAmount("Multiple Vehicles");
                        }
                        special.setTitle(spec.getString("title"));
                        special.setDealer(dealer.getString("dealerName"));
                        special.setDescription(spec.getString("description"));
                        special.setType(spec.getString("type"));
                        specials.add(special);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addCards(specials);
            }
        });
    }

    private void addCards(ArrayList<Special> specials) {
        returnSize = specials.size();
        cards = new ArrayList<Card>();
        cards = createSpecials(0, specials, cards);
        mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        CardListView cardListView = (CardListView) homeView.findViewById(R.id.myList1);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
            cardListView.setOnScrollListener(this);
        }

        if (mPullToRefreshLayout != null) {
            mPullToRefreshLayout.setRefreshComplete();
        }
    }

    /**
     * Creates cards for a given ArrayList of specials
     *
     * @param specials - Specials that will have cards created for them
     * @return Arraylist of created cards
     */
    public ArrayList<Card> createSpecials(int index, ArrayList<Special> specials, ArrayList<Card> cards) {
        for (int i = index; i < index+10 && i < returnSize; i++) {
            SpecialCard card = new SpecialCard(getActivity(), R.layout.special_card);
            card.setTitle(specials.get(i).getTitle());
            card.setDescription(specials.get(i).getDescription());
            card.setDealer(specials.get(i).getDealer());
            card.setSpecialType(specials.get(i).getType());
            if (specials.get(i).getPrice() != -1000){
                int old = Integer.parseInt(specials.get(i).getAmount());
                card.setNewPrice(String.valueOf(specials.get(i).getPrice() - old));
                card.setOldPrice(String.valueOf(specials.get(i).getPrice()));
            }
            else{
                card.setOldPrice(specials.get(i).getAmount());
            }

            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Intent intent = new Intent(getActivity(), SpecialDetail.class);
                    SpecialCard temp = (SpecialCard) card;
                    intent.putExtra("title",  temp.getTitle());
                    intent.putExtra("description", temp.getDescription());
                    intent.putExtra("oldP", temp.getOldPrice());
                    intent.putExtra("newP", temp.getNewPrice());
                    getActivity().startActivity(intent);

                }
            });

            cards.add(card);
            currIndex = i;
        }
        currIndex++;
        return cards;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

        if (loadMore && currIndex < returnSize-1){
            createSpecials(currIndex, specials, cards);
            mCardArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.search){
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra("tab", 1);
            startActivity(intent);
        }
        return true;
    }

}
