package com.example.specialsapp.app.Cards;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.specialsapp.app.R;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * This class provides a simple Google Play card to display
 * dealers (may not be needed later)
 *
 * @author brownea
 */
public class DealerCard extends Card {

    private String dealer;
    private String cityState;
    private String distance;
    private String numSpecials;
    private Context context;
    private double lat;
    private double longi;

    public DealerCard(Context context) {
        this(context, R.layout.vehicle_card, 0.0, 0.0);
    }

    public DealerCard(Context context, int innerLayout, double lat, double longi) {
        super(context, innerLayout);
        this.context = context;
        this.lat = lat;
        this.longi = longi;
    }

    public DealerCard(Context context, String dealer, String cityState, String distance, String numSpecials, double lat, double longi) {
        super(context);
        this.dealer = dealer;
        this.cityState = cityState;
        this.distance = distance;
        this.numSpecials = numSpecials;
        this.lat = lat;
        this.longi = longi;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView mDealer = (TextView) parent.findViewById(R.id.dealer);
        TextView mCityState = (TextView) parent.findViewById(R.id.city);
        TextView mDistance = (TextView) parent.findViewById(R.id.distance);
        TextView mNumSpecials = (TextView) parent.findViewById(R.id.deals);
        ImageView mPin = (ImageView) parent.findViewById(R.id.mapButton);

        mDealer.setText(dealer);
        mCityState.setText(cityState);
        mDistance.setText(distance);
        mNumSpecials.setText(numSpecials);
        mPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uriBegin = "geo:" + lat + "," + longi;
                String query = dealer;
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }

    public String getCityState() {
        return this.cityState;
    }

    public void setCityState(String cityState){
        this.cityState = cityState;
    }

    public String getDealer() {
        return this.dealer;
    }

    public void setDealer(String dealer){ this.dealer = dealer; }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getNumSpecials() {
        return numSpecials;
    }

    public void setNumSpecials(String numSpecials) {
        this.numSpecials = numSpecials;
    }



}
