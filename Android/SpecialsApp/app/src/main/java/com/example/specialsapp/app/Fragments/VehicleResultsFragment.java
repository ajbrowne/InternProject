package com.example.specialsapp.app.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.R;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class VehicleResultsFragment extends BaseSearchFragment implements OnRefreshListener {

    private View resultsView;
    private static final double defaultLocation = -1000.0;
    private String[] params = new String[5];
    private PullToRefreshLayout mPullToRefreshLayout;
    private RequestParams parameters;

    public VehicleResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        resultsView = inflater.inflate(R.layout.fragment_vehicle_results, container, false);

        String zip = getActivity().getIntent().getStringExtra("zip");
        params = getActivity().getIntent().getStringArrayExtra("params");

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setTitle("Vehicle Results");

        mPullToRefreshLayout = (PullToRefreshLayout)resultsView.findViewById(R.id.carddemo_extra_ptr_layout);
        ActionBarPullToRefresh.from(this.getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        search(zip);
        return resultsView;
    }

    private void search(String zip) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final GPS gps = new GPS(getActivity());
        Double latitude = gps.getLatitude();
        Double longitude = gps.getLongitude();
        String latt = String.valueOf(latitude);
        String longi = String.valueOf(longitude);

        HashMap<String, String> param = new HashMap<String, String>();
        if (sharedPreferences.getBoolean("use_location", false)) {
            param.put("lng", longi);
            param.put("lat", latt);
        } else {
            double[] location = getLoc(zip);
            System.out.println(location[0]);
            if (location[0] != defaultLocation) {
                latt = String.valueOf(location[0]);
                longi = String.valueOf(location[1]);
                param.put("lng", longi);
                param.put("lat", latt);
            }

        }

        param.put("make", params[0]);
        param.put("model", params[1]);
        param.put("type", params[2]);
        param.put("max", params[3]);
        param.put("extra", params[4]);
        parameters = new RequestParams(param);
        System.out.println("PARAMS: " + parameters);
        vehicleAsync(parameters, resultsView, mPullToRefreshLayout);
    }

    @Override
    public void onRefreshStarted(View view) {
        vehicleAsync(parameters, resultsView, mPullToRefreshLayout);
    }
}


