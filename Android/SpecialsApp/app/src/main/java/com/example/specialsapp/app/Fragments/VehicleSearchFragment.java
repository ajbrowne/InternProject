package com.example.specialsapp.app.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.specialsapp.app.Activities.VehicleResultsActivity;
import com.example.specialsapp.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleSearchFragment extends Fragment {

    private static final int MAKE = 0;
    private static final int MODEL = 1;
    private static final int TYPE  = 2;
    private static final String SPACE = "%20";

    private Spinner makeSpinner;
    private Spinner modelSpinner;
    private Spinner typeSpinner;
    private String[] params = new String[5];

    public VehicleSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View searchView = inflater.inflate(R.layout.fragment_special_search, container, false);

        final SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

        Button submitSearch = (Button) searchView.findViewById(R.id.searchButton);
        makeSpinner = (Spinner) searchView.findViewById(R.id.makeSpinner);
        modelSpinner = (Spinner) searchView.findViewById(R.id.modelSpinner);
        typeSpinner = (Spinner) searchView.findViewById(R.id.typeSpinner);

        setSpinnerListener(makeSpinner, MAKE);
        setSpinnerListener(modelSpinner, MODEL);
        setSpinnerListener(typeSpinner, TYPE);

        submitSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sets parameters to "" for when all default fields are used
                if (makeSpinner.getSelectedItem().toString().equals("Any")) {
                    params[MAKE] = "";
                    params[MODEL] = "";
                }
                if (typeSpinner.getSelectedItem().toString().compareTo("Any") == 0) {
                    params[TYPE] = "";
                }
                if (modelSpinner.getSelectedItem().toString().compareTo("All") == 0) {
                    params[MODEL] = "";
                }

                params[MAKE] = params[MAKE].replaceAll(" ", SPACE);
                params[MODEL] = params[MODEL].replaceAll(" ", SPACE);
                Intent intent = new Intent(getActivity(), VehicleResultsActivity.class);
                intent.putExtra("params", params);

                String zipValue = sharedPrefs.getBoolean("use_location", false)
                        ? sharedPrefs.getString("zip", "")
                        : "nope";
                intent.putExtra("zip", zipValue);
                startActivity(intent);
            }
        });

        return searchView;
    }

    /**
     * Listener that pulls the correct string-array of vehicle models for the selected make.
     * Also handles when we don't have data for that make or when the make is "Any".
     *
     * @param spinner - spinner that was clicked
     * @param index - index used to change correct spinner
     *
     */
    public void setSpinnerListener(final Spinner spinner, final int index) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = spinner.getSelectedItem().toString();
                params[index] = selected;
                String theStringField;
                if (index == 0) {
                    theStringField = spinner.getSelectedItem().toString();
                    theStringField = theStringField.replaceAll(" ", "").replaceAll("-", "");
                    int identifier = 0;
                    String[] models;
                    identifier = getActivity().getResources().getIdentifier(theStringField, "array", getActivity().getPackageName());
                    if (identifier == 0) {
                        identifier = getActivity().getResources().getIdentifier("none", "array", getActivity().getPackageName());
                        models = getActivity().getResources().getStringArray(identifier);
                    } else if (theStringField.equals("Any")) {
                        identifier = getActivity().getResources().getIdentifier("any", "array", getActivity().getPackageName());
                        models = getActivity().getResources().getStringArray(identifier);
                    } else {
                        models = getActivity().getResources().getStringArray(identifier);
                    }
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, models); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    modelSpinner.setAdapter(spinnerArrayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (index == 1) {
                    String[] models = getActivity().getResources().getStringArray(R.array.any);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, models);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    modelSpinner.setAdapter(spinnerArrayAdapter);
                }
            }
        });
    }

}
