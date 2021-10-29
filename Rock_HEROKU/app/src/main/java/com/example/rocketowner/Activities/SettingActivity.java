package com.example.rocketowner.Activities;

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
import android.widget.Toast;

import com.example.rocketowner.Classes.QuestionWord;
import com.example.rocketowner.Fragment.MainContainerFragment;
import com.example.rocketowner.Fragment.SettingContainerFragment;
import com.example.rocketowner.Loader.PostQuestionLoader;
import com.example.rocketowner.MainActivity;
import com.example.rocketowner.R;

import java.util.List;

public class SettingActivity extends AppCompatActivity implements SettingContainerFragment.OnButtonClickListener,
        LoaderManager.LoaderCallbacks<String> {

    List<QuestionWord> questionWordList;
    int gLocationId;

//    public static String USGS_REQUEST_URL_Location_rating = "http://10.0.2.2:5000/questions"; // link for internal server
    public static String USGS_REQUEST_URL_Location_rating = MainActivity.URL_REQUEST+"/questions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));// set status background white

        // Fragment configuration
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();

        // Begin the Search Fragment
        SettingContainerFragment settingContainerFragment = new SettingContainerFragment();
        fragmentTransaction.add(R.id.setting_container_framelayout,settingContainerFragment).commit();
    }

    @Override
    public void onBackButtonSelected(int locationId) {
        Bundle bundle = new Bundle();
        bundle.putInt("LocationId", locationId);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDoneButtonSelected(List<QuestionWord> questionWordList, int locationId) {

        this.questionWordList = questionWordList;
        this.gLocationId = locationId;
        Bundle bundle = new Bundle();
        bundle.putInt("LOCATIONID", locationId);
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(1, bundle, this);

    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        int locationId = args.getInt("LOCATIONID");
        return new PostQuestionLoader(this,USGS_REQUEST_URL_Location_rating,
                0,locationId,questionWordList);
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
            Bundle bundle = new Bundle();
            bundle.putInt("LocationId", gLocationId);
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Toast.makeText(this,"There is problem in connection",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}