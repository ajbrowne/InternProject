package com.example.specialsapp.app.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.specialsapp.app.AlertDialogs.CustomAlertDialog;
import com.example.specialsapp.app.Cards.SpecialCard;
import com.example.specialsapp.app.Fragments.SpecialDetailFragment;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Special;
import com.example.specialsapp.app.R;
import com.example.specialsapp.app.Rest.SpecialsRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

public class VehicleResultsActivity extends FragmentActivity {

    private String[] params = new String[5];
    private RequestParams parameters;
    private CardArrayAdapter mCardArrayAdapter;
    private ArrayList<Card> cards;
    private CardListView cardListView;
    private String zip;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_results);

        zip = getIntent().getStringExtra("zip");
        params = getIntent().getStringArrayExtra("params");

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Vehicle Results");

        search();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vehicle_results, menu);
        return true;
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
            onBackPressed();
            return true;
        }
        if (id == R.id.action_login) {
            Intent intent = new Intent(VehicleResultsActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void search() {
        final GPS gps = new GPS(this);
        Double latitiude = gps.getLatitude();
        Double longitude = gps.getLongitude();
        String latt = String.valueOf(latitiude);
        String longi = String.valueOf(longitude);

        HashMap<String, String> param = new HashMap<String, String>();
        if (zip.compareTo("nope") == 0) {
            param.put("lng", latt);
            param.put("lat", longi);
        } else {
            double[] location = getLoc(zip);
            if (location[0] != -1000) {
                latt = String.valueOf(location[0]);
                longi = String.valueOf(location[1]);
                param.put("lng", longi);
                param.put("lat", latt);
            }

        }

        param.put("make", params[0]);
        param.put("model", params[1]);
        param.put("type", params[2]);
        param.put("max", params[3]);
        parameters = new RequestParams(param);
        System.out.println(parameters);
        SpecialsRestClient.get("vehicle", parameters, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray request) {
                ArrayList<Special> specials = new ArrayList<Special>();
                try {
                    JSONObject dealer = (JSONObject) request.get(0);
                    JSONArray specialArray = (JSONArray) dealer.get("specials");
                    for (int i = 0; i < specialArray.length(); i++) {
                        Special special = new Special();
                        JSONObject spec = (JSONObject) specialArray.get(i);
                        JSONArray vehicles = (JSONArray) spec.get(("vehicleId"));
                        if (vehicles.length() == 1) {
                            JSONArray vehicles2 = (JSONArray) dealer.get("vehicles");
                            for (int j = 0; j < vehicles2.length(); j++) {
                                JSONObject vehicle = (JSONObject) vehicles2.get(j);
                                JSONArray ids = (JSONArray) spec.get("vehicleId");
                                if (vehicle.getString("id").compareTo((String) ids.get(0)) == 0) {
                                    special.setPrice(vehicle.getInt("price"));
                                    special.setAmount(spec.getString("amount"));
                                }
                            }
                        } else {
                            special.setAmount("Multiple Vehicles");
                        }
                        special.setTitle(spec.getString("title"));
                        special.setDealer(dealer.getString("dealerName"));
                        special.setDescription(spec.getString("description"));
                        special.setType(spec.getString("type"));
                        specials.add(special);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cards = new ArrayList<Card>();
                cards = createSpecials(0, specials, cards);
                mCardArrayAdapter = new CardArrayAdapter(VehicleResultsActivity.this, cards);

                cardListView = (CardListView)findViewById(R.id.myList2);
                if (cardListView != null) {
                    cardListView.setAdapter(mCardArrayAdapter);
                    //cardListView.setOnScrollListener(VehicleSearchFragment.this);
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
        for (int i = index; i < specials.size(); i++) {
            SpecialCard card = new SpecialCard(this, R.layout.special_card);
            card.setTitle(specials.get(i).getTitle());
            card.setDescription(specials.get(i).getDescription());
            card.setDealer(specials.get(i).getDealer());
            card.setSpecialType(specials.get(i).getType());
            if (specials.get(i).getPrice() != -1000){
                int old = Integer.parseInt(specials.get(i).getAmount());
                card.setNewPrice(String.valueOf(specials.get(i).getPrice() - old));
                card.setOldPrice(String.valueOf(specials.get(i).getPrice()));
            }
            else{
                card.setOldPrice(specials.get(i).getAmount());
            }

            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    SpecialDetailFragment specialDetailFragment = new SpecialDetailFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragmentContainer2, specialDetailFragment);
                    ft.commit();
                }
            });

            cards.add(card);
        }
        return cards;
    }

    public double[] getLoc(String zip){
        final Geocoder geocoder = new Geocoder(this);
        double [] location = {-1000, -1000};
        try{
            List<Address> addresses = geocoder.getFromLocationName(zip, 1);
            if (addresses != null && !addresses.isEmpty()){
                Address address = addresses.get(0);
                location[0] = address.getLatitude();
                location[1] = address.getLongitude();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return location;
    }

}
