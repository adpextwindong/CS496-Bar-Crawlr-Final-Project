package group5.com.barcrawlr;

import android.content.Intent;
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

import java.io.IOException;
import java.util.ArrayList;

import group5.com.barcrawlr.utils.BreweryDBUtils;
import group5.com.barcrawlr.utils.NetworkUtils;

/**
 * Created by georgecrary on 6/4/17.
 */

public class BeerSearchActivity extends AppCompatActivity
        implements BeerSearchAdapter.OnBeerItemClickListener, android.support.v4.app.LoaderManager.LoaderCallbacks<String>{

    private static final String TAG = "debug";
    Button mButtonSearch;
    EditText mEditTextSearch;
    RecyclerView mSearchResultsRV;
    ProgressBar mLoadingIndicatorPB;
    TextView mLoadingErrorMessageTV;
    BeerSearchAdapter mBeerSearchAdapter;
    private static final String SEARCH_URL_KEY =
            "BreweryDBSearchURL";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer_search_activity);

        mButtonSearch = (Button) findViewById(R.id.btn_search);
        mEditTextSearch = (EditText) findViewById(R.id.et_search_box);
        mLoadingIndicatorPB = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView) findViewById(R.id.tv_loading_error_message);
        mSearchResultsRV = (RecyclerView) findViewById(R.id.rv_search_results);
        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mBeerSearchAdapter = new BeerSearchAdapter(this);
        mSearchResultsRV.setAdapter(mBeerSearchAdapter);

        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBrewerySearch(mEditTextSearch.getText().toString());
            }
        });

        Bundle loaderArgs = new Bundle();
        loaderArgs.putString(SEARCH_URL_KEY, null);

        getSupportLoaderManager().initLoader(0, loaderArgs, this);
    }

    @Override
    public void onBeerItemClick(BreweryDBUtils.beerDetail beerDetail) {
        Intent intent = new Intent(this, BeerDescriptionActivity.class);
        intent.putExtra(BreweryDBUtils.beerDetail.EXTRA_BEER_ITEM, beerDetail);
        startActivity(intent);
    }

    public void doBrewerySearch(String searchQuery) {
        String beerSearchURL;
        if(searchQuery!=null) {
            StringBuilder searchQueryBuilder = new StringBuilder(searchQuery);
            searchQueryBuilder.insert(0, '*');
            searchQueryBuilder.append('*');
            beerSearchURL = BreweryDBUtils.buildBeerSearchURL(searchQueryBuilder.toString());
        }else {
            beerSearchURL = null;
        }

        Bundle loaderArgs = new Bundle();
        loaderArgs.putString(SEARCH_URL_KEY, beerSearchURL);

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
                    String beerSearchURL = args.getString(SEARCH_URL_KEY);
                    String beerJSON = null;
                    try {
                        Log.d(TAG, "loading results from BreweryDB with url: " + beerSearchURL);
                        beerJSON = NetworkUtils.doHTTPGet(beerSearchURL);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return beerJSON;
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
            ArrayList<BreweryDBUtils.beerDetail> beerDetails = BreweryDBUtils.parseBeerSearchJSON(data);
            mBeerSearchAdapter.updateSearchResults(beerDetails);
        } else {
            mSearchResultsRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "AsyncTaskLoader finished loading");
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
