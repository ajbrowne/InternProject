package com.example.specialsapp.app.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.specialsapp.app.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SearchFragment extends Fragment {

    private Spinner makeSpinner;
    private Spinner modelSpinner;
    private Spinner yearSpinner;
    private Spinner priceSpinner;
    private Spinner typeSpinner;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View searchView = inflater.inflate(R.layout.fragment_search, container, false);

        initializeSpinners(searchView);

        return searchView;
    }

    public void initializeSpinners(View searchView){
        makeSpinner = (Spinner) searchView.findViewById(R.id.makeSpinner);
        modelSpinner = (Spinner) searchView.findViewById(R.id.modelSpinner);
        yearSpinner = (Spinner) searchView.findViewById(R.id.yearSpinner);
        priceSpinner = (Spinner) searchView.findViewById(R.id.priceSpinner);
        typeSpinner = (Spinner) searchView.findViewById(R.id.typeSpinner);

        populateSpinner(makeSpinner, R.array.makes);
        populateSpinner(modelSpinner, R.array.models_Ford);
        populateSpinner(yearSpinner, R.array.year);
        populateSpinner(priceSpinner, R.array.priceRange);
        populateSpinner(typeSpinner, R.array.type);
    }

    public void populateSpinner(Spinner spinner, int array_id){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), array_id, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }


}
