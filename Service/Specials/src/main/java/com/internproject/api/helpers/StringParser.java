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
            add("2008");
            add("2007");
            add("2006");
            add("2005");
        }
    };
    private static List makes = new ArrayList() {
        {
            add("acura");
            add("aston martin");
            add("audi");
            add("bentley");
            add("bmw");
            add("buick");
            add("cadillac");
            add("chevrolet");
            add("chevy");
            add("chrysler");
            add("dodge");
            add("ferrari");
            add("fiat");
            add("ford");
            add("gmc");
            add("honda");
            add("hyundai");
            add("infiniti");
            add("jaguar");
            add("jeep");
            add("kia");
            add("lamborghini");
            add("land rover");
            add("lexus");
            add("lincoln");
            add("lotus");
            add("maserati");
            add("mazda");
            add("mclaren");
            add("mercedes-benz");
            add("mini");
            add("mitsubishi");
            add("nissan");
            add("pontiac");
            add("porsche");
            add("ram");
            add("rolls-royce");
            add("scion");
            add("smart");
            add("subaru");
            add("tesla");
            add("toyota");
            add("volkswagen");
            add("volvo");
        }
    };
    private static List models = new ArrayList() {
        {
            add("2500");
            add("3500");
            add("astro");
            add("avalanche");
            add("aveo");
            add("blazer");
            add("camaro");
            add("captiva sport");
            add("cavalier");
            add("classic");
            add("cobalt");
            add("colorado");
            add("corvette");
            add("corvette stingray");
            add("cruze");
            add("equinox");
            add("express");
            add("van");
            add("hhr");
            add("impala");
            add("impala limited");
            add("lumina");
            add("malibu");
            add("malibu classic");
            add("malibu hybrid");
            add("malibu maxx");
            add("metro");
            add("monte carlo");
            add("pickup");
            add("prizm");
            add("s-10");
            add("silverado");
            add("sonic");
            add("spark");
            add("spark ev");
            add("ss");
            add("ssr");
            add("suburban");
            add("tahoe");
            add("tahoe hybrid");
            add("tracker");
            add("trailblazer");
            add("trailblazer ext");
            add("traverse");
            add("uplander");
            add("venture");
            add("volt");
            add("acadia");
            add("canyon");
            add("envoy");
            add("envoy xl");
            add("envoy xuv");
            add("jimmy");
            add("safari");
            add("savana");
            add("sierra");
            add("sonoma");
            add("terrain");
            add("yukon");
            add("yukon hybrid");
            add("yukon xl");
            add("ats");
            add("catera");
            add("cts");
            add("deville");
            add("dts");
            add("eldorado");
            add("elr");
            add("escalade");
            add("escalade esv");
            add("escalade ext");
            add("escalade hybrid");
            add("seville");
            add("srx");
            add("sts");
            add("xlr");
            add("xts");
            add("century");
            add("enclave");
            add("encore");
            add("lacrosse");
            add("lesabre");
            add("lucerne");
            add("park avenue");
            add("rainier");
            add("regal");
            add("rendezvous");
            add("terraza");
            add("verano");
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
