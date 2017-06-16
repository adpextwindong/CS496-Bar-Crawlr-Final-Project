package group5.com.barcrawlr.utils;

import android.provider.BaseColumns;

/**
 * Created by georgecrary on 6/15/17.
 */

public class FavoritesContract {
    private FavoritesContract() {}

    public static class FavoriteBeers implements BaseColumns {
        public static final String TABLE_NAME = "favoriteBeers";
        public static final String COLUMN_BEER_NAME = "beerName";
        public static final String COLUMN_ABV = "abv";
        public static final String COLUMN_STYLE = "style";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE_URL = "imageURL";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

    public static class FavoriteBars implements BaseColumns {
        public static final String TABLE_NAME = "favoriteBars";
        public static final String COLUMN_BAR_NAME = "barName";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_ESTABLISHED = "established";
        public static final String COLUMN_IS_ORGANIC = "isOrganic";
        public static final String COLUMN_WEBSITE_URL = "websiteURL";
        public static final String COLUMN_IMAGE_URL = "imageURL";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
