package group5.com.barcrawlr.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by georgecrary on 6/15/17.
 */

public class FavoritesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "brewFavs.db";
    private static final int DATABASE_VERSION = 1;

    public FavoritesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_BEERS_TABLE =
                "CREATE TABLE " + FavoritesContract.FavoriteBeers.TABLE_NAME + " (" +
                        FavoritesContract.FavoriteBeers._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoritesContract.FavoriteBeers.COLUMN_BEER_NAME + " TEXT NOT NULL, " +
                        FavoritesContract.FavoriteBeers.COLUMN_DESCRIPTION + " TEXT, " +
                        FavoritesContract.FavoriteBeers.COLUMN_ABV + " TEXT NOT NULL, " +
                        FavoritesContract.FavoriteBeers.COLUMN_STYLE + " INTEGER DEFAULT 0, " +
                        FavoritesContract.FavoriteBeers.COLUMN_IMAGE_URL+ " TEXT, " +
                        FavoritesContract.FavoriteBeers.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                        ");";
        db.execSQL(SQL_CREATE_FAVORITE_BEERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoriteBeers.TABLE_NAME);
        onCreate(db);
    }
}
