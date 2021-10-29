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
import com.example.rocket.fragments.CommentsContainerFragment;
import com.example.rocket.fragments.MainContainerFragment;

public class CommentsActivity extends AppCompatActivity implements CommentsContainerFragment.OnBackButtonClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));// set status background white

        // Fragment configuration
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();

        // Begin the Search Fragment
        CommentsContainerFragment commentsContainerFragment = new CommentsContainerFragment();
        fragmentTransaction.add(R.id.comments_container_framelayout,commentsContainerFragment).commit();
    }


    @Override
    public void onButtonSelected(int locationId,
                                 String locationName,
                                 String imageUrl) {

        Bundle b = new Bundle();
        b.putInt("LOCATIONID",locationId);
        b.putString("LOCATIONNAME",locationName);
        b.putString("LOCATIONIMAGEURL",imageUrl);

        final Intent intent = new Intent(this,LocationActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }
}