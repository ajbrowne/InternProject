package com.example.specialsapp.app.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.specialsapp.app.Fragments.DealerSpecialsFragment;
import com.example.specialsapp.app.HomeFragments.HomeFragment;
import com.example.specialsapp.app.Fragments.NearbyDealersFragment;

/**
 *
 * Adapter used for switching tabs in HomeActivity
 *
 * Created by brownea on 6/23/14.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    public HomePagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new DealerSpecialsFragment();
            case 2:
                return new NearbyDealersFragment();
        }
        return null;
    }

    public CharSequence getPageTitle(int position) {

        switch(position){
            case 0:
                return "Home";
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
