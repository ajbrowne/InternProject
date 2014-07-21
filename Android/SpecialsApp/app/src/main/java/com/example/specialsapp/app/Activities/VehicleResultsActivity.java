package com.example.specialsapp.app.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.example.specialsapp.app.Fragments.VehicleResultsFragment;
import com.example.specialsapp.app.R;

/**
 * Activity holding VehicleResultsFragment to allow for BaseVehicleFragment to be a thing.
 */
public class VehicleResultsActivity extends BaseActivity {

    /**
     * onCreate called when activity is created. This gets all needed views and carries
     * out necessary initializations.
     * @param savedInstanceState -
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_results);

        VehicleResultsFragment fragment = new VehicleResultsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.vehicle_results_activity, fragment);
        fragmentTransaction.commit();
    }

    /**
     * onCreateOptionsMenu gets the correct xml for the menu among other setup.
     * @param menu - the menu for the activity
     * @return - true upon success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * onOptionsSelected handles action bar behavior for an activity.
     * @param item - the selected item
     * @return - true upon success
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
