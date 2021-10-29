package com.example.rocket.loaders;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.rocket.classes.QuestionWord;

import java.util.List;

public class PostAnsQuestionsLoader extends AsyncTaskLoader<String> {

    private String mstringUrl;
    private int mclientId;
    private int mlocationId;
    private List<QuestionWord> mquestionsList;

    public PostAnsQuestionsLoader( Context context,
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

        String feedback = PostAnsQuestionsQueryUtils.fetchUrlData(mstringUrl,mclientId,mlocationId,mquestionsList);
        return feedback;
    }

    @Override
    protected void onStartLoading() {
        Log.e("CHECKPOINT","inside startloading");
        forceLoad();
    }
}
