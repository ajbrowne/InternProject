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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

public class VehicleResultsActivity extends BaseActivity {

    private static final double defaultLocation = -1000.0;
    private String[] params = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_results);


        String zip = getIntent().getStringExtra("zip");
        params = getIntent().getStringArrayExtra("params");

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Vehicle Results");

        search(zip);
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

    private void search(String zip) {
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
        param.put("extra", params[4]);
        RequestParams parameters = new RequestParams(param);
        System.out.println("PARAMS: " + parameters);
        vehicleAsync(parameters);
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
                    JSONArray vehicleArray = (JSONArray) request.get(1);
                    for (int i = 0; i < vehicleArray.length(); i++) {
                        JSONObject vehicle = (JSONObject) vehicleArray.get(i);
                        String id = vehicle.getString("id");
                        JSONArray specialArray = (JSONArray) dealer.get("specials");
                        for (int j = 0; j < specialArray.length(); j++) {
                            JSONObject special = (JSONObject) specialArray.get(j);
                            JSONArray ids = (JSONArray) special.get("vehicleId");
                            for (int k = 0; k < ids.length(); k++) {
                                if (ids.get(i).equals(id)) {
                                    Special specialObject = new Special();
                                    specialObject.setTitle(special.getString("title"));
                                    Vehicle vehicleObject = new Vehicle();
                                    vehicleObject.setMake(vehicle.getString("make"));
                                    vehicleObject.setModel(vehicle.getString("model"));
                                    vehicleObject.setYear(vehicle.getString("year"));
                                    vehicleObject.setPrice(vehicle.getString("price"));
                                    vehicleObject.setUrl(vehicle.getString("url"));
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
        CardArrayAdapter mCardArrayAdapter;
        ArrayList<Card> cards;
        CardListView cardListView;
        cards = new ArrayList<Card>();
        cards = createSpecials(0, cards, newVehicles);
        mCardArrayAdapter = new CardArrayAdapter(this, cards);

        cardListView = (CardListView) findViewById(R.id.myList2);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
        }
    }

    /**
     * Creates cards for a given ArrayList of specials
     *
     * @return Arraylist of created cards
     */
    private ArrayList<Card> createSpecials(int index, ArrayList<Card> cards, final ArrayList<Vehicle> newVehicles) {
        for (int i = index; i < newVehicles.size(); i++) {
            VehicleCard card = new VehicleCard(this, R.layout.vehicle_card);
            final Vehicle vehicle = newVehicles.get(i);
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
            card.setNewPrice(insertCommas(String.valueOf(newPrice)));
            card.setOldPrice(insertCommas(String.valueOf(old)));

            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Intent intent = new Intent(VehicleResultsActivity.this, SpecialDetail.class);
                    VehicleCard temp = (VehicleCard) card;
                    intent.putExtra("title", temp.getTitle());
                    //intent.putExtra("description", temp.getDescription());
                    intent.putExtra("oldP", temp.getOldPrice());
                    intent.putExtra("newP", temp.getNewPrice());
                    intent.putExtra("imageUrl", temp.getUrl());
                    intent.putExtra("year", vehicle.getYear());
                    intent.putExtra("make", vehicle.getMake());
                    intent.putExtra("model", vehicle.getModel());
                    startActivity(intent);
                }
            });

            cards.add(card);
        }
        return cards;
    }

    private double[] getLoc(String zip) {
        final Geocoder geocoder = new Geocoder(this);
        double[] location = {defaultLocation, defaultLocation};
        try {
            List<Address> addresses = geocoder.getFromLocationName(zip, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                location[0] = address.getLatitude();
                location[1] = address.getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    public String insertCommas(String amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        Double number = Double.parseDouble(amount);
        return String.valueOf(formatter.format(number));
    }
}
