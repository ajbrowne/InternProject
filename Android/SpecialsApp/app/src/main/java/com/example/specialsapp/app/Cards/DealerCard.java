package com.example.specialsapp.app.Cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.specialsapp.app.R;

import org.w3c.dom.Text;

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

    public DealerCard(Context context) {
        this(context, R.layout.vehicle_card);
    }

    public DealerCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView mDealer = (TextView) parent.findViewById(R.id.dealer);
        TextView mCityState = (TextView) parent.findViewById(R.id.city);
        TextView mDistance = (TextView) parent.findViewById(R.id.distance);
        TextView mNumSpecials = (TextView) parent.findViewById(R.id.deals);

        mDealer.setText(dealer);
        mCityState.setText(cityState);
        mDistance.setText(distance);
        mNumSpecials.setText(numSpecials);
    }

    public void setDealer(String dealer){ this.dealer = dealer; }

    public void setCityState(String cityState){
        this.cityState = cityState;
    }

    public String getCityState() {
        return this.cityState;
    }

    public String getDealer() {
        return this.dealer;
    }

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
