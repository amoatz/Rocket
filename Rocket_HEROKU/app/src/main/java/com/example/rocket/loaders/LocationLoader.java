package com.example.rocket.loaders;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.rocket.classes.LocationWord;

import java.util.ArrayList;

public class LocationLoader extends AsyncTaskLoader<ArrayList<LocationWord>> {

    private String mstringUrl;

    public LocationLoader(Context context, String stringUrl) {
        super(context);
        mstringUrl = stringUrl;
    }

    @Override
    public ArrayList<LocationWord> loadInBackground() {

        ArrayList<LocationWord> locations = new ArrayList<LocationWord>();
        locations = QueryUtils.fetchUrlData(mstringUrl);
        return locations;
    }

    @Override
    protected void onStartLoading() {
        Log.e("CHECKPOINT","inside startloading");
        forceLoad();
    }
}
