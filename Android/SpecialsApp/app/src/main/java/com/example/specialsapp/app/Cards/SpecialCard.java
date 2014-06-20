package com.example.specialsapp.app.Cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.specialsapp.app.R;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * This class provides a simple Google Play card
 *
 * @author brownea
 */
public class SpecialCard extends Card {

    protected TextView dealer;
    protected TextView cityState;

    public SpecialCard(Context context) {
        this(context, R.layout.deal_card);
    }

    public SpecialCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    private void init() {
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getContext(), "Click Listener card'", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        dealer = (TextView) view.findViewById(R.id.dealer);
        cityState = (TextView) view.findViewById(R.id.city);
    }

    public void setDealer(String name){
        //setupInnerViewElements(null, view);
        if (dealer != null){
            dealer.setText(name);
        }
    }

    public void setCityState(String location){
        //setupInnerViewElements(null, view);
        if (cityState != null){
            cityState.setText(location);
        }
    }
}
