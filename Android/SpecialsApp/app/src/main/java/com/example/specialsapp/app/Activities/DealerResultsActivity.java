package com.example.specialsapp.app.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.specialsapp.app.Cards.DealerCard;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Dealer;
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

public class DealerResultsActivity extends BaseActivity {


    private TextView mResultsNone;
    private double lat;
    private double longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_results);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Dealer Results");
        mResultsNone = (TextView) findViewById(R.id.second_result);

        GPS gps = new GPS(this);
        lat = gps.getLatitude();
        longi = gps.getLongitude();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("lng", String.valueOf(longi));
        param.put("lat", String.valueOf(lat));
        param.put("make", getIntent().getStringExtra("make"));
        param.put("extra", "1");
        RequestParams params = new RequestParams(param);
        System.out.println(params);
        SpecialsRestClient.get("dealers", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray request) {
                ArrayList<Dealer> dealers = new ArrayList<Dealer>();
                try {
                    for (int i = 0; i < request.length(); i++) {
                        JSONObject dealerObject = (JSONObject) request.get(i);
                        Dealer dealer = new Dealer();
                        dealer.setCity(dealerObject.get("city").toString());
                        dealer.setState(dealerObject.get("state").toString());
                        dealer.setName(dealerObject.get("name").toString());
                        JSONObject loc = (JSONObject) dealerObject.get("loc");
                        JSONArray coords = (JSONArray) loc.get("coordinates");
                        dealer.setLongitude(coords.getDouble(1));
                        dealer.setLatitude(coords.getDouble(0));
                        dealer.setNumSpecials(dealerObject.getInt("numSpecials"));
                        dealers.add(dealer);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(dealers.size() == 0){
                    mResultsNone.setVisibility(View.VISIBLE);
                }
                addCards(dealers);
            }

        });

    }

    private void addCards(ArrayList<Dealer> dealers) {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards = createDealers(dealers, cards);

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);

        CardListView cardListView = (CardListView) findViewById(R.id.myList3);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
        }

    }

    public ArrayList<Card> createDealers(ArrayList<Dealer> dealers, ArrayList<Card> cards) {
        for (Dealer dealer : dealers) {
            DealerCard card = new DealerCard(this, R.layout.dealer_card);
            card.setDealer(dealer.getName());
            card.setCityState(dealer.getCity() + ", " + dealer.getState());
            Double distance = distance(dealer.getLatitude(), dealer.getLongitude(), lat, longi);
            distance = (double)Math.round(distance *10)/10;
            card.setDistance(String.valueOf(distance) + " mi");
            card.setNumSpecials(String.valueOf(dealer.getNumSpecials()) + " deals currently running");
            cards.add(card);
        }
        return cards;
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

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
