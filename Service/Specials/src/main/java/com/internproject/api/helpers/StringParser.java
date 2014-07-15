package com.internproject.api.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maharb on 6/26/14.
 */
public class StringParser {

    private static List years = new ArrayList() {
        {
            add("2014");
            add("2013");
            add("2012");
            add("2011");
            add("2010");
            add("2009");
        }
    };
    private static List makes = new ArrayList() {
        {
            add("chevy");
            add("chevrolet");
            add("cadillac");
            add("caddy");
            add("vw");
            add("volkswagen");
            add("mini");
            add("bmw");
            add("pontiac");
        }
    };
    private static List models = new ArrayList() {
        {
            add("malibu");
            add("impala");
            add("silverado");
            add("cts");
            add("vts");
            add("ats");
            add("cooper");
            add("cooper s");
            add("clubman");
            add("jetta");
            add("golf");
            add("gti");
            add("428");
            add("aztek");
        }
    };


    public static String[] parseString(String input) {
        String delims = "[ ]+";
        String[] tokens = input.split(delims);
        return tokens;
    }

    public static HashMap<String, String> mapValues(String[] tokens) {
        HashMap<String, String> parsed = new HashMap<String, String>();
        for (String token : tokens) {
            if (years.contains(token)) {
                parsed.put("year", token);
            } else if (makes.contains(token.toLowerCase())) {
                parsed.put("make", token);
            } else if (models.contains(token.toLowerCase())) {
                parsed.put("model", token);
            } else {
                if (parsed.get("model") != null) {
                    parsed.put("model", parsed.get("model").concat(" " + token));
                }
            }
        }
        return parsed;
    }
}
