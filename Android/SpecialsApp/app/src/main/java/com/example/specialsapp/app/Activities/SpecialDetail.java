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


public class SpecialDetail extends BaseActivity {


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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
