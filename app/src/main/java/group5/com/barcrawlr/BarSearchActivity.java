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

public class BarSearchActivity extends AppCompatActivity
        implements BarSearchAdapter.OnBarItemClickListener, android.support.v4.app.LoaderManager.LoaderCallbacks<String>{

    private static final String TAG = "debug";
    Button mButtonSearch;
    EditText mEditTextSearch;
    RecyclerView mSearchResultsRV;
    ProgressBar mLoadingIndicatorPB;
    TextView mLoadingErrorMessageTV;
    BarSearchAdapter mBarSearchAdapter;
    private static final String SEARCH_URL_KEY =
            "BreweryDBSearchURL";

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

        mBarSearchAdapter = new BarSearchAdapter(this);
        mSearchResultsRV.setAdapter(mBarSearchAdapter);

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
    public void onBarItemClick(BreweryDBUtils.barDetail barDetail) {
        Intent intent = new Intent(this, BarDescriptionActivity.class);
        intent.putExtra(BreweryDBUtils.barDetail.EXTRA_BAR_ITEM, barDetail);
        startActivity(intent);
    }

    public void doBrewerySearch(String searchQuery) {
        String barSearchURL;
        if(searchQuery!=null) {
            StringBuilder searchQueryBuilder = new StringBuilder(searchQuery);
            searchQueryBuilder.insert(0, '*');
            searchQueryBuilder.append('*');
            barSearchURL = BreweryDBUtils.buildBarSearchURL(searchQueryBuilder.toString());
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
            ArrayList<BreweryDBUtils.barDetail> barDetails = BreweryDBUtils.parseBarSearchJSON(data);
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
}
