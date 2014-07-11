package com.example.specialsapp.app.HomeFragments;

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

import com.example.specialsapp.app.Activities.HomeActivity;
import com.example.specialsapp.app.Activities.SearchActivity;
import com.example.specialsapp.app.Activities.SpecialDetail;
import com.example.specialsapp.app.Cards.HomeVehicleCard;
import com.example.specialsapp.app.GPS.GPS;
import com.example.specialsapp.app.Models.Special;
import com.example.specialsapp.app.Models.Vehicle;
import com.example.specialsapp.app.R;
import com.example.specialsapp.app.Rest.SpecialsRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment {

    private static final String Trending = "See What's Trending";
    private static final String TrendingDescription = "Most Popular Deals";

    private ArrayList<Card> cards;
    private View homeView;
    private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra("tab", 0);
            startActivity(intent);
        }
        return true;
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

    private void getTrending(double latitude, double longitude) {
        String latt = String.valueOf(latitude);
        String longg = String.valueOf(longitude);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("lng", longg);
        param.put("lat", latt);
        param.put("make", "");
        param.put("extra", "0");
        RequestParams params = new RequestParams(param);

        System.out.println(params);
        trendingAsync(params);
    }

    private void trendingAsync(RequestParams params) {
        SpecialsRestClient.get("vehicle", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray request) {
                try {
                    JSONObject dealer = (JSONObject) request.get(0);
                    JSONArray specialArray = (JSONArray) dealer.get("specials");
                    trendingSpecialHelp(dealer, specialArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addCards(vehicles);
            }
        });
    }

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

                ArrayList<Special> specs = new ArrayList<Special>();
                specs.add(specialObject);
                String newPrice = String.valueOf(Integer.parseInt(vehicle.getString("price")) - Integer.parseInt(spec.getString("amount")));

                boolean duplicate = false;
                for (int l = 0; l < vehicles.size(); l++) {
                    Vehicle added = vehicles.get(l);
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
        cards = new ArrayList<Card>();
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
        createCards(homeView, Trending, TrendingDescription, cards);
        return cards;
    }

    public String insertCommas(String amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        Double number = Double.parseDouble(amount);
        return String.valueOf(formatter.format(number));
    }
}
