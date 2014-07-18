package com.example.specialsapp.app.Cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.specialsapp.app.R;
import com.squareup.picasso.Picasso;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * This class provides a simple Google Play card to display
 */
public class HomeVehicleCard extends Card {

    private String name = "";
    private String price = "";
    private String type = "";
    private String url;
    private Context context;

    public HomeVehicleCard(Context context) {
        this(context, R.layout.h_vehicle_card);
        this.context = context;
    }

    public HomeVehicleCard(Context context, int innerLayout) {
        super(context, innerLayout);
        this.context = context;
    }

    /**
     * Automatically called upon a listener being added to a card array (I think).
     * Sets up all elements on a card.
     * @param parent - the parent view
     * @param view - the current view where cards will be added
     */
    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView mName = (TextView) parent.findViewById(R.id.smallCar);
        TextView mPrice = (TextView) parent.findViewById(R.id.smallPrice);
        TextView mType = (TextView) parent.findViewById(R.id.smallType);
        ImageView thumbnail = (ImageView) parent.findViewById(R.id.gridCar);

        mName.setText(this.name);
        mPrice.setText("$" + this.price);
        mType.setText(this.type);

        // Loads images either from imgur or cache
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.tesla)
                .resize(100, 80)
                .into(thumbnail);
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
