package com.example.specialsapp.app.Fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.R;

import org.json.JSONException;

import java.util.HashMap;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Currently the home view that displays all specials from the dealer
 * that is closest to your current location.
 * <p/>
 * Created by brownea on 6/12/14.
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

        mPullToRefreshLayout = (PullToRefreshLayout) homeView.findViewById(R.id.carddemo_extra_ptr_layout);
        ActionBarPullToRefresh.from(this.getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        //TODO ***make a GPS location class that is just 2 doubles.
        //TODO It is almost always best to create objects for these kinds of things

        GPS gps = new GPS(getActivity());
        double[] location = gps.checkLocationSettings();

        try {
            getDealerSpecials(location[0], location[1]);
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
     * @param lng - longitude
     * @param lat - latitude
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
