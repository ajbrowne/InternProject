package com.example.specialsapp.app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.Volley;
import com.example.specialsapp.app.Adapters.AssetsPropertyAdapter;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.Models.LocationObject;
import com.example.specialsapp.app.R;

import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Displays all dealers nearest to your current location/entered zip
 */
public class NearbyDealersFragment extends BaseDealerFragment implements OnRefreshListener {

    private static String baseUrl;
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

        // Get url from properties file
        AssetsPropertyAdapter assetsPropertyAdapter = new AssetsPropertyAdapter(getActivity());
        Properties properties = assetsPropertyAdapter.getProperties("specials.properties");
        baseUrl = properties.getProperty("baseUrl") + properties.getProperty("dealer");

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
        makeRequest(queue, url, lat, longi, homeView, false);
    }

    @Override
    public void onRefreshStarted(View view) {
        gps.checkLocationSettings();
        LocationObject location = gps.checkLocationSettings();
        lat = location.getLatitude();
        longi = location.getLongitude();
        System.out.println("leggo");
        getDealers();
    }

    /**
     * Builds the url for the GET request.
     *
     * @param parameters - map of parameters
     * @return - the generated url
     */
    public String generateUrl(HashMap<String, String> parameters) {
        return baseUrl + "lng=" + parameters.get("lng") + "&lat=" + parameters.get("lat");
    }

    /**
     * Creates the ArrayList for the cards and calls createDealerCards to make the cards.
     * Then sets the adapter for the cards, making them visible.
     *
     * @param dealers - Any dealers that will have cards made for them.
     */
    public void addCards(ArrayList<Dealer> dealers) {
        ArrayList cards = new ArrayList();
        createDealerCards(dealers, cards);

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        CardListView cardListView = (CardListView) homeView.findViewById(R.id.myList);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
        }

        if (mPullToRefreshLayout != null) {
            mPullToRefreshLayout.setRefreshComplete();
        }
    }
}
