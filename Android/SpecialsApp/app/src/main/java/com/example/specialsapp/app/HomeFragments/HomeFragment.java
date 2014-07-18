package com.example.specialsapp.app.HomeFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.specialsapp.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Home");

        TrendingFragment trendingFragment = new TrendingFragment();
        TopDiscountFragment topDiscountFragment = new TopDiscountFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.homeFragment1, trendingFragment);
        transaction.add(R.id.homeFragment2, topDiscountFragment).commit();

        return homeView;
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("RESUMING");
    }

}
