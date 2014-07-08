package com.example.specialsapp.app.Cards;

import android.content.Context;
import android.graphics.Paint;
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
public class SpecialCard extends Card {

    private Context context;

    private String title;
    private String description;
    private String type;
    private String dealer;
    private String oldPrice;
    private String newPrice;
    private String url;

    public SpecialCard(Context context) {
        this(context, R.layout.special_card);
        this.context = context;
    }

    public SpecialCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView mDealer = (TextView) parent.findViewById(R.id.dealerName);
        TextView mTitle = (TextView) parent.findViewById(R.id.title);
        TextView mSpecialType = (TextView) parent.findViewById(R.id.type);
        TextView mDescription = (TextView) parent.findViewById(R.id.subTitle);
        TextView mNewPrice = (TextView) parent.findViewById(R.id.newPrice);
        TextView mOldPrice = (TextView) parent.findViewById(R.id.oldPrice);
        ImageView mThumbnail = (ImageView) parent.findViewById(R.id.thumbnail);

        mDealer.setText(dealer);
        mTitle.setText(title);
        mDescription.setText(description);
        mSpecialType.setText(type);
        mOldPrice.setText(oldPrice);


        if (mOldPrice.getText().toString().equals("Multiple Vehicles")){
            mOldPrice.setText(oldPrice);
            mOldPrice.setPaintFlags(0);
            mNewPrice.setText("");
            mThumbnail.setVisibility(View.GONE);
        }
        else{
            mOldPrice.setText("$" + oldPrice);
            mOldPrice.setPaintFlags(mOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mNewPrice.setText("$" + newPrice);
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.tesla)
                    .resize(335, 600)
                    .into(mThumbnail);
            mThumbnail.setVisibility(View.VISIBLE);
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
