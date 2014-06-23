package com.example.specialsapp.app.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.specialsapp.app.AlertDialogs.CustomAlertDialog;
import com.example.specialsapp.app.Cards.SpecialCard;
import com.example.specialsapp.app.Fragments.DealerSpecialsFragment;
import com.example.specialsapp.app.Fragments.NearbyDealersFragment;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.Models.Special;
import com.example.specialsapp.app.R;
import com.example.specialsapp.app.Rest.SpecialsRestClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Hosts all fragments that display dealers and their specials
 */
public class HomeActivity extends FragmentActivity {

    private Menu menu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CardListView cardListView;
    private String[] mMenuList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<Dealer> dealers;
    private ArrayList<Special> specials;
    private ArrayList<Card> cards;
    private Special special;
    private Dealer dealer;
    private RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mMenuList = getResources().getStringArray(R.array.list_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        dealers = new ArrayList<Dealer>();
        specials = new ArrayList<Special>();
        cards = new ArrayList<Card>();
        special = new Special();
        params = new RequestParams();
        dealer = new Dealer();

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mMenuList));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        // Show NearbyDealersFragment
        DealerSpecialsFragment nearbyDealersFragment = new DealerSpecialsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer2, nearbyDealersFragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        // Check login status, change menu appropriately
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        boolean status = shared.getBoolean("stored", true);
        if (status) {
            menu.findItem(R.id.action_logout).setVisible(true);
            menu.findItem(R.id.action_login).setVisible(false);
        } else {
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_login).setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor edit = shared.edit();
            edit.putString("User", "");
            edit.putString("Password", "");
            edit.putBoolean("stored", false);
            edit.commit();
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_login).setVisible(true);
            new CustomAlertDialog(this, "Logout", "You have been logged out. You can no longer send contact info to dealers").show();
            return true;
        }
        if (id == android.R.id.home) {
            NearbyDealersFragment nearbyDealersFragment = new NearbyDealersFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer2, nearbyDealersFragment);
            fragmentTransaction.commit();
        }
        if (id == R.id.action_login) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Controls backstack for dealers/specials fragments
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

    }

    public ArrayList<Dealer> getDealers() {
        return dealers;
    }

    public void setDealers(ArrayList<Dealer> dealers) {
        this.dealers = dealers;
    }

    public void toggleDrawerOff() {
        mDrawerToggle.setDrawerIndicatorEnabled(false);
    }

    public void toggleDrawerOn() {
        mDrawerToggle.setDrawerIndicatorEnabled(true);
    }

    public ArrayList<Card> getDealerSpecials(String dealer, View homeView) throws JSONException {

        final View view = homeView;
        params.put("dealer", dealer);

        SpecialsRestClient.get("special", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray request) {
                for (int i = 0; i < request.length(); i++) {
                    try {
                        JSONObject content = ((JSONObject) request.get(i));
                        special.setTitle(content.getString("title"));
                        special.setDealer(content.getString("dealer"));
                        special.setDescription(content.getString("description"));
                        special.setType(content.getString("type"));
                        specials.add(special);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                createSpecials(specials);
                CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(HomeActivity.this, cards);

                cardListView = (CardListView) view.findViewById(R.id.myList1);
                if (cardListView != null) {
                    cardListView.setAdapter(mCardArrayAdapter);
                }
            }
        });
        return cards;
    }

    public ArrayList<Dealer> getDealers(Double lng, Double lat, View view) throws JSONException {

        String latt = String.valueOf(lat);
        String longg = String.valueOf(lng);

        final View homeView = view;
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("lng", longg);
        param.put("lat", latt);
        params = new RequestParams(param);

        SpecialsRestClient.get("dealers", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray request) {
                for (int i = 0; i < request.length(); i++) {
                    try {
                        JSONObject content = (JSONObject) ((JSONObject) request.get(i)).get("content");
                        JSONObject distance = (JSONObject) ((JSONObject) request.get(i)).get("distance");
                        dealer.setName(content.getString("name"));
                        dealer.setCity(content.getString("city"));
                        dealer.setState(content.getString("state"));
                        //dealer.setNumSpecials(content.getInt("numSpecials"));
                        //dealer.setDistanceFrom(distance.getDouble());
                        dealers.add(i, dealer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try{
                    getDealerSpecials(dealers.get(0).getName(), homeView);
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });
        return dealers;
    }

    public ArrayList<Card> createSpecials(ArrayList<Special> specials) {
        for (int i = 0; i < specials.size(); i++) {
            SpecialCard card = new SpecialCard(HomeActivity.this, R.layout.special_card);
            card.setTitle(specials.get(i).getTitle());
            card.setDescription(specials.get(i).getDescription());
            card.setDealer(specials.get(i).getDealer());
            card.setSpecialType(specials.get(i).getType());
            cards.add(card);
        }
        return cards;
    }
}
