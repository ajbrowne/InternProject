package com.example.specialsapp.app.Fragments;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.specialsapp.app.Activities.HomeActivity;
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
    private CardListView cardListView;
    private int currIndex, returnSize;
    private Menu menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_dealer_specials, container, false);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Specials");

        this.menu = ((HomeActivity)getActivity()).getMenu();

        getActivity().setTitle("Specials");
        mPullToRefreshLayout = (PullToRefreshLayout)homeView.findViewById(R.id.carddemo_extra_ptr_layout);
        ActionBarPullToRefresh.from(this.getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        // Get location upon opening app, returning to Dealers
        final GPS gps = new GPS(getActivity());
        Double latitiude = gps.getLatitude();
        Double longitude = gps.getLongitude();

        // Call to retrieve specials to display
        try {
            getDealerSpecials(longitude, latitiude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return homeView;
    }


    @Override
    public void onRefreshStarted(View view) {
        final GPS gps = new GPS(getActivity());
        Double latitiude = gps.getLatitude();
        Double longitude = gps.getLongitude();
        // Call to retrieve specials to display
        try {
           getDealerSpecials(longitude, latitiude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds nearest dealers (x determined in api) to given lat and long
     *
     * @param lng  - longitude
     * @param lat  - latitude
     * @return ArrayList of dealers found
     * @throws JSONException
     */
    public void getDealerSpecials(Double lng, Double lat) throws JSONException {

        String latt = String.valueOf(lat);
        String longg = String.valueOf(lng);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("lng", longg);
        param.put("lat", latt);
        RequestParams params = new RequestParams(param);

        SpecialsRestClient.get("special", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray request) {
                specials = new ArrayList<Special>();
                try {
                    JSONObject dealer = (JSONObject) request.get(0);
                    JSONArray specialArray = (JSONArray) dealer.get("specials");
                    for (int i = 0; i < specialArray.length(); i++) {
                        Special special = new Special();
                        JSONObject spec = (JSONObject) specialArray.get(i);
                        special.setTitle(spec.getString("title"));
                        special.setDealer(dealer.getString("dealerName"));
                        special.setDescription(spec.getString("description"));
                        special.setType(spec.getString("type"));
                        specials.add(special);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                returnSize = specials.size();
                cards = new ArrayList<Card>();
                cards = createSpecials(0, specials, cards);
                mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

                cardListView = (CardListView) homeView.findViewById(R.id.myList1);
                if (cardListView != null) {
                    cardListView.setAdapter(mCardArrayAdapter);
                    cardListView.setOnScrollListener(DealerSpecialsFragment.this);
                }

                if (mPullToRefreshLayout != null) {
                    mPullToRefreshLayout.setRefreshComplete();
                }
            }

        });
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

}
