package com.example.rocket.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.rocket.MainActivity;
import com.example.rocket.R;
import com.example.rocket.fragments.FilterContainerFragment;
import com.example.rocket.fragments.MainContainerFragment;

public class FilterActivity extends AppCompatActivity
        implements FilterContainerFragment.OnBackButtonClickListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));// set status background white


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();

        // Begin the Search Fragment
        FilterContainerFragment filterContainerFragment = new FilterContainerFragment();
        fragmentTransaction.add(R.id.filter_container_framelayout,filterContainerFragment).commit();
    }


    @Override
    public void onButtonSelected(String searchWord) {

        Bundle b = new Bundle();
        b.putString("SEARCHTEXT",searchWord);

        final Intent intent = new Intent(this,SearchActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public void onDoneButtonSelected(String searchWord, int unrated,
                                     int oneStar, int twoStar, int threeStar, int fourStar, int fiveStar) {
        Bundle b = new Bundle();
        b.putString("SEARCHTEXT",searchWord);
        b.putInt("UNRATED",unrated);
        b.putInt("ONESTAR",oneStar);
        b.putInt("TWOSTAR",twoStar);
        b.putInt("THREESTAR",threeStar);
        b.putInt("FOURSTAR",fourStar);
        b.putInt("FIVESTAR",fiveStar);

        final Intent intent = new Intent(this,SearchActivity.class);
        intent.putExtras(b);
        startActivity(intent);

    }
}