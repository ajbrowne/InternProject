package com.example.specialsapp.app.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.specialsapp.app.Fragments.DealerSearchFragment;
import com.example.specialsapp.app.Fragments.VehicleSearchFragment;

/**
 * Adapter used for switching tabs in SearchActivity
 */
public class SearchPagerAdapter extends FragmentPagerAdapter {

    public SearchPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Handles these tab indexes upon creation.
     * @param position - position of tab
     * @return - the fragment that is the tab
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new VehicleSearchFragment();
            case 1:
                return new DealerSearchFragment();
        }
        return null;
    }

    /**
     * Sets the title for each tab.
     * @param position - position of tab
     * @return - the title of the tab
     */
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Vehicles";
            case 1:
                return "Dealers";
        }
        return null;
    }

    /**
     * Return the total number of tabs
     * @return - the number of tabs
     */
    @Override
    public int getCount() {
        return 2;
    }
}
