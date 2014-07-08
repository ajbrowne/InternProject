package com.example.specialsapp.app.Fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.specialsapp.app.AlertDialogs.CustomAlertDialog;
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
 *
 */
public class GeneralSearchFragment extends Fragment {

    private EditText searchPhrase;
    private EditText zip;
    private RequestParams parameters;

    public GeneralSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general_search, container, false);
        Button search = (Button)view.findViewById(R.id.keyword_button);
        searchPhrase = (EditText)view.findViewById(R.id.keyword);
        zip = (EditText)view.findViewById(R.id.general_zip);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zipCode = zip.getText().toString();
                String phrase = searchPhrase.getText().toString();
                if (phrase.compareTo("") == 0){
                    new CustomAlertDialog(getActivity(), "No search value entered", "Please enter something into the search window to proceed").show();
                }
                if (zipCode.compareTo("") == 0){
                    final GPS gps = new GPS(getActivity());
                    Double latitude = gps.getLatitude();
                    Double longitude = gps.getLongitude();
                    String latt = String.valueOf(latitude);
                    String longi = String.valueOf(longitude);
                    search(latt, longi, phrase);
                }
                else{
                    search(zipCode, phrase);
                }
            }
        });

        return view;
    }

    public void search(String zipCode, String phrase){
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("zip", zipCode);
        param.put("phrase", phrase);
        parameters = new RequestParams(param);
        async(parameters);
    }

    public void search(String lat, String longi, String phrase){
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("lng", longi);
        param.put("lat", lat);
        param.put("phrase", phrase);
        parameters = new RequestParams(param);
        async(parameters);
    }

    public void async(RequestParams theParams){
        SpecialsRestClient.get("special", theParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray request) {
                ArrayList<Special> specials = new ArrayList<Special>();
                try {
                    JSONObject dealer = (JSONObject) request.get(0);
                    JSONArray specialArray = (JSONArray) dealer.get("specials");
                    for (int i = 0; i < specialArray.length(); i++) {
                        Special special = new Special();
                        JSONObject spec = (JSONObject) specialArray.get(i);
                        special.setTitle(spec.getString("title"));
                        special.setDealer(dealer.getString("dealerName"));
                        special.setDescription(spec.getString("description"));
                        special.setType(spec.getString("type"));
                        specials.add(special);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }
}
