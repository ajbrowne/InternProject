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
 * dealers (may not be needed later)
 *
 * @author brownea
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

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView mName = (TextView) parent.findViewById(R.id.smallCar);
        TextView mPrice = (TextView) parent.findViewById(R.id.smallPrice);
        TextView mType = (TextView) parent.findViewById(R.id.smallType);
        ImageView thumbnail = (ImageView) parent.findViewById(R.id.gridCar);

        mName.setText(this.name);
        mPrice.setText("$" + this.price);
        mType.setText(this.type);
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.tesla)
                .resize(100, 80)
                .into(thumbnail);

        if (this.price.compareTo("") == 0) {
            mPrice.setText("$60,000");
            mName.setText("2004 Pontiac Aztek");
            mType.setText("Used");
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.tesla)
                    .resize(100, 80)
                    .into(thumbnail);
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
