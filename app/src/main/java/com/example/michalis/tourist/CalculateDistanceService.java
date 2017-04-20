package com.example.michalis.tourist;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by michalis on 3/20/2017.
 */

public class CalculateDistanceService extends IntentService {
    private static final String TAG = CalculateDistanceService.class.getName();

    public CalculateDistanceService() {
        super("CalculateDistanceService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String uri = intent.getStringExtra(MainActivity.NEW_PLACE_URI);


    }
}
