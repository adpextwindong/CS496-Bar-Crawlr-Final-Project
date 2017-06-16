package group5.com.barcrawlr;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import group5.com.barcrawlr.utils.BreweryDBUtils;
import group5.com.barcrawlr.utils.FavoritesContract;
import group5.com.barcrawlr.utils.FavoritesDBHelper;

/**
 * Created by aidan on 6/12/17.
 */

public class BarDescriptionActivity extends AppCompatActivity {
    TextView mBarNameTV;
    BreweryDBUtils.barDetail barItem;
    ImageView mSearchResultBookmarkIV;
    SQLiteDatabase mDB;

    private static final String TAG = BarDescriptionActivity.class.getSimpleName();

    boolean mIsBookmarked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_description);

        mSearchResultBookmarkIV = (ImageView)findViewById(R.id.iv_beer_search_result_bookmark);
        mBarNameTV = (TextView) findViewById(R.id.tv_bar_name);
        FavoritesDBHelper dbHelper = new FavoritesDBHelper(this);
        mDB = dbHelper.getWritableDatabase();


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(BreweryDBUtils.barDetail.EXTRA_BAR_ITEM )) {
            barItem = (BreweryDBUtils.barDetail)intent.getSerializableExtra(
                    BreweryDBUtils.barDetail.EXTRA_BAR_ITEM
            );

            mIsBookmarked = checkSearchResultIsInDB();
            updateBookmarkIconState();
        }

        mBarNameTV.setText(barItem.barName);

        mSearchResultBookmarkIV.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mIsBookmarked = !mIsBookmarked;
                updateSearchResultInDB();
                updateBookmarkIconState();
            }
        });
    }

    private void updateSearchResultInDB() {
        if (mIsBookmarked) {
            addSearchResultToDB();
        } else {
            deleteSearchResultFromDB();
        }
    }

    private void deleteSearchResultFromDB() {
        if (barItem != null) {
            String sqlSelection = FavoritesContract.FavoriteBars.COLUMN_BAR_NAME+ " = ?";
            String[] sqlSelectionArgs = { barItem.barName};
            mDB.delete(FavoritesContract.FavoriteBars.TABLE_NAME, sqlSelection, sqlSelectionArgs);
        }
    }

    private long addSearchResultToDB() {
        if (barItem != null) {
            ContentValues values = new ContentValues();
            values.put(FavoritesContract.FavoriteBars.COLUMN_BAR_NAME, barItem.barName);
            values.put(FavoritesContract.FavoriteBars.COLUMN_DESCRIPTION, barItem.description);
            values.put(FavoritesContract.FavoriteBars.COLUMN_ESTABLISHED, barItem.established);
            values.put(FavoritesContract.FavoriteBars.COLUMN_IMAGE_URL, barItem.imageUrl);
            values.put(FavoritesContract.FavoriteBars.COLUMN_IS_ORGANIC, barItem.isOrganic);
            values.put(FavoritesContract.FavoriteBars.COLUMN_WEBSITE_URL, barItem.websiteUrl);

            return mDB.insert(FavoritesContract.FavoriteBars.TABLE_NAME, null, values);
        } else {
            return -1;
        }
    }

    private void updateBookmarkIconState() {
        if (mIsBookmarked) {
            mSearchResultBookmarkIV.setImageResource(R.drawable.ic_bookmark_black_48dp);
        } else {
            mSearchResultBookmarkIV.setImageResource(R.drawable.ic_bookmark_border_black_48dp);
        }
    }

    private boolean checkSearchResultIsInDB() {
        boolean isInDB = false;
        if (barItem != null) {
            String sqlSelection = FavoritesContract.FavoriteBars.COLUMN_BAR_NAME + " = ?";
            String[] sqlSelectionArgs = {barItem.barName};
            Cursor cursor = mDB.query(
                    FavoritesContract.FavoriteBars.TABLE_NAME,
                    null,
                    sqlSelection,
                    sqlSelectionArgs,
                    null,
                    null,
                    null
            );
            isInDB = cursor.getCount() > 0;
            cursor.close();
        }
        return isInDB;
    }

    @Override
    protected void onDestroy() {
        mDB.close();
        super.onDestroy();
    }
}
