package main.Helpers;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by maharb on 6/17/14.
 */
public class JsonHelp {

    public String jsonGen(String response){
        JSONObject returnObj = new JSONObject();
        try {
            returnObj.put("response", response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnObj.toString();
    }
}
