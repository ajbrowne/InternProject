package com.example.specialsapp.app.Activities;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.specialsapp.app.Adapters.TabsPagerAdapter;
import com.example.specialsapp.app.AlertDialogs.CustomAlertDialog;
import com.example.specialsapp.app.Cards.SpecialCard;
import com.example.specialsapp.app.Fragments.NearbyDealersFragment;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.Models.Special;
import com.example.specialsapp.app.R;
import com.example.specialsapp.app.Rest.SpecialsRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;

/**
 * Hosts all fragments that display dealers and their specials
 */
public class HomeActivity extends FragmentActivity implements AbsListView.OnScrollListener {

    private Menu menu;
    private CardListView cardListView;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;

    private String[] tabs = {"Nearby", "Search", "Test"};
    private int currIndex, returnSize;

    private PullToRefreshLayout mPullToRefreshLayout;
    private ArrayList<Dealer> dealers;
    private ArrayList<Special> specialList;
    private ArrayList<Card> cardList;
    private RequestParams params;
    private CardArrayAdapter mCardArrayAdapter;

    // ActionBar tab implementation


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

        if (loadMore && currIndex < returnSize-1){
            System.out.println(currIndex + " " + returnSize);
            createSpecials(currIndex, specialList, cardList);
            mCardArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ActionBar actionBar = getActionBar();

        dealers = new ArrayList<Dealer>();
        specialList = new ArrayList<Special>();
        cardList = new ArrayList<Card>();
        params = new RequestParams();

        viewPager = (ViewPager) findViewById(R.id.fragmentContainer2);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
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

        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =  (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("i.e. 2014 Chevy Silverado");
        setSearchTextColour(searchView);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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

    private void setSearchTextColour(SearchView searchView) {
        try{
            Field searchClose = SearchView.class.getDeclaredField("mCloseButton");
            searchClose.setAccessible(true);
            ImageView closeBtn = (ImageView) searchClose.get(searchView);
            closeBtn.setImageResource(R.drawable.ic_action_cancel);

            Field searchButton = SearchView.class.getDeclaredField("mSearchButton");
            searchButton.setAccessible(true);
            ImageView sButton = (ImageView) searchButton.get(searchView);
            sButton.setImageResource(R.drawable.ic_action_search);



//            Field searchHint = SearchView.class.getDeclaredField("mSearchHintIcon");
//            searchHint.setAccessible(true);
//            ImageView hintBtn = (ImageView) searchView.findViewById();
//            hintBtn.setImageResource(R.drawable.ic_action_cancel);

        } catch (NoSuchFieldException e){
            e.printStackTrace();
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }

        int searchHintId = searchView.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView hintBtn = (ImageView) searchView.findViewById(searchHintId);
        hintBtn.setImageResource(R.drawable.ic_action_cancel);

        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = (EditText) searchView.findViewById(searchPlateId);
        searchPlate.setTextSize(16);
        searchPlate.setTextColor(getResources().getColor(R.color.white));
        searchPlate.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchPlate.setHintTextColor(getResources().getColor(R.color.hint));
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

    public ArrayList<Dealer> getDealers() {
        return dealers;
    }

    public void setDealers(ArrayList<Dealer> dealers) {
        this.dealers = dealers;
    }

    /**
     * Finds nearest dealers (x determined in api) to given lat and long
     *
     * @param lng  - longitude
     * @param lat  - latitude
     * @param view - view passed to getDealerSpecials
     * @return ArrayList of dealers found
     * @throws JSONException
     */
    public void getDealerSpecials(Double lng, Double lat, View view, PullToRefreshLayout pullToRefreshLayout) throws JSONException {

        String latt = String.valueOf(lat);
        String longg = String.valueOf(lng);

        mPullToRefreshLayout = pullToRefreshLayout;
        final View homeView = view;
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("lng", longg);
        param.put("lat", latt);
        params = new RequestParams(param);

        SpecialsRestClient.get("special", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray request) {
                ArrayList<Special> specials = new ArrayList<Special>();
                try {
                    JSONObject dealer = (JSONObject) request.get(0);
                    JSONArray specialArray = (JSONArray) dealer.get("specials");
                    for (int i = 0; i < specialArray.length(); i++) {
                        Special special = new Special();
                        JSONObject spec = (JSONObject) specialArray.get(i);
                        special.setTitle(spec.getString("title"));
                        special.setDealer(dealer.getString("dealerName"));
                        special.setDescription(spec.getString("description"));
                        special.setType(spec.getString("type"));
                        specials.add(special);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                returnSize = specials.size();
                specialList = specials;
                ArrayList<Card> cards = new ArrayList<Card>();
                cards = createSpecials(0, specials, cards);
                cardList = cards;
                mCardArrayAdapter = new CardArrayAdapter(HomeActivity.this, cards);

                cardListView = (CardListView) homeView.findViewById(R.id.myList1);
                if (cardListView != null) {
                    cardListView.setAdapter(mCardArrayAdapter);
                    cardListView.setOnScrollListener(HomeActivity.this);
                }

                if(mPullToRefreshLayout != null){
                    mPullToRefreshLayout.setRefreshComplete();
                }
            }

        });
    }

    /**
     * Creates cards for a given ArrayList of specials
     *
     * @param specials - Specials that will have cards created for them
     * @return Arraylist of created cards
     */
    public ArrayList<Card> createSpecials(int index, ArrayList<Special> specials, ArrayList<Card> cards) {
        for (int i = index; i < index+10 && i < returnSize; i++) {
            SpecialCard card = new SpecialCard(HomeActivity.this, R.layout.special_card);
            card.setTitle(specials.get(i).getTitle());
            card.setDescription(specials.get(i).getDescription());
            card.setDealer(specials.get(i).getDealer());
            card.setSpecialType(specials.get(i).getType());
            cards.add(card);
            currIndex = i;
        }
        return cards;
    }
}
