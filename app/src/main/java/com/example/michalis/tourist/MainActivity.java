package com.example.michalis.tourist;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.michalis.tourist.data.PlaceDBHelper;
import com.facebook.stetho.Stetho;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.example.michalis.tourist.data.PlaceContract.PlaceEntry;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private static final int PLACE_PICKER_REQUEST = 1;
    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO Remove in production
        Stetho.initializeWithDefaults(this);
        placeAdapter = new PlaceAdapter();

        recyclerView  = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(placeAdapter);

        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(PlaceEntry.URI_PLACE_BASE,
                null,
                null,
                null,
                null);

        placeAdapter.swapCursor(cursor);


//        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//            String i = cursor.getString(cursor.getColumnIndex(PlaceEntry.PLACE_NAME));
//            Log.d("HELLO", i);
//        }
//        cursor.close();
//
//        Uri newUri = PlaceEntry.URI_PLACE_BASE.buildUpon().appendPath("ChIJN6W-X_VYwokRTqwcBnTw1Uk").build();
//        Cursor oneCursor = resolver.query(newUri, null, null, null, null);
//
//        Log.d(TAG, "REEE "+ String.valueOf(oneCursor.getCount()));
//
//        oneCursor.close();
//
//        resolver.delete(newUri,null,null);
    }




    public void showLocations(View view) {
        Log.d(TAG, "Show locations button is clicked");
        PlacePicker.IntentBuilder placeBuilder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(placeBuilder.build(this), PLACE_PICKER_REQUEST);
            //TODO Create Exception Messages
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place selectedPlace = PlacePicker.getPlace(this, data);
                Log.d(TAG, "Place selected: " + selectedPlace.getName());
                Log.d(TAG, "Address" + selectedPlace.getAddress());
                Log.d(TAG, "Address" + selectedPlace.getAttributions());
                Log.d(TAG, "Address" + selectedPlace.getId());
                Log.d(TAG, "Address" + selectedPlace.getLatLng().latitude);
                Log.d(TAG, "Address" + selectedPlace.getLatLng().longitude);
                Log.d(TAG, "Locale --> " + selectedPlace.getLocale());
                Log.d(TAG, "Address" + selectedPlace.getPhoneNumber());
                Log.d(TAG, "Address" + selectedPlace.getPlaceTypes());
                Log.d(TAG, "Address" + selectedPlace.getPriceLevel());
                Log.d(TAG, "Rating" + String.valueOf(selectedPlace.getRating()));
                Log.d(TAG, "Address" + selectedPlace.getViewport());
                Log.d(TAG, "Address" + selectedPlace.getWebsiteUri());
                saveinDB(selectedPlace);
            }
        }
    }

    private void saveinDB(Place selectedPlace) {
        PlaceDBHelper placeDBHelper = new PlaceDBHelper(this);
        ContentValues values = new ContentValues();
        values.put(PlaceEntry.PLACE_ADDRESS, selectedPlace.getAddress().toString());
        values.put(PlaceEntry.PLACE_NAME, selectedPlace.getName().toString());
        if (selectedPlace.getAttributions() != null) {
            values.put(PlaceEntry.PLACE_ATTRIBUTIONS, selectedPlace.getAttributions().toString());
        }
        values.put(PlaceEntry.PLACE_ID, selectedPlace.getId().toString());
        values.put(PlaceEntry.PLACE_LATITUDE, selectedPlace.getLatLng().latitude);
        values.put(PlaceEntry.PLACE_LONGITUDE, selectedPlace.getLatLng().longitude);
        if (selectedPlace.getLocale() != null) {
            values.put(PlaceEntry.PLACE_LOCALE, selectedPlace.getLocale().toString());
        }
        values.put(PlaceEntry.PLACE_PHONE_NUMBER, selectedPlace.getPhoneNumber().toString());
        // TODO check if exception
        values.put(PlaceEntry.PLACE_TYPE, selectedPlace.getPlaceTypes().get(0));
        values.put(PlaceEntry.PLACE_PRICE_LEVEL, selectedPlace.getPriceLevel());
        values.put(PlaceEntry.PLACE_RATING, selectedPlace.getRating());
        values.put(PlaceEntry.PLACE_WEBSITE, selectedPlace.getWebsiteUri().toString());
        ContentResolver resolver = getContentResolver();
        resolver.insert(PlaceEntry.URI_PLACE_BASE, values);
    }
}
