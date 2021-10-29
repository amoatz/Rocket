package com.example.rocketowner.Loader;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.rocketowner.Classes.QuestionWord;

import java.util.List;

public class PostQuestionLoader extends AsyncTaskLoader<String> {

    private String mstringUrl;
    private int mclientId;
    private int mlocationId;
    private List<QuestionWord> mquestionsList;

    public PostQuestionLoader(Context context,
                              String stringUrl,
                              int clientId,
                              int locationId,
                              List<QuestionWord> questionWordList) {
        super(context);
        mstringUrl = stringUrl;
        mclientId = clientId;
        mlocationId=locationId;
        mquestionsList = questionWordList;
    }

    @Nullable
    @Override
    public String loadInBackground() {

        String feedback = PostQuestionsQueryUtils.fetchUrlData(mstringUrl,mclientId,mlocationId,mquestionsList);
        return feedback;
    }

    @Override
    protected void onStartLoading() {
        Log.e("CHECKPOINT","inside startloading");
        forceLoad();
    }
}
