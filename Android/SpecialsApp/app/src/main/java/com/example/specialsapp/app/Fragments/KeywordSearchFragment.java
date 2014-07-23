package com.example.specialsapp.app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.specialsapp.app.Activities.VehicleResultsActivity;
import com.example.specialsapp.app.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class KeywordSearchFragment extends Fragment {

    private static final String SPACE = "%20";

    public KeywordSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View searchView = inflater.inflate(R.layout.fragment_keyword_search, container, false);
        final EditText searchPhrase = (EditText) searchView.findViewById(R.id.search_box);
        Button submit = (Button) searchView.findViewById(R.id.keyword_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchPhrase.getText().toString();
                searchText = searchText.replaceAll(" ", SPACE);
                String[] isKeyword = {"keyword", searchText};
                Intent intent = new Intent(getActivity(), VehicleResultsActivity.class);
                intent.putExtra("params", isKeyword);
                startActivity(intent);
            }
        });

        return searchView;
    }


}
