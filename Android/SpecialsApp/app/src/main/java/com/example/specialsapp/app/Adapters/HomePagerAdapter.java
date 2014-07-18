package com.example.specialsapp.app.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.specialsapp.app.Fragments.DealerSpecialsFragment;
import com.example.specialsapp.app.Fragments.NearbyDealersFragment;
import com.example.specialsapp.app.HomeFragments.HomeFragment;

/**
 * Adapter used for switching tabs in HomeActivity
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter {

    public HomePagerAdapter(FragmentManager fm) {
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
                return new HomeFragment();
            case 1:
                return new DealerSpecialsFragment();
            case 2:
                return new NearbyDealersFragment();
        }
        return null;
    }

    /**
     * Sets the title for each tab.
     * @param position - position of tab
     * @return - the title of the tab
     */
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Home";
            case 1:
                return "Vehicles";
            case 2:
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
        return 3;
    }

}
