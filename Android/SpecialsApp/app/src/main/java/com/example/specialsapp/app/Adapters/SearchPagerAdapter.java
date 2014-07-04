package com.example.specialsapp.app.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.specialsapp.app.Fragments.DealerSearchFragment;
import com.example.specialsapp.app.Fragments.GeneralSearchFragment;
import com.example.specialsapp.app.Fragments.VehicleSearchFragment;

/**
 *
 * Adapter used for switching tabs in HomeActivity
 *
 * Created by brownea on 6/23/14.
 */
public class SearchPagerAdapter extends FragmentPagerAdapter {

    public SearchPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new GeneralSearchFragment();
            case 1:
                return new VehicleSearchFragment();
            case 2:
                return new DealerSearchFragment();
        }
        return null;
    }

    public CharSequence getPageTitle(int position) {

        switch(position){
            case 0:
                return "General";
            case 1:
                return "Vehicles";
            case 2:
                return "Dealers";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
