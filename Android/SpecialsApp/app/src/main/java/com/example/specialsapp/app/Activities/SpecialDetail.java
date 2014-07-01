package com.example.specialsapp.app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.specialsapp.app.AlertDialogs.CustomAlertDialog;
import com.example.specialsapp.app.R;

import org.w3c.dom.Text;


public class SpecialDetail extends Activity {


    private TextView mTitle;
    private TextView mDescription;
    private TextView mOldPrice;
    private TextView mNewPrice;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_detail);
        Bundle extras = getIntent().getExtras();
        mTitle = (TextView)findViewById(R.id.title_text);
        mDescription = (TextView)findViewById(R.id.description);
        mOldPrice = (TextView)findViewById(R.id.price_old);
        mNewPrice = (TextView)findViewById(R.id.price_new);

        mTitle.setText(extras.getString("title"));
        mDescription.setText(extras.getString("description"));
        mOldPrice.setText("$"+extras.getString("oldP"));
        mOldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mNewPrice.setText("$"+extras.getString("newP"));

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Details");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        // Check login status, change menu appropriately
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        boolean status = shared.getBoolean("stored", true);
        if (status) {
            menu.findItem(R.id.action_logout).setVisible(true);
            menu.findItem(R.id.action_login).setVisible(false);
        } else {
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_login).setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor edit = shared.edit();
            edit.putString("User", "");
            edit.putString("Password", "");
            edit.putBoolean("stored", false);
            edit.commit();
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_login).setVisible(true);
            new CustomAlertDialog(this, "Logout", "You have been logged out. You can no longer send contact info to dealers").show();
            return true;
        }
        if (id == R.id.action_login) {
            Intent intent = new Intent(SpecialDetail.this, MainActivity.class);
            startActivity(intent);
        }
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
