package com.example.specialsapp.app.Activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.specialsapp.app.Adapters.SearchPagerAdapter;
import com.example.specialsapp.app.R;


/**
 * Hosts all fragments that display dealers and their specials
 */
public class SearchActivity extends BaseActivity {

    private ViewPager viewPager;
    private final String[] tabs = {"General", "Vehicles", "Dealers"};
    private static final int numTabs = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        int tabIndex = getIntent().getIntExtra("tab", numTabs);

        final ActionBar actionBar = getActionBar();

        SearchPagerAdapter mAdapter; mAdapter = new SearchPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.fragmentContainer3);

        viewPager.setAdapter(mAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Search");

        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                }
        );

        // Listener for tab changes
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

            }
        };

        // Add tabs to action bar
        for (String tab : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab).setTabListener(tabListener));
        }

        if (tabIndex != numTabs){
            actionBar.setSelectedNavigationItem(tabIndex);
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
