package com.example.specialsapp.app.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.specialsapp.app.Fragments.DealerSpecialsFragment;
import com.example.specialsapp.app.Fragments.HomeFragment;
import com.example.specialsapp.app.Fragments.NearbyDealersFragment;
import com.example.specialsapp.app.Fragments.SearchFragment;

/**
 *
 * Adapter used for switching tabs in HomeActivity
 *
 * Created by brownea on 6/23/14.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new SearchFragment();
            case 2:
                return new DealerSpecialsFragment();
            case 3:
                return new NearbyDealersFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
