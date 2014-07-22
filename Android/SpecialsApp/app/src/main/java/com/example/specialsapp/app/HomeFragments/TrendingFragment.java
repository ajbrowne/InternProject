package com.example.specialsapp.app.HomeFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.specialsapp.app.Activities.HomeActivity;
import com.example.specialsapp.app.Activities.VehicleDetail;
import com.example.specialsapp.app.Cards.HomeVehicleCard;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Special;
import com.example.specialsapp.app.Models.Vehicle;
import com.example.specialsapp.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends BaseHomeFragment {

    private static final String TRENDING = "See What's Trending";
    private static final String TRENDING_DESCRIPTION = "Most Popular Deals";
    //TODO I've seen this in a couple of places - this is another thing that should be read in from a properties file
    private static final String BASE_URL = "http://192.168.169.252:8080/v1/specials/vehicle?";
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
        cards = new ArrayList<>();

        final GPS gps = new GPS(getActivity());
        Double latitude = gps.getLatitude();
        Double longitude = gps.getLongitude();

        setVariables(TRENDING, TRENDING_DESCRIPTION, BASE_URL);
        setParameters(latitude, longitude, homeView);

        return homeView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

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
    public ArrayList<Vehicle> idCheck(JSONObject dealer, JSONObject spec, JSONObject vehicle, JSONArray ids) throws JSONException {
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
        return vehicles;
    }

    /**
     * Creates the three cards for the view. Adds a listener with all information needed to
     * make the detailed views when a card is clicked.
     *
     * @param index    - index of card that is created
     * @param vehicles - vehicles for which cards are being created (top discounts)
     * @return - An ArrayList of the cards that are created
     */
    public ArrayList<Card> createVehicles(int index, ArrayList<Vehicle> vehicles) {
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
        createCards(homeView, TRENDING, TRENDING_DESCRIPTION, cards);
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
}

