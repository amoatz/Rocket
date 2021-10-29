package com.example.rocketowner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.rocketowner.Activities.CodeActivity;
import com.example.rocketowner.Activities.SettingActivity;
import com.example.rocketowner.Fragment.MainContainerFragment;

public class MainActivity extends AppCompatActivity implements MainContainerFragment.OnButtonClickListener {

    public int locationId=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));// set status background white

        // Fragment configuration
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();

        // Begin the Search Fragment
        MainContainerFragment searchFragment = new MainContainerFragment();
        fragmentTransaction.add(R.id.main_container_framelayout,searchFragment).commit();
    }

    @Override
    public void onButtonSelected(int code) {
        Bundle b = new Bundle();
        b.putInt("CUSTOMERCODE",code);
        b.putInt("LocationId", locationId);
        Intent intent = new Intent(this, CodeActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    public void onSettingSelected() {

        Bundle bundle = new Bundle();
        bundle.putInt("LocationId", locationId);
        Log.v("CHECK","Main The locationid is "+locationId);
        Intent intent = new Intent(this, SettingActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}