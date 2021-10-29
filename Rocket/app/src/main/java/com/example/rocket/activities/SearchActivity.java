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

import com.example.rocket.MainActivity;
import com.example.rocket.R;
import com.example.rocket.classes.LocationWord;
import com.example.rocket.fragments.SearchContainerFragment;
import com.example.rocket.loaders.LocationLoader;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity
        implements SearchContainerFragment.OnItemClickListener, SearchContainerFragment.OnBackButtonClickListener,
                    SearchContainerFragment.OnFilterButtonClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));// set status background white


        // Fragment configuration
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();

        // Begin the Search Fragment
        SearchContainerFragment searchFragment = new SearchContainerFragment();
        fragmentTransaction.add(R.id.search_container_framelayout,searchFragment).commit();

//        getSystemService(Context.CONNECTIVITY_SERVICE)

    }

    @Override
    public void onItemSelected(int itemId, int locationId,String imageUrl, String locationName) {

        Bundle b = new Bundle();
        b.putInt("LOCATIONID",locationId);
        b.putString("LOCATIONNAME",locationName);
        b.putString("LOCATIONIMAGEURL",imageUrl);

        final Intent intent = new Intent(this,LocationActivity.class);
        intent.putExtras(b);
        startActivity(intent);

    }

    @Override
    public void onBackButtonSelected() {

        final Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFilterButtonSelected(String searchWord) {

        Bundle b = new Bundle();
        b.putString("SEARCHTEXT",searchWord);

        final Intent intent = new Intent(this,FilterActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }
}