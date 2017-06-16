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
    public static final String POSTAL_CODE = "postalCode";
    public static final String API_KEY = "00018739ed2662a0c01fb436c996e404";

    public static class beerDetail implements Serializable {
        public static final String EXTRA_BEER_ITEM = "group5.com.barcrawlr.utils.BeerItem.SearchResult";
        public String beerName;
        public String abv;
        public String style;
        public String description;
        public String imageUrl;
    }

    public static class barDetail implements Serializable {
        public static final String EXTRA_BAR_ITEM = "group5.com.barcrawlr.utils.BarItem.SearchResult";
        public String barName;
        public String description;
        public String established;
        public String isOrganic;
        public String websiteUrl;
        public String imageUrl;
    }

    public static class eventDetail implements Serializable {
        public static final String EXTRA_EVENT_ITEM = "group5.com.barcrawlr.utils.EventItem.SearchResult";
        public String eventName;
        public String date;
        public String description;
        public String year;
        public String websiteUrl;
        public String imageUrl;
    }

    public static String buildBeerSearchURL(String beerName) {
        return Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(KEY_PARAM, API_KEY)
                .appendQueryParameter(NAME_PARAM, beerName)
                .appendPath("beers")
                .build()
                .toString();
    }

    public static String buildBarSearchURL(String searchTerm, String searchByPref) {

        String URL = "";
        if(searchByPref.equals("zip")){
            URL = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(KEY_PARAM, API_KEY)
                    .appendQueryParameter(POSTAL_CODE, searchTerm)
                    .appendPath("locations")
                    .build()
                    .toString();
        }else{
            URL = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(KEY_PARAM, API_KEY)
                    .appendQueryParameter(NAME_PARAM, "*"+searchTerm+"*")
                    .appendPath("breweries")
                    .build()
                    .toString();
        }

        return URL;
    }

    public static String buildEventSearchURL(String eventName) {
        return Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(KEY_PARAM, API_KEY)
                .appendQueryParameter(NAME_PARAM, eventName)
                .appendPath("events")
                .build()
                .toString();
    }

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
                    searchResult.description = "N/A";
                }

                try{
                    searchResult.imageUrl = searchResultItem.getJSONObject("labels").getString("icon");
                } catch (JSONException e) {
                    searchResult.imageUrl = null;
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

    public static ArrayList<barDetail> parseBarSearchJSON(String barJSON, String searchByPref, String mSearchTerm) {

        boolean searchByName = searchByPref.equals("name");
        //TODO add image handling for POSTAL CODE breweries
        ArrayList<barDetail> searchResultsList;
        try {
            JSONObject beerObj = new JSONObject(barJSON);
            JSONArray searchResultsItems = beerObj.getJSONArray("data");

            searchResultsList = new ArrayList<barDetail>();
            for (int i = 0; i < searchResultsItems.length(); i++) {
                barDetail searchResult = null;
                JSONObject searchResultItem = searchResultsItems.getJSONObject(i);

                if (searchByName) {
                    searchResult = parseBarByNameData(searchResultItem, false);
                    searchResultsList.add(searchResult);
                } else {
                    searchResult = parseBarByLocationsPostalCodeData(searchResultItem, mSearchTerm);

                    searchResultsList.add(searchResult);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        return searchResultsList;
    }


    private static barDetail parseBarByLocationsPostalCodeData(JSONObject searchResultItem, String mSearchTerm) {
        barDetail searchResult = new barDetail();
        String postalCode = null;

        try {
            postalCode = searchResultItem.getString("postalCode");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(postalCode != null && postalCode.equals(mSearchTerm)){
            JSONObject brewObj = null;
            try {
                brewObj = searchResultItem.getJSONObject("brewery");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(brewObj != null){
                searchResult = parseBarByNameData(brewObj, true);

            }else{
                searchResult = null;
            }
        }else{
            searchResult = null;
        }

        return searchResult;
    }

    private static barDetail parseBarByNameData(JSONObject searchResultItem, boolean fromLocationsEndpoint) {
        barDetail searchResult = new barDetail();

        try {
            searchResult.barName = searchResultItem.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            searchResult.websiteUrl = searchResultItem.getString("website");
        } catch (JSONException e) {
            searchResult.websiteUrl = "N/A";
        }

        try {
            searchResult.isOrganic = searchResultItem.getString("isOrganic");
            /*
            if(temp == "Y")
                searchResult.isOrganic = true;
            else
                searchResult.isOrganic = false;
            */
        } catch (JSONException e) {
            searchResult.isOrganic = "N/A";
        }

        try {
            searchResult.established = searchResultItem.getString("established");
        } catch (JSONException e) {
            searchResult.established = "N/A";
        }

        if (!fromLocationsEndpoint) {
            try {
                searchResult.imageUrl = searchResultItem.getJSONObject("images").getString("icon");
            } catch (JSONException e) {
                searchResult.imageUrl = "N/A";
            }
        } else{
            //TODO FINISH THIS. GET IMAGE URL FROM OTHER API ENDPOINT IDK...
            searchResult.imageUrl = null;
        }


        try{
            searchResult.description = searchResultItem.getString("description");
        } catch (JSONException e) {
            searchResult.description = "N/A";
        }

        return searchResult;
    }

    public static ArrayList<eventDetail> parseEventSearchJSON(String eventJSON) {
        try {
            JSONObject eventObj = new JSONObject(eventJSON);
            JSONArray searchResultsItems = eventObj.getJSONArray("data");

            ArrayList<eventDetail> searchResultsList = new ArrayList<eventDetail>();
            for(int i=0; i<searchResultsItems.length(); i++)
            {
                eventDetail searchResult = new eventDetail();
                JSONObject searchResultItem = searchResultsItems.getJSONObject(i);
                searchResult.eventName = searchResultItem.getString("name");

                try {
                    searchResult.date = searchResultItem.getString("startDate");
                } catch (JSONException e) {
                    searchResult.date = "N/A";
                }

                try{
                    searchResult.description = searchResultItem.getString("description");
                } catch (JSONException e) {
                    searchResult.description = "N/A";
                }

                try{
                    searchResult.year = searchResultItem.getString("year");
                } catch (JSONException e) {
                    searchResult.year = "N/A";
                }

                try {
                    searchResult.websiteUrl = searchResultItem.getString("website");
                } catch (JSONException e) {
                    searchResult.websiteUrl = "N/A";
                }

                try{
                    searchResult.imageUrl = searchResultItem.getJSONObject("labels").getString("icon");
                } catch (JSONException e) {
                    searchResult.imageUrl = null;
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
