package group5.com.barcrawlr.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by georgecrary on 6/15/17.
 */

public class FavoritesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "brewFavs.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TAG = FavoritesDBHelper.class.getSimpleName();

    public FavoritesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO GO OVERDEFAULTS
        String SQL_CREATE_FAVORITE_TABLES_BEERS =
                "CREATE TABLE " + FavoritesContract.FavoriteBeers.TABLE_NAME + " (" +
                        FavoritesContract.FavoriteBeers._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoritesContract.FavoriteBeers.COLUMN_BEER_NAME + " TEXT NOT NULL, " +
                        FavoritesContract.FavoriteBeers.COLUMN_DESCRIPTION + " TEXT, " +
                        FavoritesContract.FavoriteBeers.COLUMN_ABV + " TEXT NOT NULL, " +
                        FavoritesContract.FavoriteBeers.COLUMN_STYLE + " INTEGER DEFAULT 0, " +
                        FavoritesContract.FavoriteBeers.COLUMN_IMAGE_URL+ " TEXT, " +
                        FavoritesContract.FavoriteBeers.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                        ");";
        //db.execSQL(SQL_CREATE_FAVORITE_BEERS_TABLE);

        String SQL_CREATE_FAVORITE_TABLES_BARS =
                "CREATE TABLE " + FavoritesContract.FavoriteBars.TABLE_NAME + " (" +
                        FavoritesContract.FavoriteBars._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoritesContract.FavoriteBars.COLUMN_BAR_NAME + " TEXT NOT NULL, " +
                        FavoritesContract.FavoriteBars.COLUMN_DESCRIPTION + " TEXT, " +
                        FavoritesContract.FavoriteBars.COLUMN_ESTABLISHED + " TEXT NOT NULL, " +
                        FavoritesContract.FavoriteBars.COLUMN_IS_ORGANIC + " INTEGER DEFAULT 0, " +
                        FavoritesContract.FavoriteBars.COLUMN_WEBSITE_URL + " TEXT, " +
                        FavoritesContract.FavoriteBars.COLUMN_IMAGE_URL + " TEXT, " +
                        FavoritesContract.FavoriteBars.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                        "); ";

        //Log.d(TAG, "onCreate: ~"+SQL_CREATE_FAVORITE_TABLES+"~");
        db.execSQL(SQL_CREATE_FAVORITE_TABLES_BEERS);
        db.execSQL(SQL_CREATE_FAVORITE_TABLES_BARS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE *");
        //db.execSQL("DROP TABLE " + FavoritesContract.FavoriteBeers.TABLE_NAME + "IF EXISTS " );
        //db.execSQL("DROP TABLE " + FavoritesContract.FavoriteBars.TABLE_NAME + "IF EXISTS " );
        onCreate(db);
    }
}
