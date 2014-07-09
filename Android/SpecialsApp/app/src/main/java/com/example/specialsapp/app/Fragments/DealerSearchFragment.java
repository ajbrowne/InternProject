package com.example.specialsapp.app.Fragments;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.specialsapp.app.Activities.DealerResultsActivity;
import com.example.specialsapp.app.Adapters.AlphaAdapter;
import com.example.specialsapp.app.Cards.DealerCard;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.R;
import com.example.specialsapp.app.Rest.SpecialsRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class DealerSearchFragment extends Fragment {

    private View searchView;

    public DealerSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        searchView = inflater.inflate(R.layout.fragment_dealer_search, container, false);
        // Inflate the layout for this fragment

        ListView makes = (ListView) searchView.findViewById(R.id.dealer_search_listview);

        makes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DealerResultsActivity.class);
                intent.putExtra("make", ((TextView)view).getText().toString());
                startActivity(intent);
            }
        });

        ArrayList<String> makesArray = new ArrayList<String>(Arrays.asList(getActivity().getResources().getStringArray(R.array.makes)));
        AlphaAdapter alphaAdapter = new AlphaAdapter(getActivity(), R.layout.custom_item, makesArray);
        makes.setAdapter(alphaAdapter);

        return searchView;
    }

}
