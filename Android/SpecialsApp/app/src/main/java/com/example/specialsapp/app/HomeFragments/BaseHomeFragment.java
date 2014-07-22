package com.example.specialsapp.app.HomeFragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.specialsapp.app.Models.Vehicle;
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

import it.gmariotti.cardslib.library.internal.Card;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseHomeFragment extends Fragment {

    private static String TITLE;
    private static String DESCRIPTION;
    private static String BASE_URL;
    private ArrayList<Card> cards;
    private View homeView;
    private ArrayList<Vehicle> vehicles = new ArrayList<>();

    public BaseHomeFragment() {
        // Required empty public constructor
    }

    public void setVariables(String title, String description, String baseUrl) {
        this.TITLE = title;
        this.DESCRIPTION = description;
        this.BASE_URL = baseUrl;
    }

    /**
     * Gets the static fields on the view and sets them. Also sets the card adapter to
     * make cards visible.
     *
     * @param view        - current view
     * @param title       - "Top Discounts"
     * @param description - "The Best Deals Around"
     * @param theCards    - cards to be created
     */
    public abstract void createCards(View view, String title, String description, ArrayList<Card> theCards);

    /**
     * Used if trending is the subclass using this
     *
     * @param latitude  - lat of user
     * @param longitude - long of user
     * @param homeView  - current view
     */
    public void setParameters(double latitude, double longitude, View homeView) {
        this.homeView = homeView;
        String latt = String.valueOf(latitude);
        String longg = String.valueOf(longitude);

        HashMap<String, String> param = new HashMap<>();
        param.put("lng", longg);
        param.put("lat", latt);

        homeAsync(param, true);
    }

    /**
     * Makes the asynchronous call via Google Volley including caching (which actually gets used
     * due to the url being the same every time.)
     */
    public void homeAsync(HashMap<String, String> params, boolean trending) {
        AbstractHttpClient client = new DefaultHttpClient();
        RequestQueue queue = Volley.newRequestQueue(getActivity(), new HttpClientStack(client));

        if (trending) {
            BASE_URL = generateUrl(params);
        }

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(BASE_URL);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                JSONArray cached = new JSONArray(data);
                useResponse(cached);
            } catch (UnsupportedEncodingException | JSONException e) {
                Log.d("error", "Trending http request failed");
            }

        } else {
            JsonArrayRequest searchRequest = new JsonArrayRequest(BASE_URL, new ResponseListener(), new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("error", "Trending http request failed");
                }
            });
            queue.add(searchRequest);
        }
    }

    /**
     * Gets the current special and all vehicles for a dealer.
     *
     * @param dealer       - dealer nearest to user
     * @param specialArray - all specials at that dealer
     * @throws JSONException
     */
    private void specialHelp(JSONObject dealer, JSONArray specialArray) throws JSONException {
        for (int i = 0; i < specialArray.length(); i++) {
            JSONObject spec = (JSONObject) specialArray.get(i);
            JSONArray vehicles2 = (JSONArray) dealer.get("vehicles");
            vehicleHelp(dealer, spec, vehicles2);
        }
    }

    /**
     * Gets each vehicle and each id of vehicles in the special
     *
     * @param spec      - special being examined
     * @param vehicles2 - vehicles in the special
     * @throws JSONException
     */
    private void vehicleHelp(JSONObject dealer, JSONObject spec, JSONArray vehicles2) throws JSONException {
        for (int j = 0; j < vehicles2.length(); j++) {
            JSONObject vehicle = (JSONObject) vehicles2.get(j);
            JSONArray ids = (JSONArray) spec.get("vehicleId");
            vehicles = idCheck(dealer, spec, vehicle, ids);
        }
    }

    /**
     * Checks to see if a vehicle has been previously added. If not, a new vehicle object is
     * created and added to the ArrayList of vehicles for which cards will be created.
     *
     * @param spec    - special being examined
     * @param vehicle - vehicle being checked against
     * @param ids     - all vehicles in the special
     * @throws JSONException
     */
    public abstract ArrayList<Vehicle> idCheck(JSONObject dealer, JSONObject spec, JSONObject vehicle, JSONArray ids) throws JSONException;

    /**
     * Makes call to create the special cards.
     *
     * @param vehicles - vehicles with the top discounts
     */
    private void addCards(ArrayList<Vehicle> vehicles) {
        cards = new ArrayList<>();
        cards = createVehicles(0, vehicles);
    }

    /**
     * Creates the three cards for the view. Adds a listener with all information needed to
     * make the detailed views when a card is clicked.
     *
     * @param index    - index of card that is created
     * @param vehicles - vehicles for which cards are being created (top discounts)
     * @return - An ArrayList of the cards that are created
     */
    public abstract ArrayList<Card> createVehicles(int index, ArrayList<Vehicle> vehicles);

    /**
     * Used to insert commas into a string that is a number
     *
     * @param amount - In this case the car price being formatted
     * @return - the formatted string i.e 7,456
     */
    public String insertCommas(String amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        Double number = Double.parseDouble(amount);
        return String.valueOf(formatter.format(number));
    }

    /**
     * Method that has a response from either an HTTP GET or the cached car info.
     *
     * @param response - response received or cached response
     */
    public void useResponse(JSONArray response) {
        try {
            JSONObject dealer = (JSONObject) response.get(0);
            JSONArray specialArray = (JSONArray) dealer.get("specials");
            specialHelp(dealer, specialArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        addCards(vehicles);
    }

    private String generateUrl(HashMap<String, String> parameters) {
        return BASE_URL + "lng=" + parameters.get("lng") + "&lat=" + parameters.get("lat");
    }

    /**
     * Callback for the Google Volley HTTP GET
     */
    private class ResponseListener implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray response) {
            useResponse(response);
        }

    }
}
