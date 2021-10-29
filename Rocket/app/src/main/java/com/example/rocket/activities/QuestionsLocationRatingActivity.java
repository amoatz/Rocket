package com.example.rocket.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rocket.MainActivity;
import com.example.rocket.R;
import com.example.rocket.classes.QuestionWord;
import com.example.rocket.fragments.MainContainerFragment;
import com.example.rocket.fragments.QuestionsLocationRatingFragment;
import com.example.rocket.loaders.PostAnsQuestionsLoader;

import java.util.List;

public class QuestionsLocationRatingActivity extends AppCompatActivity
        implements QuestionsLocationRatingFragment.OnBackButtonClickListener,
        LoaderManager.LoaderCallbacks<String> {

    List<QuestionWord> finalQuestionWordList;
    ProgressBar progressBar;

    public static String USGS_REQUEST_URL_Location_rating = "http://10.0.2.2:5000/questionsVote"; // link for internal server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_location_rating);
        progressBar = (ProgressBar) findViewById(R.id.loading_spinner_questions_activity);
        progressBar.setVisibility(View.INVISIBLE);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));// set status background white

        // Fragment configuration
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();

        // Begin the Search Fragment
        QuestionsLocationRatingFragment questionsLocationRatingFragment = new QuestionsLocationRatingFragment();
        fragmentTransaction.add(R.id.questions_location_rating_container_framelayout,questionsLocationRatingFragment).commit();

    }

    @Override
    public void onButtonSelected() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onButtonDone(List<QuestionWord> questionWordList,int locationId) {
        progressBar.setVisibility(View.VISIBLE);
        finalQuestionWordList = questionWordList;
        LoaderManager loaderManager = getSupportLoaderManager();
        Bundle b = new Bundle();
        b.putInt("LOCATIONID",locationId);
        loaderManager.initLoader(1, b, this);
    }

    //////////// Loaders //////////////////////
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {

        int locationId = args.getInt("LOCATIONID");
        return new PostAnsQuestionsLoader(this,USGS_REQUEST_URL_Location_rating,
                MainActivity.clientId,locationId,finalQuestionWordList);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        Log.e("CHECKPOINT","Inside onLoadFinished");
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Boolean isConnected = activeNetwork !=null && activeNetwork.isConnected();
        Log.e("CHECKPOINT","isConnected is "+isConnected);

        if (isConnected) {
            Log.v("RATING",data);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this,"There is problem in connection",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}