package com.example.specialsapp.app.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.specialsapp.app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * This is the vehicle detail activity that displays info
 * about vehicles and allows the user to send their
 * information to the dealer that has that vehicle.
 *
 * @author maharb
 */
public class VehicleDetail extends BaseActivity {

    /**
     * The main method that sets up the view
     *
     * @param savedInstanceState - saved data for the view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_detail);
        Bundle extras = getIntent().getExtras();
        TextView mTitle = (TextView) findViewById(R.id.title_text);
        TextView mOldPrice = (TextView) findViewById(R.id.price_old);
        TextView mNewPrice = (TextView) findViewById(R.id.price_new);
        ImageView mTitleImage = (ImageView) findViewById(R.id.title_image);
        ListView mSpecsList = (ListView) findViewById(R.id.specs_list);


        //Set up the views with the correct information about the
        //vehicles
        if (extras != null) {
            mTitle.setText(extras.getString("title"));
            mOldPrice.setText("$" + extras.getString("oldP"));
            mOldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mNewPrice.setText("$" + extras.getString("newP"));
            Picasso.with(this)
                    .load(extras.getString("imageUrl"))
                    .placeholder(R.drawable.tesla)
                    .resize(335, 600)
                    .into(mTitleImage);
            ArrayList<String> specsTemp = (ArrayList<String>) extras.get("spec");
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_item, specsTemp);
            mSpecsList.setAdapter(spinnerArrayAdapter);
        }

        //Configure actionbar and up navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Details");
    }


    /**
     * Sets up the options menu
     *
     * @param menu - the menu to be configured
     * @return - return true if successfully created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Configure the items in the options menu to be selected
     *
     * @param item - the item selected
     * @return - return the result of the parent of this method
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Logic to handle clicking the buy button in the view
     * This allows you to submit your info to the dealer.
     * Depends on if the user is logged in or not
     *
     * @param view - the view that contains the button
     */
    public void submitInfo(View view) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        if (shared.getBoolean("stored", true)) {
            Toast.makeText(this.getApplicationContext(), "Info Submitted", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(VehicleDetail.this, MainActivity.class);
            intent.putExtra("submit", true);
            startActivity(intent);
        }
    }
}
