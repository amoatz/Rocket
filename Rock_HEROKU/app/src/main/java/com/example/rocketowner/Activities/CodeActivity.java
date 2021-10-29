package com.example.rocketowner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.rocketowner.Fragment.CodeContainerFragment;
import com.example.rocketowner.Fragment.MainContainerFragment;
import com.example.rocketowner.MainActivity;
import com.example.rocketowner.R;

public class CodeActivity extends AppCompatActivity implements CodeContainerFragment.OnButtonClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white));// set status background white

        // Fragment configuration
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();

        // Begin the Search Fragment
        CodeContainerFragment searchFragment = new CodeContainerFragment();
        fragmentTransaction.add(R.id.code_container_framelayout,searchFragment).commit();
    }

    @Override
    public void onButtonSelected(int locationId) {
        Bundle bundle = new Bundle();
        bundle.putInt("LocationId", locationId);
        Intent intent = new Intent (this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}