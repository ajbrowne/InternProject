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

import com.example.specialsapp.app.Activities.HomeActivity;
import com.example.specialsapp.app.Activities.SearchActivity;
import com.example.specialsapp.app.Activities.SpecialDetail;
import com.example.specialsapp.app.Cards.VehicleCard;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Special;
import com.example.specialsapp.app.Models.Vehicle;
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
public class DealerSpecialsFragment extends BaseSearchFragment implements OnRefreshListener{

    private View homeView;
    private PullToRefreshLayout mPullToRefreshLayout;

    public static DealerSpecialsFragment newInstance(int index) {
        DealerSpecialsFragment fragment = new DealerSpecialsFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_dealer_specials, container, false);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Cobalt Deals");

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

        try {
            getDealerSpecials(longitude, latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return homeView;
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
        param.put("extra", "0");

        vehicleAsync(param, homeView, mPullToRefreshLayout, false);
    }
}
