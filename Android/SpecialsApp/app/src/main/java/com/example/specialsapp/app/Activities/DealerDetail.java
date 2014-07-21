package com.example.specialsapp.app.Activities;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.specialsapp.app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This is the dealer detail activity.
 * It sets up the activity to display dealer information
 * and to display the dealers location on a map.
 *
 * @author maharb
 */
public class DealerDetail extends BaseActivity {

    private GoogleMap googleMap;
    private String phoneNumber;
    private TextView mAddress; //TODO what is m for?
    private TextView mName;
    private TextView mPhone;
    private TextView mDistance;
    private Geocoder mGeoCoder;
    private MarkerOptions mMarkerOptions;

    /**
     * The method that handles the creation of the activity.
     * This is where the views are declared if they need to be
     * used in this activity.
     *
     * @param savedInstanceState - the saved bundle that is used to create
     *                           the activity if any data is saved.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_detail);
        Bundle extras = getIntent().getExtras();
        mGeoCoder = new Geocoder(this, Locale.getDefault());
        //declare the textviews and get the actual views
        loadTextViews();
        float ZOOM = 13;
        //set icon as a back button for navigation
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Load the map for the dealers location
        loadMap();

        //Get the information passed to this activity to display
        //the vehicle info
        new GetAddressTask().execute(extras.getDouble("lat"), extras.getDouble("long"));
        mName.setText(extras.getString("name"));
        mPhone.setText("555-555-5555");
        mDistance.setText(extras.getString("dist") + " miles away");
        phoneNumber = "5555555555";
        configureMap(extras, ZOOM);
    }

    /**
     * Initialize TextViews
     */
    private void loadTextViews() {
        mAddress = (TextView) findViewById(R.id.dealer_address);
        mName = (TextView) findViewById(R.id.dealer_name);
        mPhone = (TextView) findViewById(R.id.dealer_phone);
        mDistance = (TextView) findViewById(R.id.dealer_distance);
    }

    /**
     * Configures the map to show the pin where the dealer
     * is located. Also sets the info text to be clickable
     * and to take the user to google maps.
     *
     * @param extras - the info needed to create the pins
     * @param ZOOM - distance to zoom in on the map
     */
    private void configureMap(final Bundle extras, float ZOOM) {
        mMarkerOptions = new MarkerOptions().position(new LatLng(extras.getDouble("lat"), extras.getDouble("long"))).title(extras.getString("name"));
        Marker marker = googleMap.addMarker(mMarkerOptions);
        marker.showInfoWindow();
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(extras.getDouble("lat"), extras.getDouble("long"))).zoom(ZOOM).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String uriBegin = "geo:" + extras.getDouble("lat") + "," + extras.getDouble("long");
                String encodedQuery = Uri.encode(extras.getString("name"));
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }


    /**
     * Sets up the options menu for this activity
     *
     * @param menu - the menu that is passed in by android
     * @return - returns if the menu was created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * sets what happens when a menu item is clicked
     *
     * @param item - the menu item selected
     * @return - true if it was successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Load the map. If map is not created it will create it for you
     */
    private void loadMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // check if map is created successfully or not
            if (googleMap == null) {

                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /**
     * Override onResume so that the map reloads
     * when the activity is re-opened
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadMap();
    }


    /**
     * Helper method to start the dialer activity with
     * the dealers phone number
     * @param view - the view that we are currently in
     */
    public void sendToDialer(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    /**
     * Gets the address of a specific location.
     * Background activity to make sure that the geocoder
     * actually works. This is needed because for
     * some reason the geocoder fails sometimes.
     * This method makes sure that it will continue trying
     * to get the address until successful.
     */
    private class GetAddressTask extends AsyncTask<Double, Void, String> {

        @Override
        protected String doInBackground(Double... doubles) {
            double lat = doubles[0];
            double longitude = doubles[1];
            String address = "No Address Found";
            List<Address> addresses = new ArrayList<>();
            while (address.equals("No Address Found")) {
                try {
                    addresses = mGeoCoder.getFromLocation(lat, longitude, 1);
                    address = addresses.get(0).getAddressLine(0) + "\n" + addresses.get(0).getAddressLine(1);
                } catch (IOException e) {
                    Log.d("error", "No Address Found");
                }
                if (addresses.size() != 0) {
                    return address;
                }
            }
            return address;
        }

        protected void onPostExecute(String result) {
            mAddress.setText(result);
            googleMap.clear();
            Marker marker = googleMap.addMarker(mMarkerOptions.snippet(result));
            marker.showInfoWindow();
        }
    }
}
