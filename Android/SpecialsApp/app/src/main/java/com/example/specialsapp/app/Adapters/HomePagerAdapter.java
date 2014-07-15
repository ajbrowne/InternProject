package com.example.specialsapp.app.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.example.specialsapp.app.Fragments.DealerSpecialsFragment;
import com.example.specialsapp.app.HomeFragments.HomeFragment;
import com.example.specialsapp.app.Fragments.NearbyDealersFragment;
import com.example.specialsapp.app.R;

/**
 *
 * Adapter used for switching tabs in HomeActivity
 *
 * Created by brownea on 6/23/14.
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter {

    private FragmentManager mFragmentManager;

    public HomePagerAdapter(FragmentManager fm){
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        String name = makeFragmentName(R.id.fragmentContainer2, position);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        switch (position){
            case 0:
                if (fragment == null){
                    fragment = HomeFragment.newInstance(position);
                }
                return fragment;
            case 1:
                if (fragment == null){
                    fragment = DealerSpecialsFragment.newInstance(position);
                }
                return fragment;
            case 2:
                if (fragment == null){
                    fragment = NearbyDealersFragment.newInstance(position);
                }
                return fragment;
        }
        return null;
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
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

    @Override
    public int getItemPosition(Object object) {
        System.out.println("GOT HERE");
        return POSITION_NONE;
    }
}
