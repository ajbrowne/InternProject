package com.example.specialsapp.app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.specialsapp.app.Cards.DealerCard;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.R;

import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

public class DealerResultsActivity extends BaseActivity {


    private static final String baseUrl = "http://192.168.170.100:8080/v1/specials/dealers?";
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
        AbstractHttpClient client = new DefaultHttpClient();
        RequestQueue queue = Volley.newRequestQueue(this, new HttpClientStack(client));

        GPS gps = new GPS(this);
        lat = gps.getLatitude();
        longi = gps.getLongitude();

        HashMap<String, String> param = createParams();

        String url = generateUrl(param);
        JsonArrayRequest searchRequest = new JsonArrayRequest(url, new ResponseListener(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Error", error.toString());
            }
        });
        queue.add(searchRequest);

    }

    private HashMap<String, String> createParams() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("lng", String.valueOf(longi));
        param.put("lat", String.valueOf(lat));
        if (getIntent().getStringExtra("make").equals("All")) {
            param.put("extra", "0");
        } else {
            param.put("extra", "1");
            param.put("make", getIntent().getStringExtra("make"));
        }
        System.out.println(param);
        return param;
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

            Double distance = distance(dealer.getLatitude(), dealer.getLongitude(), lat, longi);
            distance = (double) Math.round(distance * 10) / 10;

            DealerCard card = new DealerCard(this, R.layout.dealer_card, dealer.getLatitude(), dealer.getLongitude());
            card.setDealer(dealer.getName());
            card.setCityState(dealer.getCity() + ", " + dealer.getState());
            card.setDistance(String.valueOf(distance) + " mi");
            card.setNumSpecials(String.valueOf(dealer.getNumSpecials()) + " deals currently running");
            card.setOnClickListener(getOnClickListener(dealer, distance));
            cards.add(card);
        }
        return cards;
    }

    private Card.OnCardClickListener getOnClickListener(final Dealer dealer, final Double distance) {
        return new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Intent intent = new Intent(DealerResultsActivity.this, DealerDetail.class);
                intent.putExtra("name", dealer.getName());
                intent.putExtra("lat", dealer.getLatitude());
                intent.putExtra("long", dealer.getLongitude());
                intent.putExtra("dist", distance.toString());
                startActivity(intent);
            }
        };
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

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
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

    private String generateUrl(HashMap<String, String> parameters) {
        return baseUrl + "lng=" + parameters.get("lng") + "&lat=" + parameters.get("lat") +
                "&make=" + parameters.get("make") + "&extra=" + parameters.get("extra");
    }

    private class ResponseListener implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray response) {
            ArrayList<Dealer> dealers = new ArrayList<Dealer>();
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject dealerObject = (JSONObject) response.get(i);
                    JSONObject loc = (JSONObject) dealerObject.get("loc");
                    JSONArray coordinates = (JSONArray) loc.get("coordinates");

                    Dealer dealer = new Dealer(dealerObject.getString("name"),
                            dealerObject.get("city").toString(), dealerObject.get("state").toString(),
                            dealerObject.getInt("numSpecials"), coordinates.getDouble(0), coordinates.getDouble(1));
                    dealers.add(dealer);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (dealers.size() == 0) {
                mResultsNone.setVisibility(View.VISIBLE);
            }
            addCards(dealers);
        }

    }
}
