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
public class HomeVehicleCard extends Card {

    private TextView mName;
    private TextView mPrice;
    private TextView mType;
    private String name = "";
    private String price = "";
    private String type = "";

    public HomeVehicleCard(Context context) {
        this(context, R.layout.h_vehicle_card);
    }

    public HomeVehicleCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        mName = (TextView) parent.findViewById(R.id.smallCar);
        mPrice = (TextView) parent.findViewById(R.id.smallPrice);
        mType = (TextView) parent.findViewById(R.id.smallType);

        mName.setText(this.name);
        mPrice.setText("$" + this.price);
        mType.setText(this.type);

        if (this.price.compareTo("") == 0){
            mPrice.setText("$6000");
            mName.setText("2004 Pontiac Aztek");
            mType.setText("Used");

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVehicleType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
