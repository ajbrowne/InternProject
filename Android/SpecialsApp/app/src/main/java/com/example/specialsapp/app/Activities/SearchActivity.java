package com.example.specialsapp.app.Activities;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.example.specialsapp.app.Adapters.SearchPagerAdapter;
import com.example.specialsapp.app.R;


/**
 * Hosts all fragments that display dealers and their specials
 */
public class SearchActivity extends BaseActivity {

    private static final int numTabs = 3;

    /**
     * onCreate called when activity is created. This gets all needed views and carries
     * out necessary initializations.
     *
     * @param savedInstanceState -
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        int tabIndex = getIntent().getIntExtra("tabIndex", numTabs);

        SearchPagerAdapter mAdapter = new SearchPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.fragmentContainer3);
        viewPager.setAdapter(mAdapter);

        // Set title and backnav for action bar
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Search");

        // Set up tabs for this view
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs_search);
        tabs.setBackgroundColor(getResources().getColor(android.R.color.white));
        tabs.setTextColor(getResources().getColor(android.R.color.black));

        Typeface typeFace = Typeface.createFromAsset(getAssets(), FONT_PATH);
        tabs.setTypeface(typeFace, android.R.style.TextAppearance_DeviceDefault);
        viewPager.setAdapter(mAdapter);
        tabs.setViewPager(viewPager);

        // Set the correct search tab
        if (tabIndex != numTabs) {
            viewPager.setCurrentItem(tabIndex);
        }

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
        getMenuInflater().inflate(R.menu.search, menu);
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
        return super.onOptionsItemSelected(item);
    }

}
