package com.example.specialsapp.app.Async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.specialsapp.app.Models.Dealer;
import com.example.specialsapp.app.Models.Special;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by brownea on 6/12/14.
 * Builds HttpPost and sends JSON for location information to api - returns the auth result code
 */
public class SpecialAsyncTask extends AsyncTask<String, Void, ArrayList<Special>> {

    private JSONArray request;
    private HttpGet httpGet;
    private JSONObject auth;
    private JSONObject content;
    private HttpHost proxy;
    private HttpClient httpClient;
    private JSONObject location;
    private JSONObject point;
    private JSONArray coord;
    private ArrayList<Special> specials;
    private Special special;

    public SpecialAsyncTask() {
        auth = new JSONObject();
        httpClient = new DefaultHttpClient();
        proxy = new HttpHost("det-brownea-m", 8080, "http");
        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        location = new JSONObject();
        content = new JSONObject();
        point = new JSONObject();
        coord = new JSONArray();
        special = new Special();
        specials = new ArrayList<Special>();
    }

    @Override
    protected ArrayList<Special> doInBackground(String... params) {
        String dealer = params[0];
        int authCode = 0;

        httpGet = new HttpGet(
                "http://det-brownea-m:8080/v1/specials/special?dealer=" + "Summit%20City%20Chevrolet");

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
                content = (JSONObject)((JSONObject)request.get(i));
                special.setTitle(content.getString("title"));
                special.setDealer(content.getString("dealer"));
                special.setDescription(content.getString("description"));
                special.setType(content.getString("type"));
                specials.add(special);
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
        return specials;
    }
}
