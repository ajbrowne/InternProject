package com.example.specialsapp.app.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import com.example.specialsapp.app.Fragments.DealerResultsFragment;
import com.example.specialsapp.app.R;
/**
 * Displays the results of a dealer search that is done based on the make of
 * cars that the dealer sells.
 */
public class DealerResultsActivity extends BaseActivity {

    /**
     * onCreate called when activity is created. This gets all needed views and carries
     * out necessary initializations.
     * @param savedInstanceState -
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_results);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Dealer Results");

        DealerResultsFragment dealerResultsFragment = new DealerResultsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.results_container, dealerResultsFragment);
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
