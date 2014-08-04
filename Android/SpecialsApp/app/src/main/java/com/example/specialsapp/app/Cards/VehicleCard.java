package com.example.specialsapp.app.Cards;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.specialsapp.app.R;
import com.squareup.picasso.Picasso;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * This class provides a simple Google Play card to display
 * specials.
 *
 * @author brownea
 */
public class VehicleCard extends Card {

    private Context context;
    private String title;
    private String gasMileage;
    private String type;
    private String dealer;
    private String oldPrice;
    private String newPrice;
    private String url;

    public VehicleCard(Context context) {
        this(context, R.layout.vehicle_card);
        this.context = context;
    }

    public VehicleCard(Context context, int innerLayout) {
        super(context, innerLayout);
        this.context = context;
    }

    /**
     * Automatically called upon a listener being added to a card array (I think).
     * Sets up all elements on a card.
     *
     * @param parent - the parent view
     * @param view   - the current view where cards will be added
     */
    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView mDealer = (TextView) parent.findViewById(R.id.dealerName);
        TextView mTitle = (TextView) parent.findViewById(R.id.title);
        TextView mSpecialType = (TextView) parent.findViewById(R.id.type);
        TextView mGasMileage = (TextView) parent.findViewById(R.id.gasMileage);
        TextView mNewPrice = (TextView) parent.findViewById(R.id.newPrice);
        TextView mOldPrice = (TextView) parent.findViewById(R.id.oldPrice);
        ImageView mThumbnail = (ImageView) parent.findViewById(R.id.thumbnail);
        ImageView mPhone = (ImageView) parent.findViewById(R.id.phoneButton);

        mDealer.setText(dealer);
        mTitle.setText(title);
        mGasMileage.setText(gasMileage);
        mSpecialType.setText(type);

        mOldPrice.setText("$" + oldPrice);
        mOldPrice.setPaintFlags(mOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        mNewPrice.setText("$" + newPrice);

        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "(555)-434-5638"));
                context.startActivity(intent);
            }
        });

        // Used to load images either from imgur or cache
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.tesla)
                .resize(335, 600)
                .into(mThumbnail);
        mThumbnail.setVisibility(View.VISIBLE);
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

    public String geVehicleType() {
        return type;
    }

    public void setVehicleType(String type) {
        this.type = type;
    }

    public String getGasMileage() {
        return gasMileage;
    }

    public void setGasMileage(String gasMileage) {
        this.gasMileage = gasMileage;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
