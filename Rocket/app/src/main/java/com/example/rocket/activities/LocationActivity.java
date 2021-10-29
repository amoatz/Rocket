package com.example.rocket.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rocket.R;
import com.example.rocket.fragments.LocationContainerFragment;
import com.example.rocket.fragments.SearchContainerFragment;

public class LocationActivity extends AppCompatActivity implements LocationContainerFragment.OnButtonVoteClickListener,
                                                                LocationContainerFragment.OnCommentsButtonClickListener,
                                                                LocationContainerFragment.OnBackButtonClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));// set status background white

        // Fragment configuration
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();

        // Begin the Search Fragment
        LocationContainerFragment searchFragment = new LocationContainerFragment();
        fragmentTransaction.add(R.id.location_container_framelayout,searchFragment).commit();


    }


    public void onButtonSelected(int locationId, String imageUrl, String locationName) {

        Bundle b = new Bundle();
        b.putInt("LOCATIONID",locationId);
        b.putString("LOCATIONIMAGEURL",imageUrl);
        b.putString("LOCATIONNAME", locationName);

        final Intent intent = new Intent(this, LocationRatingActivity.class);
        intent.putExtras(b);

        startActivity(intent);

    }

    @Override
    public void onCommentButton(int locationId, String imageUrl, String locationName) {

        Bundle b = new Bundle();
        b.putInt("LOCATIONID",locationId);
        b.putString("LOCATIONIMAGEURL",imageUrl);
        b.putString("LOCATIONNAME", locationName);
        b.putString("CUSTOMERTYPE","Viewer");
        b.putInt("CUSTOMERCODE",0);

        final Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtras(b);

        startActivity(intent);

    }

    @Override
    public void onButtonSelected(int locationPosition, String searchWord) {

        Bundle b = new Bundle();
        b.putString("SEARCHTEXT",searchWord);

        final Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtras(b);

        startActivity(intent);
    }
}