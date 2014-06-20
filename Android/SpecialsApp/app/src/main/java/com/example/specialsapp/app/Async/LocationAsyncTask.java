package com.example.specialsapp.app.Async;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.specialsapp.app.Models.Dealer;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by brownea on 6/12/14.
 * Builds HttpPost and sends JSON for location information to api - returns the auth result code
 */
public class LocationAsyncTask extends AsyncTask<Double, Void, ArrayList<Dealer>> {

    private JSONArray request;
    private HttpGet httpGet;
    private JSONObject auth;
    private JSONObject content;
    private JSONObject distance;
    private HttpHost proxy;
    private HttpClient httpClient;
    private JSONObject location;
    private JSONObject point;
    private JSONArray coord;
    private ArrayList<Dealer> dealers;
    private Dealer dealer;

    public LocationAsyncTask() {
        auth = new JSONObject();
        httpClient = new DefaultHttpClient();
        proxy = new HttpHost("det-brownea-m", 8080, "http");
        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        location = new JSONObject();
        content = new JSONObject();
        distance = new JSONObject();
        point = new JSONObject();
        coord = new JSONArray();
        dealer = new Dealer();
        dealers = new ArrayList<Dealer>();
    }

    @Override
    protected ArrayList<Dealer> doInBackground(Double... params) {
        Double latitude = params[0];
        Double longitude = params[1];
        int authCode = 0;

        httpGet = new HttpGet(
                "http://det-brownea-m:8080/v1/specials/dealers?lng=" + longitude + "&lat=" + latitude);

        String message = location.toString();
        System.out.println(message.toString());

        // Make http request
        try {
            HttpResponse response = httpClient.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();

            for (String line = null; (line = reader.readLine()) != null; ) {
                builder.append(line).append("/n");
            }

            System.out.println(builder.toString());

            request = new JSONArray(builder.toString());

            for (int i = 0; i < request.length(); i++){
                content = (JSONObject)((JSONObject)request.get(i)).get("content");
                distance = (JSONObject)((JSONObject)request.get(i)).get("distance");

                dealer.setName(content.getString("name"));
                dealer.setCity(content.getString("city"));
                dealer.setState(content.getString("state"));
                System.out.println(i);
                System.out.println(content.get("name"));
                //dealer.setNumSpecials(content.getInt("numSpecials"));
                //dealer.setDistanceFrom(distance.getDouble());
                dealers.add(i, dealer);
            }

            Log.d("HTTP Response: ", response.toString());

            if (response.getStatusLine().getStatusCode() == 200) {
                authCode = 1;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dealers;
    }
}
