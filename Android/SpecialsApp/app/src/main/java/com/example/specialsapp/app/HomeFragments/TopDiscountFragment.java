package com.example.specialsapp.app.HomeFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.specialsapp.app.Activities.SearchActivity;
import com.example.specialsapp.app.Activities.SpecialDetail;
import com.example.specialsapp.app.Cards.HomeVehicleCard;
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

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopDiscountFragment extends Fragment {

    private static final String TopDiscounts = "Top Discounts";
    private static final String TopDiscountsDescription = "The Best Deals Around";
    private static final String baseUrl = "http://192.168.171.146:8080/v1/specials/special/top";
    private ArrayList<Card> cards;
    private View homeView;
    private ArrayList<String> addedVehicles = new ArrayList<String>();
    private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
    private RequestQueue queue;
    private AbstractHttpClient client;
    private ProgressDialog pDialog;

    public TopDiscountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_top_discount, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Home");

        client = new DefaultHttpClient();
        queue = Volley.newRequestQueue(getActivity(), new HttpClientStack(client));

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        topAsync();

        return homeView;
    }

    private void createCards(View view, String title, String description, ArrayList<Card> theCards) {
        TextView theTitle = (TextView) view.findViewById(R.id.newVehicles1);
        TextView theDescription = (TextView) view.findViewById(R.id.descrip1);
        TextView more = (TextView) view.findViewById(R.id.more1);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).getViewPager().setCurrentItem(1);
            }
        });

        theTitle.setText(title);
        theDescription.setText(description);

        CardGridArrayAdapter cardGridArrayAdapter = new CardGridArrayAdapter(getActivity(), theCards);
        CardGridView gridView = (CardGridView) view.findViewById(R.id.newGrid1);
        if (gridView != null) {
            gridView.setAdapter(cardGridArrayAdapter);
        }
    }

    private void topAsync() {
        client = new DefaultHttpClient();
        queue = Volley.newRequestQueue(getActivity(), new HttpClientStack(client));

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(baseUrl);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                JSONArray cached = new JSONArray(data);
                System.out.println("Cached top discounts call");
                getDiscounts(cached);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            JsonArrayRequest searchRequest = new JsonArrayRequest(baseUrl, new ResponseListener(), new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            queue.add(searchRequest);
        }

    }

    private void topDiscountHelp(JSONObject dealer, JSONArray specialArray) throws JSONException {
        for (int i = 0; i < specialArray.length(); i++) {
            JSONObject spec = (JSONObject) specialArray.get(i);
            JSONArray vehicles2 = (JSONArray) dealer.get("vehicles");
            topVehicleHelp(spec, vehicles2);
        }
    }

    private void topVehicleHelp(JSONObject spec, JSONArray vehicles2) throws JSONException {
        for (int j = 0; j < vehicles2.length(); j++) {
            JSONObject vehicle = (JSONObject) vehicles2.get(j);
            JSONArray ids = (JSONArray) spec.get("vehicleId");
            topIdCheck(spec, vehicle, ids);
        }
    }

    private void topIdCheck(JSONObject spec, JSONObject vehicle, JSONArray ids) throws JSONException {
        for (int k = 0; k < ids.length(); k++) {
            boolean add = false;
            for (String addedVehicle : addedVehicles) {
                if (addedVehicle.compareTo((String) ids.get(k)) == 0) {
                    add = true;
                }
            }
            if (!add) {
                if (vehicle.getString("id").compareTo((String) ids.get(k)) == 0) {
                    Vehicle newVehicle = new Vehicle();
                    addedVehicles.add((String) ids.get(k));
                    newVehicle.setNewPrice(String.valueOf(vehicle.getInt("price") - Integer.parseInt(spec.getString("amount"))));
                    newVehicle.setOldPrice(String.valueOf(vehicle.getInt("price")));
                    newVehicle.setName(vehicle.getString("year") + " " + vehicle.getString("make") + " " + vehicle.getString("model"));
                    newVehicle.setVehicleType(vehicle.getString("type"));
                    newVehicle.setUrl(vehicle.getString("urlImage"));
                    newVehicle.setSpecs(vehicle.getJSONArray("specs"));
                    newVehicle.setMake(vehicle.getString("make"));
                    newVehicle.setYear(vehicle.getString("year"));
                    newVehicle.setModel(vehicle.getString("model"));
                    vehicles.add(newVehicle);
                }
            }
        }
    }

    private void addCards(ArrayList<Vehicle> vehicles) {
        cards = new ArrayList<Card>();
        cards = createSpecials(0, vehicles);
    }

    public ArrayList<Card> createSpecials(int index, ArrayList<Vehicle> vehicles) {
        for (int i = index; i < 3; i++) {
            HomeVehicleCard card = new HomeVehicleCard(getActivity(), R.layout.h_vehicle_card);
            final Vehicle vehicle = vehicles.get(i);
            int discount = Integer.parseInt(vehicle.getOldPrice()) - Integer.parseInt(vehicle.getNewPrice());
            card.setTitle(vehicle.getYear() + " " + vehicle.getMake() + " " + vehicle.getModel());
            card.setName(vehicle.getName());
            card.setPrice(insertCommas(String.valueOf(discount)) + " Off");
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
            });

            cards.add(card);
        }
        createCards(homeView, TopDiscounts, TopDiscountsDescription, cards);
        return cards;
    }

    public String insertCommas(String amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        Double number = Double.parseDouble(amount);
        return String.valueOf(formatter.format(number));
    }

    private void getDiscounts(JSONArray response) {
        pDialog.hide();
        try {
            JSONObject dealer = (JSONObject) response.get(0);
            JSONArray specialArray = (JSONArray) dealer.get("specials");
            topDiscountHelp(dealer, specialArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        addCards(vehicles);
    }

    private class ResponseListener implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray response) {
            getDiscounts(response);
        }

    }
}
