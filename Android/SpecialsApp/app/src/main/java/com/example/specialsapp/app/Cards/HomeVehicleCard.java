package com.example.specialsapp.app.Cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.specialsapp.app.R;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * This class provides a simple Google Play card to display
 * dealers (may not be needed later)
 *
 * @author brownea
 */
public class HomeVehicleCard extends Card {

    protected TextView mDealer;
    protected TextView mCityState;
    protected String dealer;
    protected String cityState;

    public HomeVehicleCard(Context context) {
        this(context, R.layout.h_vehicle_card);
    }

    public HomeVehicleCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
//        mDealer = (TextView) parent.findViewById(R.id.dealer);
//        mCityState = (TextView) parent.findViewById(R.id.city);
//        mDealer.setText(this.dealer);
//        mCityState.setText(this.cityState);
    }

    public void setDealer(String dealer){
        this.dealer = dealer;
    }

    public void setCityState(String cityState){
        this.cityState = cityState;
    }

    public String getCityState() {
        return this.cityState;
    }

    public String getDealer() {
        return this.dealer;
    }
}
