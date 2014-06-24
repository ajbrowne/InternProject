package com.example.specialsapp.app.Rest;
import android.content.Context;

import com.loopj.android.http.*;

import org.apache.http.entity.StringEntity;

/**
 *
 * Centralizes all rest call to the api using Android Async HTTP Library
 * Used by Pinterest/Instagram and based off of Apache
 *
 * Created by brownea on 6/21/14.
 */

public class SpecialsRestClient {
    private static final String BASE_URL = "http://det-maharb-m:8080/v1/specials/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setProxy("det-maharb-m", 8080);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context context, String url, StringEntity entity, String type, AsyncHttpResponseHandler responseHandler) {
        client.setProxy("det-maharb-m", 8080);
        client.post(context, getAbsoluteUrl(url), entity, type, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        System.out.println(BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }
}
