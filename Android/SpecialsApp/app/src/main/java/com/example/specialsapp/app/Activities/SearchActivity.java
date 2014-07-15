package com.example.specialsapp.app.Activities;

import android.app.ActionBar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        int tabIndex = getIntent().getIntExtra("tabIndex", numTabs);

        final ActionBar actionBar = getActionBar();

        SearchPagerAdapter mAdapter; mAdapter = new SearchPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.fragmentContainer3);

        viewPager.setAdapter(mAdapter);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Search");

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs_search);
        tabs.setBackgroundColor(getResources().getColor(android.R.color.white));
        tabs.setTextColor(getResources().getColor(android.R.color.black));

        viewPager.setAdapter(mAdapter);
        tabs.setViewPager(viewPager);

        if (tabIndex != numTabs){
            viewPager.setCurrentItem(tabIndex);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
