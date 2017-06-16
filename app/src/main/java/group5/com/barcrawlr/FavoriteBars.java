package group5.com.barcrawlr;

/**
 * Created by georgecrary on 6/4/17.
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import group5.com.barcrawlr.utils.BreweryDBUtils;
import group5.com.barcrawlr.utils.FavoritesContract;
import group5.com.barcrawlr.utils.FavoritesDBHelper;

public class FavoriteBars extends AppCompatActivity implements BarSearchAdapter.OnBarItemClickListener {

    private String TAG = FavoriteBars.class.getSimpleName();
    private RecyclerView mSavedSearchResultsRV;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_beer);

        FavoritesDBHelper dbHelper = new FavoritesDBHelper(this);
        mDB = dbHelper.getReadableDatabase();

        ArrayList<BreweryDBUtils.barDetail> favoriteBeersList = getAllSavedBars();
        BarSearchAdapter adapter = new BarSearchAdapter(this);
        adapter.updateSearchResults(favoriteBeersList);

        mSavedSearchResultsRV = (RecyclerView)findViewById(R.id.rv_saved_beer_search_results);
        mSavedSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSavedSearchResultsRV.setHasFixedSize(true);
        mSavedSearchResultsRV.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        mDB.close();
        super.onDestroy();
    }

    @Override
    public void onBarItemClick(BreweryDBUtils.barDetail barDetail) {
        Intent intent = new Intent(this, BarDescriptionActivity.class);
        intent.putExtra(BreweryDBUtils.barDetail.EXTRA_BAR_ITEM, barDetail);
        startActivity(intent);
    }

    public ArrayList<BreweryDBUtils.barDetail> getAllSavedBars() {
        Cursor cursor = mDB.query(
                FavoritesContract.FavoriteBars.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavoritesContract.FavoriteBars.COLUMN_TIMESTAMP + " DESC"
        );

        ArrayList<BreweryDBUtils.barDetail> searchResultsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            BreweryDBUtils.barDetail result = new BreweryDBUtils.barDetail();

            result.barName = cursor.getString(
                    cursor.getColumnIndex(FavoritesContract.FavoriteBars.COLUMN_BAR_NAME)
            );

            result.description = cursor.getString(
                    cursor.getColumnIndex(FavoritesContract.FavoriteBars.COLUMN_DESCRIPTION)
            );

            result.established = cursor.getString(
                    cursor.getColumnIndex(FavoritesContract.FavoriteBars.COLUMN_ESTABLISHED)
            );

            String organicString = cursor.getString(
                    cursor.getColumnIndex(FavoritesContract.FavoriteBars.COLUMN_IS_ORGANIC)
            );

            result.isOrganic = organicString;

            //TODO FIX?
            /*
            if(organicString.equals("True")){
                result.isOrganic = true;
            }else{
                result.isOrganic = false;
            }*/

            result.imageUrl = cursor.getString(
                    cursor.getColumnIndex(FavoritesContract.FavoriteBars.COLUMN_IMAGE_URL)
            );

            result.websiteUrl = cursor.getString(
                    cursor.getColumnIndex(FavoritesContract.FavoriteBars.COLUMN_WEBSITE_URL)
            );

            searchResultsList.add(result);
        }
        cursor.close();

        return searchResultsList;
    }
}