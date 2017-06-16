package group5.com.barcrawlr;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import group5.com.barcrawlr.utils.BreweryDBUtils;
import group5.com.barcrawlr.utils.FavoritesContract;
import group5.com.barcrawlr.utils.FavoritesDBHelper;

/**
 * Created by RheaMae on 6/15/2017.
 */

public class EventDescriptionActivity extends AppCompatActivity{
    TextView mEventNameTV;
    TextView mEventDescriptionTV;
    TextView mEventWebsiteTV;
    TextView mEventDateTV;

    ImageView mEventImageIV;

    BreweryDBUtils.eventDetail eventItem;

    //ImageView mSearchResultBookmarkIV;
    //SQLiteDatabase mDB;

    private static final String TAG = EventDescriptionActivity.class.getSimpleName();

    //boolean mIsBookmarked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_description);

        //mSearchResultBookmarkIV = (ImageView)findViewById(R.id.iv_beer_search_result_bookmark);

        mEventNameTV = (TextView) findViewById(R.id.tv_event_name);
        mEventDescriptionTV = (TextView) findViewById(R.id.tv_event_description);
        mEventDateTV = (TextView) findViewById(R.id.tv_event_date);
        mEventWebsiteTV = (TextView) findViewById(R.id.tv_event_website);

        mEventImageIV = (ImageView) findViewById(R.id.iv_event_image);

        /*FavoritesDBHelper dbHelper = new FavoritesDBHelper(this);
        mDB = dbHelper.getWritableDatabase();*/

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(BreweryDBUtils.eventDetail.EXTRA_EVENT_ITEM )) {
            eventItem = (BreweryDBUtils.eventDetail)intent.getSerializableExtra(
                    BreweryDBUtils.eventDetail.EXTRA_EVENT_ITEM
            );

            //mIsBookmarked = checkSearchResultIsInDB();
            //updateBookmarkIconState();
        }

        mEventNameTV.setText(eventItem.eventName);
        mEventDescriptionTV.setText(eventItem.description);
        mEventWebsiteTV.setText(eventItem.websiteUrl);
        mEventDateTV.setText(eventItem.date);

        /*mEventImageIV.setText(eventItem.imageUrl); Need to download image*/

        /*
        mSearchResultBookmarkIV.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mIsBookmarked = !mIsBookmarked;
                updateSearchResultInDB();
                updateBookmarkIconState();
            }
        });
        */
    }

    /*
    private void updateSearchResultInDB() {
        if (mIsBookmarked) {
            addSearchResultToDB();
        } else {
            deleteSearchResultFromDB();
        }
    }*/

    /*
    private void deleteSearchResultFromDB() {
        if (eventItem != null) {
            String sqlSelection = FavoritesContract.FavoriteEvents.COLUMN_EVENT_NAME+ " = ?";
            String[] sqlSelectionArgs = { eventItem.eventName};
            mDB.delete(FavoritesContract.FavoriteEvents.TABLE_NAME, sqlSelection, sqlSelectionArgs);
        }
    }*/

    /*
    private long addSearchResultToDB() {
        if (eventItem != null) {
            ContentValues values = new ContentValues();
            values.put(FavoritesContract.FavoriteEvents.COLUMN_EVENT_NAME, eventItem.eventName);
            values.put(FavoritesContract.FavoriteEvents.COLUMN_DESCRIPTION, eventItem.description);
            values.put(FavoritesContract.FavoriteEvents.COLUMN_ESTABLISHED, eventItem.established);
            values.put(FavoritesContract.FavoriteEvents.COLUMN_IMAGE_URL, eventItem.imageUrl);
            values.put(FavoritesContract.FavoriteEvents.COLUMN_IS_ORGANIC, eventItem.isOrganic);
            values.put(FavoritesContract.FavoriteEvents.COLUMN_WEBSITE_URL, eventItem.websiteUrl);

            return mDB.insert(FavoritesContract.FavoriteEvents.TABLE_NAME, null, values);
        } else {
            return -1;
        }
    }*/

    /*
    private void updateBookmarkIconState() {
        if (mIsBookmarked) {
            mSearchResultBookmarkIV.setImageResource(R.drawable.ic_bookmark_black_48dp);
        } else {
            mSearchResultBookmarkIV.setImageResource(R.drawable.ic_bookmark_border_black_48dp);
        }
    }*/

    /*private boolean checkSearchResultIsInDB() {
        boolean isInDB = false;
        if (eventItem != null) {
            String sqlSelection = FavoritesContract.FavoriteEvents.COLUMN_EVENT_NAME + " = ?";
            String[] sqlSelectionArgs = {eventItem.eventName};
            Cursor cursor = mDB.query(
                    FavoritesContract.FavoriteEvents.TABLE_NAME,
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
    }*/

    /*@Override
    protected void onDestroy() {
        mDB.close();
        super.onDestroy();

    }*/
}
