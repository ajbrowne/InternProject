package com.example.specialsapp.app.Fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.LocationObject;
import com.example.specialsapp.app.R;

import org.json.JSONException;

import java.util.HashMap;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * The home view that displays all vehicles on special from the dealer
 * that is closest to your current location or entered zip.
 */
public class DealerSpecialsFragment extends BaseVehicleFragment implements OnRefreshListener {

    private View homeView;
    private PullToRefreshLayout mPullToRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_dealer_specials, container, false);
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Cobalt Deals");
        setHasOptionsMenu(true);

        // Set up the pull to refresh for this view
        mPullToRefreshLayout = (PullToRefreshLayout) homeView.findViewById(R.id.carddemo_extra_ptr_layout);
        ActionBarPullToRefresh.from(this.getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        GPS gps = new GPS(getActivity());
        LocationObject location = gps.checkLocationSettings();

        try {
            getDealerSpecials(location.getLatitude(), location.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return homeView;
    }

    /**
     * Executes the GET request again/uses cached info if possible
     * @param view
     */
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
     * @param lng - longitude
     * @param lat - latitude
     * @throws JSONException
     */
    public void getDealerSpecials(Double lng, Double lat) throws JSONException {

        String latt = String.valueOf(lat);
        String longg = String.valueOf(lng);

        HashMap<String, String> param = new HashMap<>();
        param.put("lng", longg);
        param.put("lat", latt);

        vehicleAsync(param, homeView, mPullToRefreshLayout, false);
    }
}
