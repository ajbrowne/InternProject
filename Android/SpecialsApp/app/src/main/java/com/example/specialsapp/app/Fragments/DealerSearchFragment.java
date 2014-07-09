package com.example.specialsapp.app.Fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.specialsapp.app.Adapters.AlphaAdapter;
import com.example.specialsapp.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
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
                
            }
        });

        ArrayList<String> makesArray = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.makes)));
        AlphaAdapter alphaAdapter = new AlphaAdapter(getActivity(), R.layout.custom_item, makesArray);
        makes.setAdapter(alphaAdapter);

        return searchView;
    }
}
