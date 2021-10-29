package com.example.rocket.loaders;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.rocket.classes.CommentWord;
import com.example.rocket.classes.LocationNames;

import java.util.ArrayList;

public class CodeCheckLoader extends AsyncTaskLoader<LocationNames> {

    private String mstringUrl;

    public CodeCheckLoader(Context context, String stringUrl) {
        super(context);
        mstringUrl = stringUrl;
    }

    @Nullable
    @Override
    public LocationNames loadInBackground() {

        LocationNames locationNames;
        locationNames =   CodeCheckQueryUtils.fetchUrlData(mstringUrl);
        return locationNames;
    }

    @Override
    protected void onStartLoading() {
//        Log.e("CHECKPOINT","inside startloading");
        forceLoad();
    }
}
