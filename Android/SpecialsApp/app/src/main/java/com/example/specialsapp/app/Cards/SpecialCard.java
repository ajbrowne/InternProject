package com.example.specialsapp.app.Cards;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.specialsapp.app.R;
import com.loopj.android.image.SmartImageView;
import com.squareup.picasso.Picasso;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * This class provides a simple Google Play card to display
 * specials.
 *
 * @author brownea
 */
public class SpecialCard extends Card {

    private TextView mTitle;
    private TextView mDescription;
    private TextView mSpecialType;
    private TextView mDealer;
    private TextView mOldPrice;
    private TextView mNewPrice;
    private ImageView mThumbnail;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private Activity activity;

    private String title;
    private String description;
    private String type;
    private String dealer;
    private String oldPrice;
    private String newPrice;
    private String url;

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
        mThumbnail = (ImageView) parent.findViewById(R.id.thumbnail);

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
            Picasso.with(activity)
                    .load(url)
                    //.placeholder(R.drawable.silverado)
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
