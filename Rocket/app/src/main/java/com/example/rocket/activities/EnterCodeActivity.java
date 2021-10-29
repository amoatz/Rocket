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
import com.example.rocket.fragments.EnterCodeContainerFragment;
import com.example.rocket.fragments.MainContainerFragment;

public class EnterCodeActivity extends AppCompatActivity implements EnterCodeContainerFragment.OnBackButtonClickListener,
                                                                    EnterCodeContainerFragment.OnDoneClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));// set status background white

        // Fragment configuration
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();

        // Begin the Search Fragment
        EnterCodeContainerFragment enterCodeContainerFragment = new EnterCodeContainerFragment();
        fragmentTransaction.add(R.id.enter_code_container_framelayout,enterCodeContainerFragment).commit();

    }

    @Override
    public void onBackButtonSelected() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDoneButtonSelected(String locationName, int locationId, String imageUrl, int customerCode) {
        Bundle b = new Bundle();
        b.putInt("LOCATIONID",locationId);
        b.putString("LOCATIONNAME",locationName);
        b.putString("LOCATIONIMAGEURL",imageUrl);
        b.putString("CUSTOMERTYPE","Customer");
        b.putInt("CUSTOMERCODE",customerCode);
        Intent intent;

        if (customerCode<10000){
            intent = new Intent(this,LocationRatingActivity.class);
        } else {
            intent = new Intent(this,QuestionsLocationRatingActivity.class);
        }

        intent.putExtras(b);
        startActivity(intent);
    }
}