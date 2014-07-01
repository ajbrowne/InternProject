package com.example.specialsapp.app.Cards;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.specialsapp.app.R;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * This class provides a simple Google Play card to display
 * specials.
 *
 * @author brownea
 */
public class SpecialCard extends Card {

    protected TextView mTitle;
    protected TextView mDescription;
    protected TextView mSpecialType;
    protected TextView mDealer;
    protected TextView mOldPrice;
    protected TextView mNewPrice;

    protected String title;
    protected String description;
    protected String type;
    protected String dealer;
    protected String oldPrice;
    protected String newPrice;

    public SpecialCard(Context context) {
        this(context, R.layout.special_card);
    }

    public SpecialCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        mDealer = (TextView) parent.findViewById(R.id.dealerName);
        mTitle = (TextView) parent.findViewById(R.id.title);
        mSpecialType = (TextView) parent.findViewById(R.id.type);
        mDescription = (TextView) parent.findViewById(R.id.subTitle);
        mNewPrice = (TextView) parent.findViewById(R.id.newPrice);
        mOldPrice = (TextView) parent.findViewById(R.id.oldPrice);


        mDealer.setText(this.dealer);
        mTitle.setText(title);
        mDescription.setText(description);
        mSpecialType.setText(type);
        if (mOldPrice.getText().toString().compareTo("Multiple Vehicles") != 0){
            mOldPrice.setPaintFlags(mOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mNewPrice.setText("$" + newPrice);
            mOldPrice.setText("$" + oldPrice);
        }
        else{
            mOldPrice.setText(oldPrice);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getSpecialType() {
        return type;
    }

    public void setSpecialType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

}
