package com.example.specialsapp.app.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.specialsapp.app.Cards.SpecialCard;
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

public class VehicleResultsActivity extends BaseActivity {

    private String[] params = new String[5];
    private String zip;

    private static final double defaultLocation = -1000.0;

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void search() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final GPS gps = new GPS(this);
        Double latitude = gps.getLatitude();
        Double longitude = gps.getLongitude();
        String latt = String.valueOf(latitude);
        String longi = String.valueOf(longitude);

        HashMap<String, String> param = new HashMap<String, String>();
        if (sharedPreferences.getBoolean("use_location", false)) {
            param.put("lng", longi);
            param.put("lat", latt);
        } else {
            double[] location = getLoc(zip);
            System.out.println(location[0]);
            if (location[0] != defaultLocation) {
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
        RequestParams parameters = new RequestParams(param);
        System.out.println("PARAMS: " + parameters);
        vehicleAsync(parameters);
    }

    private void vehicleAsync(RequestParams parameters) {
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
                addCards(specials);
            }
        });
    }

    private void addCards(ArrayList<Special> specials) {
        CardArrayAdapter mCardArrayAdapter;
        ArrayList<Card> cards;
        CardListView cardListView;
        cards = new ArrayList<Card>();
        cards = createSpecials(0, specials, cards);
        mCardArrayAdapter = new CardArrayAdapter(this, cards);

        cardListView = (CardListView) findViewById(R.id.myList2);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
        }
    }

    /**
     * Creates cards for a given ArrayList of specials
     *
     * @param specials - Specials that will have cards created for them
     * @return Arraylist of created cards
     */
    private ArrayList<Card> createSpecials(int index, ArrayList<Special> specials, ArrayList<Card> cards) {
        for (int i = index; i < specials.size(); i++) {
            SpecialCard card = new SpecialCard(this, R.layout.special_card);
            Special special = specials.get(i);
            card.setTitle(special.getTitle());
            card.setDescription(special.getDescription());
            card.setDealer(special.getDealer());
            card.setSpecialType(special.getType());
            if (special.getPrice() != defaultLocation){
                int old = Integer.parseInt(specials.get(i).getAmount());
                card.setNewPrice(String.valueOf(special.getPrice() - old));
                card.setOldPrice(String.valueOf(special.getPrice()));
            }
            else{
                card.setOldPrice(special.getAmount());
            }

            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Intent intent = new Intent(VehicleResultsActivity.this, SpecialDetail.class);
                    SpecialCard temp = (SpecialCard) card;
                    intent.putExtra("title",  temp.getTitle());
                    intent.putExtra("description", temp.getDescription());
                    intent.putExtra("oldP", temp.getOldPrice());
                    intent.putExtra("newP", temp.getNewPrice());
                    startActivity(intent);
                }
            });

            cards.add(card);
        }
        return cards;
    }

    private double[] getLoc(String zip){
        final Geocoder geocoder = new Geocoder(this);
        double [] location = {defaultLocation, defaultLocation};
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
