package com.example.rocketowner.Loader;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class GetCodeLoader extends AsyncTaskLoader<Integer> {

    private String mstringUrl;

    public GetCodeLoader(Context context, String stringUrl) {
        super(context);
        mstringUrl = stringUrl;
    }

    @Nullable
    @Override
    public Integer loadInBackground() {

        int customerCode;
        customerCode =   GetCodeQueryUtils.fetchUrlData(mstringUrl);
        return customerCode;
    }

    @Override
    protected void onStartLoading() {
        Log.e("CHECKPOINT","inside startloading");
        forceLoad();
    }
}
