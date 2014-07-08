package com.example.specialsapp.app.Fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.example.specialsapp.app.Activities.HomeActivity;
import com.example.specialsapp.app.Activities.SearchActivity;
import com.example.specialsapp.app.Activities.SpecialDetail;
import com.example.specialsapp.app.Cards.VehicleCard;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Special;
import com.example.specialsapp.app.Models.Vehicle;
import com.example.specialsapp.app.R;
import com.example.specialsapp.app.Rest.SpecialsRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 *
 * Currently the home view that displays all specials from the dealer
 * that is closest to your current location.
 *
 * Created by brownea on 6/12/14.
 */
public class DealerSpecialsFragment extends Fragment implements OnRefreshListener, AbsListView.OnScrollListener{

    private View homeView;
    private PullToRefreshLayout mPullToRefreshLayout;
    private CardArrayAdapter mCardArrayAdapter;
    private ArrayList<Vehicle> newVehicles;
    private ArrayList<Card> cards;
    private int currIndex, returnSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_dealer_specials, container, false);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle("Specials");

        setHasOptionsMenu(true);

        mPullToRefreshLayout = (PullToRefreshLayout)homeView.findViewById(R.id.carddemo_extra_ptr_layout);
        ActionBarPullToRefresh.from(this.getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        // Get location upon opening app, returning to Dealers
        final GPS gps = new GPS(getActivity());
        Double latitude = gps.getLatitude();
        Double longitude = gps.getLongitude();

        try {
            getDealerSpecials(longitude, latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return homeView;
    }


    @Override
    public void onRefreshStarted(View view) {
        final GPS gps = new GPS(getActivity());
        Double latitude = gps.getLatitude();
        Double longitude = gps.getLongitude();
        // Call to retrieve specials to display
        try {
           getDealerSpecials(longitude, latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds nearest dealers (x determined in api) to given lat and long
     *
     * @param lng  - longitude
     * @param lat  - latitude
     * @throws JSONException
     */
    public void getDealerSpecials(Double lng, Double lat) throws JSONException {

        String latt = String.valueOf(lat);
        String longg = String.valueOf(lng);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("lng", longg);
        param.put("lat", latt);
        param.put("make", "");
        param.put("extra", "0");
        RequestParams params = new RequestParams(param);

        System.out.println("DSGDGDHHDAHAHADHAH" + params);
        vehicleAsync(params);
    }

    private void vehicleAsync(RequestParams parameters) {
        SpecialsRestClient.get("vehicle", parameters, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray request) {
                ArrayList<Special> specials = new ArrayList<Special>();
                ArrayList<Vehicle> newVehicles = new ArrayList<Vehicle>();
                Vehicle newVehicle = new Vehicle();
                try {
                    JSONObject dealer = (JSONObject) request.get(0);
                    JSONArray vehicleArray = (JSONArray) dealer.get("vehicles");
                    for (int i = 0; i < vehicleArray.length(); i++) {
                        JSONObject vehicle = (JSONObject) vehicleArray.get(i);
                        String id = vehicle.getString("id");
                        JSONArray specialArray = (JSONArray) dealer.get("specials");
                        for (int j = 0; j < specialArray.length(); j++) {
                            JSONObject special = (JSONObject) specialArray.get(j);
                            JSONArray ids = (JSONArray) special.get("vehicleId");
                            for (int k = 0; k < ids.length(); k++) {
                                if (ids.get(k).equals(id)) {
                                    Special specialObject = new Special();
                                    specialObject.setTitle(special.getString("title"));
                                    specialObject.setAmount(special.getString("amount"));
                                    Vehicle vehicleObject = new Vehicle();
                                    vehicleObject.setMake(vehicle.getString("make"));
                                    vehicleObject.setModel(vehicle.getString("model"));
                                    vehicleObject.setYear(vehicle.getString("year"));
                                    vehicleObject.setPrice(vehicle.getString("price"));
                                    vehicleObject.setUrl(vehicle.getString("urlImage"));
                                    vehicleObject.setVehicleType(vehicle.getString("type"));
                                    vehicleObject.setDealer(dealer.getString("dealerName"));
                                    ArrayList<Special> specs = vehicleObject.getSpecials();
                                    specs.add(specialObject);
                                    vehicleObject.setSpecials(specs);
                                    newVehicles.add(vehicleObject);
                                }
                            }
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(specials.size());
                System.out.println(newVehicles.size());
                addCards(newVehicles);
            }
        });
    }

    private void addCards(ArrayList<Vehicle> newVehicles) {
        returnSize = newVehicles.size();
        cards = new ArrayList<Card>();
        cards = createSpecials(0, newVehicles, cards);
        mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        CardListView cardListView = (CardListView) homeView.findViewById(R.id.myList1);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
            cardListView.setOnScrollListener(this);
        }

        if (mPullToRefreshLayout != null) {
            mPullToRefreshLayout.setRefreshComplete();
        }
    }

    /**
     * Creates cards for a given ArrayList of specials
     *
     * @return Arraylist of created cards
     */
    public ArrayList<Card> createSpecials(int index, ArrayList<Vehicle> newVehicles, ArrayList<Card> cards) {
        for (int i = index; i < index+10 && i < returnSize; i++) {
            VehicleCard card = new VehicleCard(getActivity(), R.layout.vehicle_card);
            Vehicle vehicle = newVehicles.get(i);
            card.setTitle(vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel());
            // Needs to be gas mileage!!!
            //card.setDescription(special.getDescription());

            card.setDealer(vehicle.getDealer());
            card.setVehicleType(vehicle.getVehicleType());
            card.setUrl(vehicle.getUrl());
            int old = Integer.parseInt(vehicle.getPrice());
            int newPrice = old;
            for (Special special : vehicle.getSpecials()) {
                newPrice -= Integer.parseInt(special.getAmount());
            }
            card.setNewPrice(((HomeActivity)getActivity()).insertCommas(String.valueOf(newPrice)));
            card.setOldPrice(((HomeActivity)getActivity()).insertCommas(String.valueOf(old)));

            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Intent intent = new Intent(getActivity(), SpecialDetail.class);
                    VehicleCard temp = (VehicleCard) card;
                    intent.putExtra("title",  temp.getTitle());
                    //intent.putExtra("description", temp.getDescription());
                    intent.putExtra("oldP", temp.getOldPrice());
                    intent.putExtra("newP", temp.getNewPrice());
                    intent.putExtra("imageUrl", temp.getUrl());
                    getActivity().startActivity(intent);

                }
            });

            cards.add(card);
            currIndex = i;
        }
        currIndex++;
        return cards;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

        if (loadMore && currIndex < returnSize-1){
            createSpecials(currIndex, newVehicles, cards);
            mCardArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.search){
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra("tab", 1);
            startActivity(intent);
        }
        return true;
    }
}
