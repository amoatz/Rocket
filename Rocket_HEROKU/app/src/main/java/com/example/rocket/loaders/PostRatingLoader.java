package com.example.rocket.loaders;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class PostRatingLoader extends AsyncTaskLoader<String> {

    private String mstringUrl;
    private int mclientId;
    private int mvoteValue;
    private int mlocationId;
    private String mCommentText;

    public PostRatingLoader(@NonNull Context context,
                            String stringUrl,
                            int clientId,
                            int voteValue,
                            int locationId,
                            String commentText) {
        super(context);
        mstringUrl = stringUrl;
        mclientId = clientId;
        mvoteValue=voteValue;
        mlocationId=locationId;
        mCommentText=commentText;
    }

    @Nullable
    @Override
    public String loadInBackground() {

        String feedback = PostLoaderQueryUtils.fetchUrlData(mstringUrl,mclientId,mvoteValue,mlocationId,mCommentText);
        return feedback;
    }

    @Override
    protected void onStartLoading() {
        Log.e("CHECKPOINT","inside startloading");
        forceLoad();
    }
}
