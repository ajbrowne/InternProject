package com.example.specialsapp.app.Activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.specialsapp.app.Adapters.HomePagerAdapter;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.R;

import java.util.ArrayList;

/**
 * Hosts all fragments that display dealers and their specials
 */
public class HomeActivity extends BaseActivity {

    private ViewPager viewPager;
    private String[] tabs = {"Home", "Vehicles", "Dealers"};
    private ArrayList<Dealer> dealers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ActionBar actionBar = getActionBar();

        dealers = new ArrayList<Dealer>();
        viewPager = (ViewPager) findViewById(R.id.fragmentContainer2);
        HomePagerAdapter mAdapter = new HomePagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Dealer> getDealers() {
        return dealers;
    }

    public void setDealers(ArrayList<Dealer> dealers) {
        this.dealers = dealers;
    }

}
