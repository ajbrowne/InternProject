package com.example.specialsapp.app.GPS;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.example.specialsapp.app.Models.LocationObject;

import java.io.IOException;
import java.util.List;

/**
 * GPS class used to get location at will. Also includes helper methods for finding the
 * distance to a dealer and checking location settings.
 */
public class GPS extends Service implements LocationListener {

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BTW_UPDATES = 1000 * 60;
    private static final double defaultLocation = 0.0;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;
    private Context context;

    public GPS(Context context) {
        this.context = context;
        getLocation();
    }

    /**
     * Called when the GPS is created. Gets location based off of either network or location
     * services and sets the class variables that can be retrieved.
     *
     * @return - the location
     */
    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            // get GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // get network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled) {
                showSettingsAlert();
            } else {
                this.canGetLocation = true;
                // Get location from network provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BTW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // Get lat long if GPS is enabled
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BTW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS enabled", "GPS enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    // These could be implemented if desired
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    /**
     * Used to get the latitude retrieved when the GPS object is created
     *
     * @return - the latitude as a double
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    /**
     * Used to get the longitude retrieved when the GPS object is created
     *
     * @return - the longitude as a double
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will launch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Checks shared preferences to see whether a set zip code or current location
     * is desired for the app.
     *
     * @return - custom object with lat/long
     */
    public LocationObject checkLocationSettings() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String zip = sharedPreferences.getString("zip_code", "");
        boolean useLocation = sharedPreferences.getBoolean("use_location", false);
        LocationObject location = new LocationObject();
        // Used GeoCoder based off of a zip code or else use current location
        if ((!zip.equals("") && !zip.equals("Enter Zip Code")) && !useLocation) {
            location = getLoc(zip);
        } else {
            location.setLatitude(getLatitude());
            location.setLongitude(getLongitude());
        }
        return location;
    }

    /**
     * Uses the GeoCoder to retrieve a lat/long based off of zip code
     *
     * @param zip - zip code
     * @return - object containing lat/long
     */
    private LocationObject getLoc(String zip) {
        final Geocoder geocoder = new Geocoder(context);
        LocationObject location = new LocationObject(defaultLocation, defaultLocation);
        try {
            List<Address> addresses = geocoder.getFromLocationName(zip, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                location.setLatitude(address.getLatitude());
                location.setLongitude(address.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }
}
