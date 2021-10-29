package com.example.rocket.loaders;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.rocket.classes.CommentWord;
import com.example.rocket.classes.LocationWord;

import java.util.ArrayList;

public class CommentLoader extends AsyncTaskLoader<ArrayList<CommentWord>> {

    private String mstringUrl;

    public CommentLoader(Context context, String stringUrl) {
        super(context);
        mstringUrl = stringUrl;
    }

    @Nullable
    @Override
    public ArrayList<CommentWord> loadInBackground() {

        ArrayList<CommentWord> commentWords = new ArrayList<CommentWord>();
        commentWords = CommentQueryUtils.fetchUrlData(mstringUrl);
        return commentWords;
    }

    @Override
    protected void onStartLoading() {
        Log.e("CHECKPOINT","inside startloading");
        forceLoad();
    }
}
