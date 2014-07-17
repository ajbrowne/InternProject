package com.example.specialsapp.app.Fragments;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.specialsapp.app.Activities.SpecialDetail;
import com.example.specialsapp.app.Cards.VehicleCard;
import com.example.specialsapp.app.Models.Special;
import com.example.specialsapp.app.Models.Vehicle;
import com.example.specialsapp.app.R;
import com.example.specialsapp.app.Rest.AppController;

import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseSearchFragment extends Fragment implements AbsListView.OnScrollListener {

    private static final double defaultLocation = -1000.0;
    private static final String baseUrl = "http://192.168.170.100:8080/v1/specials/vehicle?";
    private View baseView;
    private CardArrayAdapter mCardArrayAdapter;
    private ArrayList<Vehicle> newVehicles;
    private ArrayList<Card> cards;
    private int currIndex, returnSize;
    private PullToRefreshLayout mPullToRefreshLayout;
    private boolean isSearch;

    public BaseSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        baseView = inflater.inflate(R.layout.fragment_dealer_specials, container, false);

        return baseView;
    }

    public void vehicleAsync(HashMap<String, String> parameters, View view, PullToRefreshLayout pullToRefreshLayout, boolean isSearch) {
        this.isSearch = isSearch;
        AbstractHttpClient client = new DefaultHttpClient();
        RequestQueue queue = Volley.newRequestQueue(getActivity(), new HttpClientStack(client));
        baseView = view;
        mPullToRefreshLayout = pullToRefreshLayout;
        JsonArrayRequest searchRequest;

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        String url = generateUrl(parameters);
        System.out.println("URL: " + url);
        Cache.Entry entry = cache.get(url);
        makeAsync(isSearch, queue, url, entry);
    }

    private void makeAsync(boolean isSearch, RequestQueue queue, String url, Cache.Entry entry) {
        JsonArrayRequest searchRequest;
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                JSONArray cached = new JSONArray(data);
                System.out.println("cached base" + isSearch);
                carSearch(cached);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            if (isSearch) {
                searchRequest = new JsonArrayRequest(url, new ResponseListener(), new ErrorListener());
            } else {
                searchRequest = new JsonArrayRequest(url, new ResponseListener(), new ErrorListener());
            }
            queue.add(searchRequest);
        }
    }

    public void createVehicle(JSONObject dealer, JSONObject vehicle, String id, JSONObject special, JSONArray ids) throws JSONException {
        for (int k = 0; k < ids.length(); k++) {
            if (ids.get(k).equals(id)) {
                Special specialObject = new Special();
                specialObject.setTitle(special.getString("title"));
                specialObject.setAmount(special.getString("amount"));

                ArrayList<Special> specs = new ArrayList<Special>();
                specs.add(specialObject);
                String newPrice = String.valueOf(Integer.parseInt(vehicle.getString("price")) - Integer.parseInt(special.getString("amount")));

                checkVehicle(dealer, vehicle, special, specialObject, specs, newPrice);
            }
        }
    }

    private void checkVehicle(JSONObject dealer, JSONObject vehicle, JSONObject special, Special specialObject, ArrayList<Special> specs, String newPrice) throws JSONException {
        boolean duplicate = false;
        for (Vehicle added : newVehicles) {
            if (added.getId().equals(vehicle.getString("id"))) {
                ArrayList<Special> combine = added.getSpecials();
                combine.add(specialObject);
                added.setSpecials(combine);
                added.setDiscount(String.valueOf(Integer.parseInt(added.getDiscount()) + Integer.parseInt(special.getString("amount"))));
                duplicate = true;
            }
        }
        if (!duplicate) {
            Vehicle vehicleObject = new Vehicle(vehicle.getString("year"), vehicle.getString("make"), vehicle.getString("model"),
                    vehicle.getString("type"), (JSONArray) vehicle.get("specs"), vehicle.getString("id"), dealer.getString("dealerName"),
                    specs, vehicle.getString("year") + " " + vehicle.getString("make") + " " + vehicle.getString("model"),
                    newPrice, vehicle.getString("price"), vehicle.getString("urlImage"), special.getString("amount"));
            newVehicles.add(vehicleObject);
        }
    }

    public void addCards(ArrayList<Vehicle> newVehicles) {
        returnSize = newVehicles.size();
        cards = new ArrayList<Card>();
        cards = createSpecials(0, newVehicles, cards);
        mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);
        CardListView cardListView = (CardListView) baseView.findViewById(R.id.myList1);
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
        for (int i = index; i < index + 10 && i < returnSize; i++) {
            VehicleCard card = new VehicleCard(getActivity(), R.layout.vehicle_card);
            final Vehicle vehicle = newVehicles.get(i);
            card.setTitle(vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel());
            try {
                card.setGasMileage((String) vehicle.getSpecs().get(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            card.setDealer(vehicle.getDealer());
            card.setVehicleType(vehicle.getVehicleType());
            card.setUrl(vehicle.getUrl());
            int newPrice = Integer.parseInt(vehicle.getOldPrice()) - Integer.parseInt(vehicle.getDiscount());
            card.setNewPrice(insertCommas(String.valueOf(newPrice)));
            card.setOldPrice(insertCommas(String.valueOf(vehicle.getOldPrice())));

            card.setOnClickListener(getCardOnClickListener(vehicle));

            cards.add(card);
            currIndex = i;
        }
        currIndex++;
        return cards;
    }

    public Card.OnCardClickListener getCardOnClickListener(final Vehicle vehicle) {
        return new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Intent intent = new Intent(getActivity(), SpecialDetail.class);
                VehicleCard temp = (VehicleCard) card;
                intent.putExtra("title", temp.getTitle());
                intent.putExtra("oldP", temp.getOldPrice());
                intent.putExtra("newP", temp.getNewPrice());
                intent.putExtra("imageUrl", temp.getUrl());
                intent.putExtra("year", vehicle.getYear());
                intent.putExtra("make", vehicle.getMake());
                intent.putExtra("model", vehicle.getModel());
                ArrayList<String> tempSpecs = new ArrayList<String>();
                for (int i = 0; i < vehicle.getSpecs().length(); i++) {
                    try {
                        tempSpecs.add(vehicle.getSpecs().get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                intent.putStringArrayListExtra("spec", tempSpecs);
                getActivity().startActivity(intent);

            }
        };
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

        if (loadMore && currIndex < returnSize - 1) {
            createSpecials(currIndex, newVehicles, cards);
            mCardArrayAdapter.notifyDataSetChanged();
        }
    }


    public double[] getLoc(String zip) {
        final Geocoder geocoder = new Geocoder(getActivity());
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

    private String insertCommas(String amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        Double number = Double.parseDouble(amount);
        return String.valueOf(formatter.format(number));
    }

    public void carSearch(JSONArray response) {
        newVehicles = new ArrayList<Vehicle>();
        ArrayList<Special> specials = new ArrayList<Special>();
        Vehicle newVehicle = new Vehicle();
        try {
            JSONObject dealer = (JSONObject) response.get(0);
            JSONArray vehicleArray = (JSONArray) dealer.get("vehicles");
            for (int i = 0; i < vehicleArray.length(); i++) {
                JSONObject vehicle = (JSONObject) vehicleArray.get(i);
                String id = vehicle.getString("id");
                JSONArray specialArray = (JSONArray) dealer.get("specials");
                for (int j = 0; j < specialArray.length(); j++) {
                    JSONObject special = (JSONObject) specialArray.get(j);
                    JSONArray ids = (JSONArray) special.get("vehicleId");
                    createVehicle(dealer, vehicle, id, special, ids);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        addCards(newVehicles);
    }

    private String generateUrl(HashMap<String, String> parameters) {
        String url = baseUrl + "lng=" + parameters.get("lng") + "&lat=" + parameters.get("lat") + "&make=" + parameters.get("make") + "&extra=" + parameters.get("extra");
        if (isSearch) {
            url = url + "&model=" + parameters.get("model") + "&type=" + parameters.get("type") + "&max=" + parameters.get("max");
        }
        return url;
    }

    private class ResponseListener implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray response) {
            if(response.length() == 0){
                TextView result = (TextView)baseView.findViewById(R.id.third_result);
                result.setVisibility(View.VISIBLE);
            }else {
                carSearch(response);
            }
        }

    }

    private class ErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("error", "Http request failed");
        }
    }

}
