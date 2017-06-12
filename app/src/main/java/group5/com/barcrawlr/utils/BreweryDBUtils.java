package group5.com.barcrawlr.utils;

import android.net.ParseException;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by aidan on 6/4/17.
 */

public class BreweryDBUtils {

    public static final String BASE_URL = "http://api.brewerydb.com/v2/";
    public static final String KEY_PARAM = "key";
    public static final String NAME_PARAM = "name";
    public static final String BEER_SEARCH_PARAM = "beers/";
    public static final String BREW_SEARCH_PARAM = "breweries/";

    public static final String API_KEY = "00018739ed2662a0c01fb436c996e404";

    public static class beerDetail implements Serializable {
        public String beerName;
        public String abv;
        public String style;
        public String description;
    }

    public static class barDetail implements Serializable {
        public String barName;
        public String description;
    }

    public static String buildBeerSearchURL(String beerName) {

        return Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(KEY_PARAM, API_KEY)
                .appendQueryParameter(NAME_PARAM, beerName)
                .appendPath("beers")
//                .appendQueryParameter(OWM_FORECAST_QUERY_PARAM, forecastLocation)
//                .appendQueryParameter(OWM_FORECAST_UNITS_PARAM, temperatureUnits)
//                .appendQueryParameter(OWM_FORECAST_APPID_PARAM, OWM_FORECAST_APPID)
                .build()
                .toString();
    }

    public static String buildBarSearchURL(String barName) {

        return Uri.parse(BASE_URL).buildUpon()
//                .appendQueryParameter(OWM_FORECAST_QUERY_PARAM, forecastLocation)
//                .appendQueryParameter(OWM_FORECAST_UNITS_PARAM, temperatureUnits)
//                .appendQueryParameter(OWM_FORECAST_APPID_PARAM, OWM_FORECAST_APPID)
                .build()
                .toString();
    }

    //TODO: use gson instead
    public static ArrayList<beerDetail> parseBeerSearchJSON(String beerJSON) {
        try {
            JSONObject beerObj = new JSONObject(beerJSON);
            JSONArray searchResultsItems = beerObj.getJSONArray("data");

            ArrayList<beerDetail> searchResultsList = new ArrayList<beerDetail>();
            for(int i=0; i<searchResultsItems.length(); i++)
            {
                beerDetail searchResult = new beerDetail();
                JSONObject searchResultItem = searchResultsItems.getJSONObject(i);
                searchResult.beerName = searchResultItem.getString("name");

                try {
                    searchResult.abv = searchResultItem.getString("abv");
                } catch (JSONException e) {
                    searchResult.abv = "N/A";
                }

                try{
                    searchResult.style = searchResultItem.getJSONObject("style").getString("shortName");
                } catch (JSONException e) {
                    searchResult.abv = "N/A";
                }

                try{
                    searchResult.description = searchResultItem.getString("description");
                } catch (JSONException e) {
                    searchResult.abv = "N/A";
                }

                searchResultsList.add(searchResult);
            }
            return searchResultsList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
