package com.example.specialsapp.app.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.specialsapp.app.Fragments.BlankFragment;
import com.example.specialsapp.app.Fragments.DealerSpecialsFragment;
import com.example.specialsapp.app.Fragments.NearbyDealersFragment;

/**
 * Created by brownea on 6/23/14.
 */
public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    public TabsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DealerSpecialsFragment();
            case 1:
                return new BlankFragment();
            case 2:
                return new BlankFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
