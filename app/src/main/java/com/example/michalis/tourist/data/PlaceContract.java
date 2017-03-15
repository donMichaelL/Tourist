package com.example.michalis.tourist.data;

import android.provider.BaseColumns;

import com.google.android.gms.location.places.Place;

/**
 * Created by michalis on 3/15/2017.
 */

public class PlaceContract {
    private PlaceContract(){}

    public static final class PlaceEntry implements BaseColumns {
        public static final String PLACE_TABLE_NAME = "PLACES";
        public static final String PLACE_ADDRESS = "address";
        public static final String PLACE_ATTRIBUTIONS = "attributions";
        public static final String PLACE_ID = "place_id";
        public static final String PLACE_LATITUDE = "latitude";
        public static final String PLACE_LONGITUDE = "longitude";
        public static final String PLACE_LOCALE = "locale";
        public static final String PLACE_PHONE_NUMBER = "phone_number";
        //TODO Create a seperate table
        public static final String PLACE_TYPE = "type";
        public static final String PLACE_PRICE_LEVEL = "level";
        public static final String PLACE_RATING = "rating";
        //TODO Not creating column for viewport
        public static final String PLACE_WEBSITE = "web_site";
    }
}
