package com.example.specialsapp.app.HomeFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.specialsapp.app.Activities.HomeActivity;
import com.example.specialsapp.app.Activities.SpecialDetail;
import com.example.specialsapp.app.Cards.HomeVehicleCard;
import com.example.specialsapp.app.GPS.GPS;
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

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment {

    private static final String TRENDING = "See What's Trending";
    private static final String TRENDING_DESCRIPTION = "Most Popular Deals";
    //TODO I've seen this in a couple of places - this is another thing that should be read in from a properties file
    private static final String BASE_URL = "http://192.168.168.235:8080/v1/specials/vehicle?";
    private ArrayList<Card> cards;
    private View homeView;
    private ArrayList<Vehicle> vehicles = new ArrayList<>();

    public TrendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_trending, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Home");

        final GPS gps = new GPS(getActivity());
        Double latitude = gps.getLatitude();
        Double longitude = gps.getLongitude();

        getTrending(latitude, longitude);

        return homeView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    private void createCards(View view, String title, String description, ArrayList<Card> theCards) {
        TextView theTitle = (TextView) view.findViewById(R.id.newVehicles);
        TextView theDescription = (TextView) view.findViewById(R.id.descrip);
        TextView more = (TextView) view.findViewById(R.id.more);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).getViewPager().setCurrentItem(1);
            }
        });

        theTitle.setText(title);
        theDescription.setText(description);

        CardGridArrayAdapter cardGridArrayAdapter = new CardGridArrayAdapter(getActivity(), theCards);
        CardGridView gridView = (CardGridView) view.findViewById(R.id.newGrid);
        if (gridView != null) {
            gridView.setAdapter(cardGridArrayAdapter);
        }
    }

    // TODO I feel like I've seen this method before as well, or several variations on pretty much the same thing
    // TODO is there a way we can centralize these methods and make it better, like using the builder pattern?
    private void getTrending(double latitude, double longitude) {
        String latt = String.valueOf(latitude);
        String longg = String.valueOf(longitude);

        HashMap<String, String> param = new HashMap<>();
        param.put("lng", longg);
        param.put("lat", latt);

        trendingAsync(param);
    }

    private void trendingAsync(HashMap<String, String> params) {
        AbstractHttpClient client = new DefaultHttpClient();
        RequestQueue queue = Volley.newRequestQueue(getActivity(), new HttpClientStack(client));

        String url = generateUrl(params);
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                JSONArray cached = new JSONArray(data);
                getTrending(cached);
            } catch (UnsupportedEncodingException|JSONException e) {
                Log.d("error", "Trending http request failed");
            }

        } else {
            JsonArrayRequest searchRequest = new JsonArrayRequest(url, new ResponseListener(), new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("error", "Trending http request failed");
                }
            });
            queue.add(searchRequest);
        }
    }

    // TODO These two methods are so similar to the ones in TopDiscountFragment. Should we be inheriting from a common superclass fragment that contains these implementations?
    // TODO or at least be using an interface that contains method declarations for these?
    private void trendingSpecialHelp(JSONObject dealer, JSONArray specialArray) throws JSONException {
        for (int i = 0; i < specialArray.length(); i++) {
            JSONObject spec = (JSONObject) specialArray.get(i);
            JSONArray vehicles2 = (JSONArray) dealer.get("vehicles");
            trendingVehicleHelp(dealer, spec, vehicles2);
        }
    }

    private void trendingVehicleHelp(JSONObject dealer, JSONObject spec, JSONArray vehicles2) throws JSONException {
        for (int j = 0; j < vehicles2.length(); j++) {
            JSONObject vehicle = (JSONObject) vehicles2.get(j);
            JSONArray ids = (JSONArray) spec.get("vehicleId");
            trendingIdCheck(dealer, spec, vehicle, ids);
        }
    }

    private void trendingIdCheck(JSONObject dealer, JSONObject spec, JSONObject vehicle, JSONArray ids) throws JSONException {
        for (int k = 0; k < ids.length(); k++) {
            if (ids.get(k).equals(vehicle.getString("id"))) {
                Special specialObject = new Special();
                specialObject.setTitle(spec.getString("title"));
                specialObject.setAmount(spec.getString("amount"));

                ArrayList<Special> specs = new ArrayList<>();
                specs.add(specialObject);
                String newPrice = String.valueOf(Integer.parseInt(vehicle.getString("price")) - Integer.parseInt(spec.getString("amount")));

                boolean duplicate = false;
                for (Vehicle added : vehicles) {
                    if (added.getId().equals(vehicle.getString("id"))) {
                        ArrayList<Special> combine = added.getSpecials();
                        combine.add(specialObject);
                        added.setSpecials(combine);
                        added.setDiscount(String.valueOf(Integer.parseInt(added.getDiscount()) + Integer.parseInt(spec.getString("amount"))));
                        duplicate = true;
                    }
                }
                if (!duplicate) {
                    Vehicle vehicleObject = new Vehicle(vehicle.getString("year"), vehicle.getString("make"), vehicle.getString("model"),
                            vehicle.getString("type"), (JSONArray) vehicle.get("specs"), vehicle.getString("id"), dealer.getString("dealerName"),
                            specs, vehicle.getString("year") + " " + vehicle.getString("make") + " " + vehicle.getString("model"),
                            newPrice, vehicle.getString("price"), vehicle.getString("urlImage"), spec.getString("amount"));
                    vehicles.add(vehicleObject);
                }
            }
        }
    }

    private void addCards(ArrayList<Vehicle> vehicles) {
        cards = new ArrayList<>();
        cards = createSpecials(0, vehicles);
    }

    public ArrayList<Card> createSpecials(int index, ArrayList<Vehicle> vehicles) {
        for (int i = index; i < 3; i++) {
            HomeVehicleCard card = new HomeVehicleCard(getActivity(), R.layout.h_vehicle_card);
            final Vehicle vehicle = vehicles.get(i);
            card.setTitle(vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel());
            card.setName(vehicle.getName());
            int newPrice = Integer.parseInt(vehicle.getOldPrice()) - Integer.parseInt(vehicle.getDiscount());
            card.setPrice(insertCommas(String.valueOf(newPrice)));
            card.setType(vehicle.getVehicleType());
            card.setUrl(vehicle.getUrl());

            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Intent intent = new Intent(getActivity(), SpecialDetail.class);
                    HomeVehicleCard temp = (HomeVehicleCard) card;
                    intent.putExtra("title", temp.getTitle());
                    intent.putExtra("oldP", insertCommas(vehicle.getOldPrice()));
                    intent.putExtra("newP", insertCommas(vehicle.getNewPrice()));
                    intent.putExtra("imageUrl", temp.getUrl());
                    intent.putExtra("year", vehicle.getYear());
                    intent.putExtra("make", vehicle.getMake());
                    intent.putExtra("model", vehicle.getModel());
                    ArrayList<String> tempSpecs = new ArrayList<>();
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
            });

            cards.add(card);
        }
        createCards(homeView, TRENDING, TRENDING_DESCRIPTION, cards);
        return cards;
    }

    public String insertCommas(String amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        Double number = Double.parseDouble(amount);
        return String.valueOf(formatter.format(number));
    }

    public void getTrending(JSONArray response) {
        try {
            System.out.println("OH DEAR GOD");
            System.out.println(this);
            JSONObject dealer = (JSONObject) response.get(0);
            JSONArray specialArray = (JSONArray) dealer.get("specials");
            trendingSpecialHelp(dealer, specialArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        addCards(vehicles);
    }

    private String generateUrl(HashMap<String, String> parameters) {
        return BASE_URL + "lng=" + parameters.get("lng") + "&lat=" + parameters.get("lat");
    }

    private class ResponseListener implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray response) {
            getTrending(response);
        }

    }
}
