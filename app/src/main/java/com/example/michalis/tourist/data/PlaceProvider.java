package com.example.michalis.tourist.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.michalis.tourist.data.PlaceContract.PlaceEntry;

import java.net.URI;

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
        // GET ONE PLACE WITH PLACE_ID not _ID
        uriMatcher.addURI(PlaceContract.AUTHORITY, PlaceEntry.PLACE_TABLE_NAME + "/*", GET_ONE_PLACE);
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
            case GET_ONE_PLACE:
                placesCursor = placeDBHelper.getReadableDatabase().query(
                        PlaceEntry.PLACE_TABLE_NAME,
                        projection,
                        PlaceEntry.PLACE_ID + " = ?",
                        new String[]{uri.getPathSegments().get(1)},
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
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case GET_ALL_PLACES:
                long id = placeDBHelper.getWritableDatabase().insert(
                        PlaceEntry.PLACE_TABLE_NAME, null, values);
                if (id>0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return ContentUris.withAppendedId(PlaceEntry.URI_PLACE_BASE, id);
                } else {
                    throw new SQLException("Failed to insert");
                }
            default:
                throw new UnsupportedOperationException("Unknown Uri");

        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = URI_MATCHER.match(uri);
        int numberRowsDeleted;
        switch (match) {
            case GET_ONE_PLACE:
                numberRowsDeleted = placeDBHelper.getWritableDatabase().delete(
                        PlaceEntry.PLACE_TABLE_NAME,
                        PlaceEntry.PLACE_ID + " = ?",
                        new String[] {uri.getPathSegments().get(1)});
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri");
        }
        if (numberRowsDeleted !=0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numberRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
