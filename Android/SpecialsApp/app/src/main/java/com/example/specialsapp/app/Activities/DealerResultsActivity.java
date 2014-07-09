package com.example.specialsapp.app.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

public class DealerResultsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_results);

        GPS gps = new GPS(this);
        final Double lat = gps.getLatitude();
        final Double longi = gps.getLongitude();

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
                        JSONObject outer = (JSONObject) request.get(i);
                        JSONObject dealerObject = (JSONObject) outer.get("content");
                        Dealer dealer = new Dealer();
                        dealer.setCity(dealerObject.get("city").toString());
                        dealer.setState(dealerObject.get("state").toString());
                        dealer.setName(dealerObject.get("name").toString());
                        dealers.add(dealer);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
            cards.add(card);
        }
        return cards;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dealer_results, menu);
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
        return super.onOptionsItemSelected(item);
    }
}
