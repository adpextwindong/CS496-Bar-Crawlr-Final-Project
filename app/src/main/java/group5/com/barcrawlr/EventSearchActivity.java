package group5.com.barcrawlr;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import group5.com.barcrawlr.utils.BreweryDBUtils;
import group5.com.barcrawlr.utils.NetworkUtils;

/**
 * Created by georgecrary on 6/4/17.
 */

public class EventSearchActivity extends AppCompatActivity
        implements EventSearchAdapter.OnEventItemClickListener, android.support.v4.app.LoaderManager.LoaderCallbacks<String>{

    private static final String TAG = "debug";
    Button mButtonSearch;
    EditText mEditTextSearch;
    RecyclerView mSearchResultsRV;
    ProgressBar mLoadingIndicatorPB;
    TextView mLoadingErrorMessageTV;
    EventSearchAdapter mEventSearchAdapter;
    private static final String SEARCH_URL_KEY =
            "BreweryDBSearchURL";

    public static String eventJSON = null;
    public static String eventSearchURL = null;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_search_activity);

        mButtonSearch = (Button) findViewById(R.id.btn_search);
        mEditTextSearch = (EditText) findViewById(R.id.et_search_box);
        mLoadingIndicatorPB = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView) findViewById(R.id.tv_loading_error_message);
        mSearchResultsRV = (RecyclerView) findViewById(R.id.rv_search_results);
        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mEventSearchAdapter = new EventSearchAdapter(this);
        mSearchResultsRV.setAdapter(mEventSearchAdapter);


        eventSearchURL = Uri.parse("http://api.brewerydb.com/v2/").buildUpon()
                .appendQueryParameter("key", "00018739ed2662a0c01fb436c996e404")
                .appendQueryParameter("order", "startDate")
                .appendQueryParameter("sort", "ASC")
                .appendQueryParameter("year", "2017")
                .appendPath("events")
                .build()
                .toString();


        InputStream is = getResources().openRawResource(R.raw.events);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        eventJSON = writer.toString();


        mSearchResultsRV.setVisibility(View.VISIBLE);
        ArrayList<BreweryDBUtils.eventDetail> eventDetails = BreweryDBUtils.parseEventSearchJSON(eventJSON);

        /*int index = 0;
        for (int i = 0; i <= eventDetails.size(); i++) {
            String element = eventDetails.get(i).date;
            if(){

                break;
            }
            index++;
        }

        eventDetails.subList(index, eventDetails.size());*/
        
        mEventSearchAdapter.updateSearchResults(eventDetails);

        Bundle loaderArgs = new Bundle();
        loaderArgs.putString(SEARCH_URL_KEY, null);

        getSupportLoaderManager().initLoader(0, loaderArgs, this);
    }

    @Override
    public void onEventItemClick(BreweryDBUtils.eventDetail eventDetail) {
        Intent intent = new Intent(this, EventDescriptionActivity.class);
        intent.putExtra(BreweryDBUtils.eventDetail.EXTRA_EVENT_ITEM, eventDetail);
        startActivity(intent);
    }

    public void doBrewerySearch(String searchQuery) {
        String eventSearchURL;
        if(searchQuery!=null) {
            StringBuilder searchQueryBuilder = new StringBuilder(searchQuery);
            searchQueryBuilder.insert(0, '*');
            searchQueryBuilder.append('*');
            eventSearchURL = BreweryDBUtils.buildEventSearchURL(searchQueryBuilder.toString());
        }else {
            eventSearchURL = null;
        }

        Bundle loaderArgs = new Bundle();
        loaderArgs.putString(SEARCH_URL_KEY, eventSearchURL);

        getSupportLoaderManager().restartLoader(0, loaderArgs, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String mSearchResultsJSON;

            @Override
            protected void onStartLoading(){
                if (mSearchResultsJSON != null || args.getString(SEARCH_URL_KEY) == null) {
                    Log.d(TAG, "AsyncTaskLoader delivering cached results");
                    deliverResult(mSearchResultsJSON);
                } else {
                    mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                if(args != null)
                {
                    String eventSearchURL = args.getString(SEARCH_URL_KEY);
                    String eventJSON = null;
                    try {
                        Log.d(TAG, "loading results from BreweryDB with url: " + eventSearchURL);
                        eventJSON = NetworkUtils.doHTTPGet(eventSearchURL);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return eventJSON;
                }
                else
                    return null;
            }

            @Override
            public void deliverResult(String data) {
                mSearchResultsJSON = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
        if (data != null) {
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
            /*mSearchResultsRV.setVisibility(View.VISIBLE);
            ArrayList<BreweryDBUtils.eventDetail> eventDetails = BreweryDBUtils.parseEventSearchJSON(data);
            mEventSearchAdapter.updateSearchResults(eventDetails);*/
        } else {
            /*mSearchResultsRV.setVisibility(View.INVISIBLE);*/
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "AsyncTaskLoader finished loading");
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
