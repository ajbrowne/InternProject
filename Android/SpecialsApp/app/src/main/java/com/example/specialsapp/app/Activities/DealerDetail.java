package com.example.specialsapp.app.Activities;

import android.app.ActionBar;
import android.app.Activity;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DealerDetail extends BaseActivity {

    private GoogleMap googleMap;
    private String phoneNumber;
    private TextView mAddress;
    private TextView mName;
    private TextView mPhone;
    private TextView mDistance;
    private Geocoder mGeoCoder;
    private MarkerOptions mMarkerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_detail);
        Bundle extras = getIntent().getExtras();
        mGeoCoder = new Geocoder(this, Locale.getDefault());
        init();
        float ZOOM = 13;
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
        new GetAddressTask().execute(extras.getDouble("lat"), extras.getDouble("long"));
        mName.setText(extras.getString("name"));
        mPhone.setText("555-555-5555");
        mDistance.setText(extras.getString("dist") + " miles away");
        phoneNumber = "5555555555";
        configureMap(extras, ZOOM);
    }


    private void init() {
        mAddress = (TextView) findViewById(R.id.dealer_address);
        mName = (TextView) findViewById(R.id.dealer_name);
        mPhone = (TextView) findViewById(R.id.dealer_phone);
        mDistance = (TextView) findViewById(R.id.dealer_distance);
    }

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initilizeMap() {
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

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }


    public void sendToDialer(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private class GetAddressTask extends AsyncTask<Double, Void, String> {

        @Override
        protected String doInBackground(Double... doubles) {
            double lat = doubles[0];
            double longitude = doubles[1];
            String address = "No Address Found";
            List<Address> addresses = new ArrayList<Address>();
            while (true) {
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
        }

        protected void onPostExecute(String result) {
            mAddress.setText(result);
            googleMap.clear();
            Marker marker = googleMap.addMarker(mMarkerOptions.snippet(result));
            marker.showInfoWindow();
        }
    }
}
