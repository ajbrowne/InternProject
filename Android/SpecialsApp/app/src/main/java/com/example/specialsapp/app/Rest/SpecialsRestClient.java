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
    private static final String IP = "192.168.169.252";
    private static final String BASE_URL = "http://" + IP + ":8080/v1/specials/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * Get request with parameters
     * @param url - url for request
     * @param params - parameters for request
     * @param responseHandler - response handler (can be changed per type of response)
     */
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setProxy(IP, 8080);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    /**
     * Get request without parameters
     * @param url - url for request
     * @param responseHandler - response handler (can be changed per type of response)
     */
    public static void get(String url, JsonHttpResponseHandler responseHandler) {
        client.setProxy(IP, 8080);
        client.get(getAbsoluteUrl(url), responseHandler);
    }

    /**
     * Post request
     * @param context - current context in app
     * @param url - url for request
     * @param entity - entity of request
     * @param type - type of request (i.e application/json)
     * @param responseHandler - reponse handler (can be changed per type of request)
     */
    public static void post(Context context, String url, StringEntity entity, String type, AsyncHttpResponseHandler responseHandler) {
        client.setProxy(IP, 8080);
        client.post(context, getAbsoluteUrl(url), entity, type, responseHandler);
    }

    /**
     * Takes base url and adds /vehicles to url for exm
     * @param relativeUrl
     * @return
     */
    private static String getAbsoluteUrl(String relativeUrl) {
        System.out.println(BASE_URL + relativeUrl);
        return BASE_URL + relativeUrl;
    }
}
