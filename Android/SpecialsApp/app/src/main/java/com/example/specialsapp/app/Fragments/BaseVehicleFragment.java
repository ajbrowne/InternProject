package com.example.specialsapp.app.Fragments;

import android.content.Intent;
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
import com.example.specialsapp.app.Activities.VehicleDetail;
import com.example.specialsapp.app.Adapters.AssetsPropertyAdapter;
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

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;

/**
 * Base fragment for DealerSpecialsFragment and VehicleResultsFragment.
 * Hits /vehicle and either returns vehicles at nearby dealers or those
 * specified in search.
 */
public class BaseVehicleFragment extends Fragment implements AbsListView.OnScrollListener {

    private static String BASE_URL;
    private View baseView;
    private CardArrayAdapter mCardArrayAdapter;
    private ArrayList<Vehicle> newVehicles;
    private ArrayList<Card> cards;
    private int currIndex, returnSize;
    private PullToRefreshLayout mPullToRefreshLayout;
    private boolean isSearch;

    public BaseVehicleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        baseView = inflater.inflate(R.layout.fragment_dealer_specials, container, false);

        return baseView;
    }

    /**
     * Initializes async fields including caching.
     *
     * @param parameters          - parameters for search
     * @param view                - current view
     * @param pullToRefreshLayout - current PullToRefreshLayout
     * @param isSearch            - denotes whether from home view or search
     */
    public void vehicleAsync(HashMap<String, String> parameters, View view, PullToRefreshLayout pullToRefreshLayout, boolean isSearch) {
        // Get url from properties file
        AssetsPropertyAdapter assetsPropertyAdapter = new AssetsPropertyAdapter(getActivity());
        Properties properties = assetsPropertyAdapter.getProperties("specials.properties");
        BASE_URL = properties.getProperty("baseUrl") + properties.getProperty("vehicle");

        this.isSearch = isSearch;
        AbstractHttpClient client = new DefaultHttpClient();
        RequestQueue queue = Volley.newRequestQueue(getActivity(), new HttpClientStack(client));
        baseView = view;
        mPullToRefreshLayout = pullToRefreshLayout;

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        String url = generateUrl(parameters);
        System.out.println(url);
        Cache.Entry entry = cache.get(url);
        makeAsync(isSearch, queue, url, entry);
    }

    /**
     * Makes the actual call for vehicles, whether with cached or new results from the api
     *
     * @param isSearch - denotes whether from home view or search
     * @param queue    - queue of async calls being made
     * @param url      - url being sent to api
     * @param entry    - cached results if they exist
     */
    private void makeAsync(boolean isSearch, RequestQueue queue, String url, Cache.Entry entry) {
        JsonArrayRequest searchRequest;
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                JSONArray cached = new JSONArray(data);
                Log.d("BaseSearchFragment", "cached base" + isSearch);
                vehicleSearch(cached);
            } catch (UnsupportedEncodingException | JSONException e) {
                Log.d("BaseSearchFragment", "Asynchronous make search request error");
            }
        } else {
            searchRequest = new JsonArrayRequest(url, new ResponseListener(), new ErrorListener());
            queue.add(searchRequest);
        }
    }

    public void getVehicleInfo(JSONObject dealer, JSONObject vehicle, String id, JSONObject special, JSONArray ids) throws JSONException {
        for (int k = 0; k < ids.length(); k++) {
            if (ids.get(k).equals(id)) {
                Special specialObject = new Special();
                specialObject.setTitle(special.getString("title"));
                specialObject.setAmount(special.getString("amount"));

                ArrayList<Special> specs = new ArrayList<>();
                specs.add(specialObject);
                String newPrice = String.valueOf(Integer.parseInt(vehicle.getString("price")) - Integer.parseInt(special.getString("amount")));

                checkVehicle(dealer, vehicle, special, specialObject, specs, newPrice);
            }
        }
    }

    /**
     * Checks to see if the vehicle was previously found for a special. If so, the discounts are
     * aggregated. If not, a new vehicle is created and added to the ArrayList of vehicles.
     *
     * @param dealer        - dealer of the vehicle
     * @param vehicle       - vehicle to be added in some way
     * @param special       - special for this vehicle
     * @param specialObject - special being stored with vehicle
     * @param specs         - array of specs for the vehicle
     * @param newPrice      - new price of vehicle after specials
     * @throws JSONException
     */
    private void checkVehicle(JSONObject dealer, JSONObject vehicle, JSONObject special, Special specialObject, ArrayList<Special> specs, String newPrice) throws JSONException {
        boolean duplicate = false;
        for (Vehicle added : newVehicles) {
            // Combines specials for the same vehicle
            if (added.getId().equals(vehicle.getString("id"))) {
                ArrayList<Special> combine = added.getSpecials();
                combine.add(specialObject);
                added.setSpecials(combine);
                added.setDiscount(String.valueOf(Integer.parseInt(added.getDiscount()) + Integer.parseInt(special.getString("amount"))));
                duplicate = true;
            }
        }
        // Otherwise adds the new vehicle
        if (!duplicate) {
            Vehicle vehicleObject = new Vehicle(vehicle.getString("year"), vehicle.getString("make"), vehicle.getString("model"),
                    vehicle.getString("type"), (JSONArray) vehicle.get("specs"), vehicle.getString("id"), dealer.getString("dealerName"),
                    specs, vehicle.getString("year") + " " + vehicle.getString("make") + " " + vehicle.getString("model"),
                    newPrice, vehicle.getString("price"), vehicle.getString("urlImage"), special.getString("amount"));
            newVehicles.add(vehicleObject);
        }
    }

    /**
     * Calls createVehicles and then sets the adapter for the cards
     *
     * @param newVehicles - the vehicles for which cards will be created
     */
    public void addCards(ArrayList<Vehicle> newVehicles) {
        returnSize = newVehicles.size();
        cards = new ArrayList<>();
        createVehicles(0, newVehicles, cards);
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
     * Creates a card for each vehicle to be displayed
     *
     * @param index       - index in the overall JSON array of results for loading
     * @param newVehicles - the vehicles for which cards will be created
     * @param cards       - the cards to be created
     */
    public void createVehicles(int index, ArrayList<Vehicle> newVehicles, ArrayList<Card> cards) {
        for (int i = index; i < index + 10 && i < returnSize; i++) {
            VehicleCard card = new VehicleCard(getActivity(), R.layout.vehicle_card);
            final Vehicle vehicle = newVehicles.get(i);
            card.setTitle(vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel());
            try {
                card.setGasMileage((String) vehicle.getSpecs().get(0));
            } catch (JSONException e) {
                Log.e("BaseSearchFragment", "problem creating specials");
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
    }

    /**
     * Sets the listener for each card that holds vehicle detailed information for
     * the next view.
     *
     * @param vehicle - the vehicle being assigned the listener
     * @return - the listener
     */
    private Card.OnCardClickListener getCardOnClickListener(final Vehicle vehicle) {
        return new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Intent intent = new Intent(getActivity(), VehicleDetail.class);
                VehicleCard temp = (VehicleCard) card;
                intent.putExtra("title", temp.getTitle());
                intent.putExtra("oldP", temp.getOldPrice());
                intent.putExtra("newP", temp.getNewPrice());
                intent.putExtra("imageUrl", temp.getUrl());
                intent.putExtra("year", vehicle.getYear());
                intent.putExtra("make", vehicle.getMake());
                intent.putExtra("model", vehicle.getModel());
                ArrayList<String> tempSpecs = new ArrayList<>();
                for (int i = 0; i < vehicle.getSpecs().length(); i++) {
                    try {
                        tempSpecs.add(vehicle.getSpecs().get(i).toString());
                    } catch (JSONException e) {
                        Log.e("BaseSearchFragment", "error on card click");
                    }
                }
                intent.putStringArrayListExtra("spec", tempSpecs);
                getActivity().startActivity(intent);

            }
        };
    }

    /**
     * Overridden for loading but not used
     *
     * @param view        - current view
     * @param scrollState - current scroll state
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    /**
     * Called when scrolling occurs. Loads more cards at the bottom of the screen.
     *
     * @param view             - current view
     * @param firstVisibleItem - first visible item
     * @param visibleItemCount - number of visible items
     * @param totalItemCount   - total items
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
        if (loadMore && currIndex < returnSize - 1) {
            createVehicles(currIndex, newVehicles, cards);
            mCardArrayAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Inserts commas into a string that is really a number
     *
     * @param amount - string being edited
     * @return - the editied string
     */
    private String insertCommas(String amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        Double number = Double.parseDouble(amount);
        return String.valueOf(formatter.format(number));
    }

    /**
     * Parses the JSON response for vehicles and calls getVehicleInfo to continue the process
     *
     * @param response
     */
    public void vehicleSearch(JSONArray response) {
        newVehicles = new ArrayList<>();
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
                    getVehicleInfo(dealer, vehicle, id, special, ids);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        addCards(newVehicles);
    }

    /**
     * Generates the url for the GET request.
     *
     * @param parameters - maps of parameters
     * @return - the url to be used in the GET request
     */
    private String generateUrl(HashMap<String, String> parameters) {
        String url = BASE_URL + "lng=" + parameters.get("lng") + "&lat=" + parameters.get("lat") + "&keyword=" + parameters.get("keyword");
        if (isSearch) {
            url = url + "&model=" + parameters.get("model") + "&type=" + parameters.get("type");
        }
        return url;
    }

    /**
     * Callback for GET request with Google Volley
     */
    private class ResponseListener implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray response) {
            if (response.length() == 0) {
                TextView result = (TextView) baseView.findViewById(R.id.third_result);
                if(result != null){
                    result.setVisibility(View.VISIBLE);
                }
            } else {
                vehicleSearch(response);
            }
        }

    }

    /**
     * Error listener for GET request
     */
    private class ErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("error", "Http request failed");
        }
    }

}
