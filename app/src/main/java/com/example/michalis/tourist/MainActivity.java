package com.example.michalis.tourist;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.michalis.tourist.data.PlaceDBHelper;
import com.facebook.stetho.Stetho;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.example.michalis.tourist.data.PlaceContract.PlaceEntry;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, PlaceAdapter.LongListItemClickListerer {
    private static final String TAG = MainActivity.class.getName();

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int ID_PLACE_LOADER = 843;
    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ContentResolver resolver;
    int mPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO Remove in production
        Stetho.initializeWithDefaults(this);

        placeAdapter = new PlaceAdapter(this);
        recyclerView  = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresher);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(placeAdapter);
        resolver = getContentResolver();

        getSupportLoaderManager().initLoader(ID_PLACE_LOADER, null, this);


        /**
         * Swipe to refresh
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSupportLoaderManager().restartLoader(ID_PLACE_LOADER, null, MainActivity.this);
            }
        });
        /**
         * Swipe to delete
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    String id = (String) viewHolder.itemView.getTag();
                    Uri newUri = PlaceEntry.URI_PLACE_BASE.buildUpon().appendPath(id).build();
                    resolver.delete(newUri, null, null);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    Toast.makeText(MainActivity.this, "HELLO", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);

    }




    public void startPlacePickerUI(View view) {
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
                saveInDB(selectedPlace);
            }
        }
    }

    private void saveInDB(Place selectedPlace) {
        //TODO check when no internet connection
        //TODO check for places with null options
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

        // TODO use asynch task
        resolver.insert(PlaceEntry.URI_PLACE_BASE, values);
    }

    @Override
    public void onLongListItemClickListener(String name) {
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_PLACE_LOADER:
                return new CursorLoader(this, PlaceEntry.URI_PLACE_BASE, null, null, null, null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        swipeRefreshLayout.setRefreshing(false);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        recyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) placeAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        placeAdapter.swapCursor(null);
    }


}
