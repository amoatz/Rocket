package com.example.rocket.loaders;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class RegisterLoader extends AsyncTaskLoader<Integer> {

    private String mstringUrl;

    public RegisterLoader(@NonNull Context context, String stringUrl) {
        super(context);
        mstringUrl = stringUrl;
    }

    @Nullable
    @Override
    public Integer loadInBackground() {

        int feedback = RegisterQueryUtils.fetchUrlData(mstringUrl);
        return feedback;
    }

    @Override
    protected void onStartLoading() {
        Log.e("CHECKPOINT","inside startloading");
        forceLoad();
    }
}
