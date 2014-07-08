package com.example.specialsapp.app.Activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.specialsapp.app.R;
import com.squareup.picasso.Picasso;

public class SpecialDetail extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_detail);
        Bundle extras = getIntent().getExtras();
        TextView mTitle = (TextView) findViewById(R.id.title_text);
        TextView mDescription = (TextView) findViewById(R.id.description);
        TextView mOldPrice = (TextView) findViewById(R.id.price_old);
        TextView mNewPrice = (TextView) findViewById(R.id.price_new);
        TextView mName = (TextView) findViewById(R.id.price_name);
        ImageView mTitleImage = (ImageView)findViewById(R.id.title_image);

        if (extras != null) {
            mTitle.setText(extras.getString("title"));
            mDescription.setText(extras.getString("description"));
            mOldPrice.setText("$" + extras.getString("oldP"));
            mOldPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mNewPrice.setText("$" + extras.getString("newP"));
            mName.setText(extras.getString("year") + " " + extras.getString("make") + " " + extras.getString("model"));
            Picasso.with(this)
                    .load(extras.getString("imageUrl"))
                    .placeholder(R.drawable.tesla)
                    .resize(335, 600)
                    .into(mTitleImage);
        }

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

    public void submitInfo(View view) {
        Intent intent = new Intent(SpecialDetail.this, MainActivity.class);
        intent.putExtra("submit", true);
        startActivity(intent);
    }
}
