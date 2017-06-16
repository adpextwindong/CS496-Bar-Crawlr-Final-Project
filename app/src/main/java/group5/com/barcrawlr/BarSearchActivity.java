package group5.com.barcrawlr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import group5.com.barcrawlr.utils.BreweryDBUtils;
import group5.com.barcrawlr.utils.NetworkUtils;

/**
 * Created by georgecrary on 6/4/17.
 */

public class BarSearchActivity extends AppCompatActivity
        implements BarSearchAdapter.OnBarItemClickListener, LoaderManager.LoaderCallbacks<String>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "debug";
    Button mButtonSearch;
    EditText mEditTextSearch;
    RecyclerView mSearchResultsRV;
    ProgressBar mLoadingIndicatorPB;
    TextView mLoadingErrorMessageTV;
    BarSearchAdapter mBarSearchAdapter;

    private SharedPreferences mSharedPreferences;

    private static final String SEARCH_URL_KEY =
            "BreweryDBSearchURL";
    private String mSearchTerm;

    Toast mInvalidZipToast = null;
    Toast mNullSearchToastZip = null;
    Toast mNullSearchToastName = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_search_activity);

        mButtonSearch = (Button) findViewById(R.id.btn_search);
        mEditTextSearch = (EditText) findViewById(R.id.et_search_box);
        mLoadingIndicatorPB = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView) findViewById(R.id.tv_loading_error_message);
        mSearchResultsRV = (RecyclerView) findViewById(R.id.rv_search_results);
        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mInvalidZipToast = Toast.makeText(this, "Invalid Zip Code. Has to be 5 digits long", Toast.LENGTH_SHORT);
        mNullSearchToastName = Toast.makeText(this, "HINT: No results? Try doing any empty search! Alternatively try search by zip in the settings!", Toast.LENGTH_LONG);
        mNullSearchToastZip = Toast.makeText(this, "Oh no! There are no bars in your area.", Toast.LENGTH_LONG);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        updateSearchByHint();

        mBarSearchAdapter = new BarSearchAdapter(this);
        mSearchResultsRV.setAdapter(mBarSearchAdapter);

        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchBy = mSharedPreferences.getString(getString(R.string.pref_searchby_key),getString(R.string.pref_searchby_default));
                if(searchBy.equals("zip")){
                    String queryTerm = mEditTextSearch.getText().toString();
                    Integer intParse = null;
                    try{
                        intParse = Integer.parseInt(queryTerm);
                    }catch(java.lang.NumberFormatException e){
                        intParse = null;
                    }
                    if(intParse != null && queryTerm.length() == 5){
                        doBrewerySearch(queryTerm);
                    }else{
                        mInvalidZipToast.show();
                    }
                }else{
                    doBrewerySearch(mEditTextSearch.getText().toString());
                }
            }
        });

        Bundle loaderArgs = new Bundle();
        loaderArgs.putString(SEARCH_URL_KEY, null);

        getSupportLoaderManager().initLoader(0, loaderArgs, this);
    }

    private void updateSearchByHint() {
        String searchByPref = mSharedPreferences.getString(getString(R.string.pref_searchby_key),getString(R.string.pref_searchby_default));

        String hintPrefix = "Search by bar ";
        String hintSuffix = "...";
        if(searchByPref.equals("name")){
            mEditTextSearch.setHint(hintPrefix + "name" + hintSuffix);
        }else{
            mEditTextSearch.setHint(hintPrefix + "zip code" + hintSuffix);
        }

        //TODO IMPLEMENT SORTING DATA RESULTS BY DISTANCE?

    }

    @Override
    public void onBarItemClick(BreweryDBUtils.barDetail barDetail) {
        Intent intent = new Intent(this, BarDescriptionActivity.class);
        intent.putExtra(BreweryDBUtils.barDetail.EXTRA_BAR_ITEM, barDetail);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.searchby, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void doBrewerySearch(String searchQuery) {
        String barSearchURL;
        if(searchQuery!=null) {
            StringBuilder searchQueryBuilder = new StringBuilder(searchQuery);
            //searchQueryBuilder.insert(0, '*');
            //searchQueryBuilder.append('*');

            //NOTE THIS DEPENDS ON THE PREFERENCES AS GLOBAL STATE.
            String searchByPref = mSharedPreferences.getString(getString(R.string.pref_searchby_key),getString(R.string.pref_searchby_default));

            mSearchTerm = searchQueryBuilder.toString();
            barSearchURL = BreweryDBUtils.buildBarSearchURL(searchQueryBuilder.toString(),searchByPref);
        }else {
            barSearchURL = null;
        }

        Bundle loaderArgs = new Bundle();
        loaderArgs.putString(SEARCH_URL_KEY, barSearchURL);

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
                    String barSearchURL = args.getString(SEARCH_URL_KEY);
                    String barJSON = null;
                    try {
                        Log.d(TAG, "loading results from BreweryDB with url: " + barSearchURL);
                        barJSON = NetworkUtils.doHTTPGet(barSearchURL);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return barJSON;
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
            mSearchResultsRV.setVisibility(View.VISIBLE);

            String searchByPref = mSharedPreferences.getString(getString(R.string.pref_searchby_key),getString(R.string.pref_searchby_default));
            ArrayList<BreweryDBUtils.barDetail> barDetails = BreweryDBUtils.parseBarSearchJSON(data, searchByPref, mSearchTerm);

            if(barDetails == null){
                if(searchByPref.equals("zip")){
                    mNullSearchToastZip.show();
                }else{
                    mNullSearchToastName.show();
                }
            }
            mBarSearchAdapter.updateSearchResults(barDetails);
        } else {
            mSearchResultsRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "AsyncTaskLoader finished loading");
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    //ON NO RESULTS, DO TOAST TO HINT DO NULL SEARCH
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_searchby_key))){
            updateSearchByHint();
        }
    }
}
