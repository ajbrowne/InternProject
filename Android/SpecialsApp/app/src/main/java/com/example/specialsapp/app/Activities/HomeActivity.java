package com.example.specialsapp.app.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.example.specialsapp.app.Adapters.HomePagerAdapter;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.R;
import java.util.ArrayList;

/**
 * Hosts all fragments that display dealers and their specials
 */
public class HomeActivity extends BaseActivity {

    private ArrayList<Dealer> dealers;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dealers = new ArrayList<Dealer>();

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setBackgroundColor(getResources().getColor(android.R.color.white));
        tabs.setTextColor(getResources().getColor(android.R.color.black));
        viewPager = (ViewPager) findViewById(R.id.fragmentContainer2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        HomePagerAdapter mAdapter = new HomePagerAdapter(fragmentManager);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(5);
        tabs.setViewPager(viewPager);
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

    public ViewPager getViewPager(){
        return this.viewPager;
    }
}
