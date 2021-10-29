package com.example.rocketowner.Loader;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.rocketowner.Classes.QuestionWord;

import java.util.ArrayList;

public class GetQuestionsLoader extends AsyncTaskLoader<ArrayList<QuestionWord>> {

    private String mstringUrl;

    public GetQuestionsLoader(Context context, String stringUrl) {
        super(context);
        mstringUrl = stringUrl;
    }

    @Nullable
    @Override
    public ArrayList<QuestionWord> loadInBackground() {

        ArrayList<QuestionWord> questionWords = new ArrayList<QuestionWord>();
        questionWords = GetQuestionsQueryUtils.fetchUrlData(mstringUrl);
        return questionWords;
    }

    @Override
    protected void onStartLoading() {
        Log.e("CHECKPOINT","inside startloading");
        forceLoad();
    }
}
