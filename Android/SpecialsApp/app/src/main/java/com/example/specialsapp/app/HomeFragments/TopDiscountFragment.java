package com.example.specialsapp.app.HomeFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.specialsapp.app.Activities.VehicleDetail;
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
import java.lang.reflect.GenericArrayType;
import java.text.DecimalFormat;
import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * View similar to those in the Google Play Store home view displaying the top discounts
 * that are currently available.
 */
public class TopDiscountFragment extends BaseHomeFragment {

    private static final String TOP_DISCOUNTS = "Top Discounts";
    private static final String TOP_DISCOUNTS_DESCRIPTION = "The Best Deals Around";
    private static final String BASE_URL = "http://192.168.169.252:8080/v1/specials/special/top";
    private ArrayList<Card> cards;
    private View homeView;
    private ArrayList<String> addedVehicles = new ArrayList<>();
    private ArrayList<Vehicle> vehicles = new ArrayList<>();

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
        cards = new ArrayList<>();

        setVariables(TOP_DISCOUNTS, TOP_DISCOUNTS_DESCRIPTION, BASE_URL);
        homeAsync(null, false);

        return homeView;
    }

    /**
     * Checks to see if a vehicle has been previously added. If not, a new vehicle object is
     * created and added to the ArrayList of vehicles for which cards will be created.
     * @param spec - special being examined
     * @param vehicle - vehicle being checked against
     * @param ids - all vehicles in the special
     * @throws JSONException
     */
    public ArrayList<Vehicle> idCheck(JSONObject dealer, JSONObject spec, JSONObject vehicle, JSONArray ids) throws JSONException {
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
        return vehicles;
    }

    /**
     * Creates the three cards for the view. Adds a listener with all information needed to
     * make the detailed views when a card is clicked.
     * @param index - index of card that is created
     * @param vehicles - vehicles for which cards are being created (top discounts)
     * @return - An ArrayList of the cards that are created
     */
    public ArrayList<Card> createVehicles(int index, ArrayList<Vehicle> vehicles) {
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
                    Intent intent = new Intent(getActivity(), VehicleDetail.class);
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
        createCards(homeView, TOP_DISCOUNTS, TOP_DISCOUNTS_DESCRIPTION, cards);
        return cards;
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
    public void createCards(View view, String title, String description, ArrayList<Card> theCards) {
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
}
