package com.example.michalis.tourist.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.michalis.tourist.data.PlaceContract.PlaceEntry;
/**
 * Created by michalis on 3/15/2017.
 */

public class PlaceProvider extends ContentProvider {

    private static final int GET_ALL_PLACES = 157;
    private static final int GET_ONE_PLACE = 750;
    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    private PlaceDBHelper placeDBHelper;

    @Override
    public boolean onCreate() {
        placeDBHelper = new PlaceDBHelper(getContext());
        return true;
    }

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PlaceContract.AUTHORITY, PlaceEntry.PLACE_TABLE_NAME, GET_ALL_PLACES);
        uriMatcher.addURI(PlaceContract.AUTHORITY, PlaceEntry.PLACE_TABLE_NAME + "/#", GET_ONE_PLACE);
        return uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = URI_MATCHER.match(uri);
        Cursor placesCursor;
        switch (match) {
            case GET_ALL_PLACES:
                placesCursor = placeDBHelper.getReadableDatabase().query(
                        PlaceEntry.PLACE_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri");
        }
        placesCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return placesCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
