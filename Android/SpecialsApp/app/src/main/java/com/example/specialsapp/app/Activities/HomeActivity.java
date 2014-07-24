package com.example.specialsapp.app.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.example.specialsapp.app.Adapters.HomePagerAdapter;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.R;

import java.util.ArrayList;

/**
 * Holds, the HomeFragment, DealerSpecialsFragment, and NearbyDealersFragment.
 */
public class HomeActivity extends BaseActivity {

    private ArrayList dealers;
    private ViewPager viewPager;

    /**
     * onCreate called when activity is created. This gets all needed views and carries
     * out necessary initializations.
     *
     * @param savedInstanceState -
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dealers = new ArrayList();

        // Create tabs and change background/text color
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setBackgroundColor(getResources().getColor(android.R.color.white));
        tabs.setTextColor(getResources().getColor(android.R.color.black));

        Typeface typeFace = Typeface.createFromAsset(getAssets(), FONT_PATH);
        tabs.setTypeface(typeFace, android.R.style.TextAppearance_DeviceDefault);
        viewPager = (ViewPager) findViewById(R.id.fragmentContainer2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        HomePagerAdapter mAdapter = new HomePagerAdapter(fragmentManager);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabs.setViewPager(viewPager);
    }

    /**
     * onCreateOptionsMenu gets the correct xml for the menu among other setup.
     *
     * @param menu - the menu for the activity
     * @return - true upon success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * onOptionsSelected handles action bar behavior for an activity.
     *
     * @param item - the selected item
     * @return - true upon success
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Go to the correct search fragment
        if (id == R.id.search) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("tabIndex", viewPager.getCurrentItem());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Return list of dealers
     *
     * @return - dealers
     */
    public ArrayList<Dealer> getDealers() {
        return dealers;
    }

    /**
     * Set list of dealers
     *
     * @param dealers - dealers being stored
     */
    public void setDealers(ArrayList<Dealer> dealers) {
        this.dealers = dealers;
    }

    /**
     * Return the ViewPager for the HomeActivity which is used in correct tab switching.
     *
     * @return - the ViewPager
     */
    public ViewPager getViewPager() {
        return this.viewPager;
    }
}
