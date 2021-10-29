package com.example.rocket.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.rocket.R;
import com.example.rocket.fragments.LocationRatingFragment;
import com.example.rocket.fragments.MainContainerFragment;

public class LocationRatingActivity extends AppCompatActivity
        implements LocationRatingFragment.OnButtonVoteClickListener,
                    LocationRatingFragment.OnBackButtonClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_rating);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));// set status background white

        // Fragment configuration
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();

        // Begin the Fragment
        LocationRatingFragment locationRatingFragment = new LocationRatingFragment();
        fragmentTransaction.add(R.id.location_rating_container_framelayout,locationRatingFragment).commit();
    }

    @Override
    public void onButtonSelected(int locationId, String imageUrl, String locationName, String commentText) {

        Bundle b = new Bundle();
        b.putInt("LOCATIONID",locationId);
        b.putString("LOCATIONIMAGEURL",imageUrl);
        b.putString("LOCATIONNAME", locationName);

        final Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtras(b);

        startActivity(intent);

    }

    @Override
    public void onBackButtonSelected(int locationId, String locationName, String imageUrl) {

        Bundle b = new Bundle();
        b.putInt("LOCATIONID",locationId);
        b.putString("LOCATIONIMAGEURL",imageUrl);
        b.putString("LOCATIONNAME", locationName);

        final Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }
}