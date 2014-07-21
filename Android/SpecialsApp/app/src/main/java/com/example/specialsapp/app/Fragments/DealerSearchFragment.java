package com.example.specialsapp.app.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.specialsapp.app.Activities.DealerResultsActivity;
import com.example.specialsapp.app.Adapters.VolleyAdapter;
import com.example.specialsapp.app.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Fragment that gets the value to be used for the dealer search.
 */
public class DealerSearchFragment extends Fragment {

    public DealerSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View searchView = inflater.inflate(R.layout.fragment_dealer_search, container, false);
        // Inflate the layout for this fragment

        ListView makes = (ListView) searchView.findViewById(R.id.dealer_search_listview);

        makes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DealerResultsActivity.class);
                intent.putExtra("make", ((TextView) view).getText().toString().replaceAll(" ", "%20"));
                startActivity(intent);
            }
        });

        ArrayList<String> makesArray = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.makes)));
        VolleyAdapter alphaAdapter = new VolleyAdapter(getActivity(), R.layout.custom_item, makesArray);
        makes.setAdapter(alphaAdapter);

        return searchView;
    }

}
