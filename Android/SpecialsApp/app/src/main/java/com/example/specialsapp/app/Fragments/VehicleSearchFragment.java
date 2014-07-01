package com.example.specialsapp.app.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.specialsapp.app.Activities.HomeActivity;
import com.example.specialsapp.app.Activities.SearchActivity;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Special;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleSearchFragment extends Fragment {

    private Spinner makeSpinner;
    private Spinner modelSpinner;
    private Spinner yearSpinner;
    private Spinner priceSpinner;
    private Spinner typeSpinner;
    private View view;
    private EditText zip;
    private Button submitSearch;
    private String[] params = new String[5];
    private RequestParams parameters;


    public VehicleSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View searchView = inflater.inflate(R.layout.fragment_special_search, container, false);
        view = searchView;

        submitSearch = (Button) searchView.findViewById(R.id.searchButton);
        makeSpinner = (Spinner) searchView.findViewById(R.id.makeSpinner);
        modelSpinner = (Spinner) searchView.findViewById(R.id.modelSpinner);
        priceSpinner = (Spinner) searchView.findViewById(R.id.priceSpinner);
        typeSpinner = (Spinner) searchView.findViewById(R.id.typeSpinner);
        zip = (EditText) searchView.findViewById(R.id.search_zip);

        setSpinnerListener(makeSpinner, 0);
        setSpinnerListener(modelSpinner, 1);
        setSpinnerListener(typeSpinner, 2);
        setSpinnerListener(priceSpinner, 3);

        submitSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        return searchView;
    }

    public void setSpinnerListener(final Spinner spinner, final int index) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = spinner.getSelectedItem().toString();
                params[index] = selected;
                if (index == 0){
                    int identifier = getActivity().getResources().getIdentifier(spinner.getSelectedItem().toString(), "array", getActivity().getPackageName());
                    String[] models = getActivity().getResources().getStringArray(identifier);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, models); //selected item will look like a spinner set from XML
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    modelSpinner.setAdapter(spinnerArrayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void search() {
        final GPS gps = new GPS(getActivity());
        Double latitiude = gps.getLatitude();
        Double longitude = gps.getLongitude();
        String latt = String.valueOf(latitiude);
        String longi = String.valueOf(longitude);

        HashMap<String, String> param = new HashMap<String, String>();
        if (zip.getText().toString().compareTo("") == 0) {
            param.put("lng", latt);
            param.put("lat", longi);
        } else {
            double[] location = ((SearchActivity)getActivity()).getLoc(zip.getText().toString());
            if (location[0] != -1000){
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
        parameters = new RequestParams(param);

        SpecialsRestClient.get("vehicle", parameters, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray request) {
                //ArrayList<Special> specials = new ArrayList<Special>();

                    System.out.println(request.toString());
//                    JSONObject dealer = (JSONObject) request.get(0);
//                    JSONArray specialArray = (JSONArray) dealer.get("specials");
//                    for (int i = 0; i < specialArray.length(); i++) {
//                        Special special = new Special();
//                        JSONObject spec = (JSONObject) specialArray.get(i);
//                        special.setTitle(spec.getString("title"));
//                        special.setDealer(dealer.getString("dealerName"));
//                        special.setDescription(spec.getString("description"));
//                        special.setType(spec.getString("type"));
//                        specials.add(special);
//                    }


            }

        });
    }
}
