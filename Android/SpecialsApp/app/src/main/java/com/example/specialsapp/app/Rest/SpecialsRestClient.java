package com.example.specialsapp.app.Rest;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;

/**
 * Centralizes all rest call to the api using Android Async HTTP Library
 * Used by Pinterest/Instagram and based off of Apache
 * <p/>
 * Created by brownea on 6/21/14.
 */

public class SpecialsRestClient {
    // TODO another address that should be in a properties file
    private static final String IP = "192.168.168.235";
    private static final String BASE_URL = "http://" + IP + ":8080/v1/specials/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setProxy(IP, 8080);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void get(String url, JsonHttpResponseHandler responseHandler) {
        client.setProxy(IP, 8080);
        client.get(getAbsoluteUrl(url), responseHandler);
    }

    public static void post(Context context, String url, StringEntity entity, String type, AsyncHttpResponseHandler responseHandler) {
        client.setProxy(IP, 8080);
        client.post(context, getAbsoluteUrl(url), entity, type, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        System.out.println(BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }
}
