package control.util;

import android.location.Address;
import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by doba on 2/25/17.
 */

public class AppStatic {

    public static Address address;
    public static Location location;

    public static JSONObject getSendingData(String name){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("lat", AppStatic.location.getLatitude());
            jsonObject.put("lng", AppStatic.location.getLongitude());
            jsonObject.put("country_code", address.getCountryCode());
            jsonObject.put("country_name", address.getCountryName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
