package com.example.specialsapp.app.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.specialsapp.app.Rest.SpecialsRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
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


    private static final String baseUrl = "http://192.168.170.93:8080/v1/specials/dealers?";
    private TextView mResultsNone;
    private double lat;
    private double longi;
    private RequestQueue queue;
    private JsonArrayRequest searchRequest;
    private AbstractHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_results);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Dealer Results");

        mResultsNone = (TextView) findViewById(R.id.second_result);
        client = new DefaultHttpClient();
        queue = Volley.newRequestQueue(this, new HttpClientStack(client));

        GPS gps = new GPS(this);
        lat = gps.getLatitude();
        longi = gps.getLongitude();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("lng", String.valueOf(longi));
        param.put("lat", String.valueOf(lat));
        param.put("make", getIntent().getStringExtra("make"));
        param.put("extra", "1");

        String url = generateUrl(param);
        searchRequest = new JsonArrayRequest(url, new ResponseListener(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        queue.add(searchRequest);

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
            DealerCard card = new DealerCard(this, R.layout.dealer_card, dealer.getLatitude(), dealer.getLongitude());
            card.setDealer(dealer.getName());
            card.setCityState(dealer.getCity() + ", " + dealer.getState());
            Double distance = distance(dealer.getLatitude(), dealer.getLongitude(), lat, longi);
            distance = (double) Math.round(distance * 10) / 10;
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

    private String generateUrl(HashMap<String, String> parameters) {
        String url = baseUrl + "lng=" + parameters.get("lng") + "&lat=" + parameters.get("lat") + "&make=" + parameters.get("make") + "&extra=" + parameters.get("extra");
        return url;
    }

    private class ResponseListener implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray response) {
            ArrayList<Dealer> dealers = new ArrayList<Dealer>();
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject dealerObject = (JSONObject) response.get(i);
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
            if (dealers.size() == 0) {
                mResultsNone.setVisibility(View.VISIBLE);
            }
            addCards(dealers);
        }

    }
}
