package com.example.michalis.tourist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.michalis.tourist.data.PlaceContract.PlaceEntry;

/**
 * Created by michalis on 3/15/2017.
 */

public class PlaceDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "tourist.db";
    private static final int DB_VERSION = 1;


    public PlaceDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String insertPlaceTable =
                "CREATE TABLE " + PlaceEntry.PLACE_TABLE_NAME + " (" +
                        PlaceEntry._ID + " INTEGER PRIMARY KEY, " +
                        PlaceEntry.PLACE_ADDRESS + " TEXT, " +
                        PlaceEntry.PLACE_ATTRIBUTIONS + " TEXT, " +
                        PlaceEntry.PLACE_ID + " TEXT, " +
                        PlaceEntry.PLACE_LATITUDE + " REAL, " +
                        PlaceEntry.PLACE_LONGITUDE + " REAL, " +
                        PlaceEntry.PLACE_LOCALE + " REAL, " +
                        PlaceEntry.PLACE_PHONE_NUMBER + " TEXT, " +
                        // TODO Change if you create a separate table
                        PlaceEntry.PLACE_TYPE + " INTEGER, " +
                        PlaceEntry.PLACE_PRICE_LEVEL + " REAL, " +
                        PlaceEntry.PLACE_RATING + " REAL, " +
                        PlaceEntry.PLACE_WEBSITE + " TEXT " +
                        ")";

        db.execSQL(insertPlaceTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Think about dropping in production
        String DROP_DATABASE = "DROP TABLE IF EXISTS " + PlaceEntry.PLACE_TABLE_NAME;
        this.onCreate(db);
    }
}
