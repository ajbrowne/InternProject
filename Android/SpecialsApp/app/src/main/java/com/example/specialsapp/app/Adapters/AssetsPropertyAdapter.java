package com.example.specialsapp.app.Adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AssetsPropertyAdapter {
    private Context context;
    private Properties properties;

    public AssetsPropertyAdapter(Context context) {
        this.context = context;
        /**
         * Constructs a new Properties object.
         */
        properties = new Properties();
    }

    public Properties getProperties(String FileName) {
        try {
            /**
             * getAssets() Return an AssetManager instance for your
             * application's package. AssetManager Provides access to an
             * application's raw asset files;
             */
            AssetManager assetManager = context.getAssets();
            /**
             * Open an asset using ACCESS_STREAMING mode. This
             */
            InputStream inputStream = assetManager.open(FileName);
            System.out.println(inputStream.toString());
            /**
             * Loads properties from the specified InputStream,
             */
            properties.load(inputStream);

        } catch (IOException e) {
            Log.e("AssetsPropertyReader", e.toString());
        }
        return properties;

    }

}
