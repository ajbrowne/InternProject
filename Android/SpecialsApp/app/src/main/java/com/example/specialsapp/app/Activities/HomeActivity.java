package com.example.specialsapp.app.Activities;

import android.content.Intent;
import android.graphics.Typeface;
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
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/roboto-light.ttf");
        tabs.setTypeface(typeFace, android.R.style.TextAppearance_DeviceDefault);
        viewPager = (ViewPager) findViewById(R.id.fragmentContainer2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        HomePagerAdapter mAdapter = new HomePagerAdapter(fragmentManager);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(3);
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
        int id = item.getItemId();
        if (id == R.id.search) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("tabIndex", viewPager.getCurrentItem());
            System.out.println("CURRENT ITEM: " + viewPager.getCurrentItem());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Dealer> getDealers() {
        return dealers;
    }

    public void setDealers(ArrayList<Dealer> dealers) {
        this.dealers = dealers;
    }

    public ViewPager getViewPager() {
        return this.viewPager;
    }
}
