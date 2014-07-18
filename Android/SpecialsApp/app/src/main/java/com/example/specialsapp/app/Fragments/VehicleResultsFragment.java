package com.example.specialsapp.app.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.R;

import java.util.HashMap;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleResultsFragment extends BaseVehicleFragment implements OnRefreshListener {

    HashMap<String, String> param = new HashMap<String, String>();
    private View resultsView;
    private String[] params = new String[5];
    private PullToRefreshLayout mPullToRefreshLayout;

    public VehicleResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        resultsView = inflater.inflate(R.layout.fragment_vehicle_results, container, false);

        params = getActivity().getIntent().getStringArrayExtra("params");

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setTitle("Vehicle Results");

        mPullToRefreshLayout = (PullToRefreshLayout) resultsView.findViewById(R.id.carddemo_extra_ptr_layout);
        ActionBarPullToRefresh.from(this.getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        search();
        return resultsView;
    }

    private void search() {
        final GPS gps = new GPS(getActivity());
        double[] location = gps.checkLocationSettings();
        Double latitude = location[0];
        Double longitude = location[1];
        String latt = String.valueOf(latitude);
        String longi = String.valueOf(longitude);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("lat", latt);
        param.put("lng", longi);
        param.put("make", params[0]);
        param.put("model", params[1]);
        param.put("type", params[2]);
        param.put("max", params[3]);
        param.put("extra", params[4]);
        vehicleAsync(param, resultsView, mPullToRefreshLayout, true);
    }

    @Override
    public void onRefreshStarted(View view) {
        vehicleAsync(param, resultsView, mPullToRefreshLayout, true);
    }
}


