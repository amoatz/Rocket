package com.example.rocket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// Import the parts needed by the SDK
//import com.auth0.android.Auth0;
//import com.auth0.android.authentication.AuthenticationException;
//import com.auth0.android.callback.Callback;
//import com.auth0.android.provider.WebAuthProvider;
//import com.auth0.android.result.Credentials;

import com.example.rocket.activities.EnterCodeActivity;
import com.example.rocket.activities.FilterActivity;
import com.example.rocket.activities.RegisterActivity;
import com.example.rocket.activities.SearchActivity;
import com.example.rocket.activities.SignInActivity;
import com.example.rocket.fragments.MainContainerFragment;
import com.example.rocket.loaders.PostRatingLoader;
import com.example.rocket.loaders.RegisterLoader;

public class MainActivity extends AppCompatActivity implements MainContainerFragment.OnButtonClickListener,
                                                            MainContainerFragment.onButtonSignInListener,
                                                            MainContainerFragment.onButtonRegisterListener,
                                                            MainContainerFragment.onButtonFilterListener,
                                                            MainContainerFragment.onEnterCodeListener,
                                                    LoaderManager.LoaderCallbacks<Integer> {

    public static int clientId = 0;

    //url
//    public static String URL_REQUEST = "http://10.0.2.2:5000";
    public static String URL_REQUEST = "https://rocket-app-heroku.herokuapp.com";

    public static String USGS_REQUEST_URL_clientid = URL_REQUEST+"/register"; // link for internal server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.white));// set status background white

        // Get the client id form the shared preferences
        SharedPreferences sp = getSharedPreferences("your_prefs", MainActivity.MODE_PRIVATE);
        clientId = sp.getInt("your_int_key", -1);

        if (clientId==-1){
            Log.v("CLIENTID","before loadermanager "+clientId);
            LoaderManager loaderManager = getSupportLoaderManager();
//            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);
        }
        Log.v("CLIENTID","After if cond "+clientId);
        // Fragment configuration
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();

        // Begin the Search Fragment
        MainContainerFragment searchFragment = new MainContainerFragment();
        fragmentTransaction.add(R.id.main_search_container_framelayout,searchFragment).commit();

    }

    public void onButtonSelected(String searchText) {

        Log.v("TEST","In Main Activity listener is working: " + searchText);
//        Toast toast = Toast.makeText(
//                this,
//                "Inside MainActivity "+searchText,
//                Toast.LENGTH_LONG);
//        toast.show();
        Bundle b = new Bundle();
        b.putString("SEARCHTEXT",searchText);

        final Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtras(b);

        startActivity(intent);

//        Button searchButton = (Button) findViewById(R.id.search_button);
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.v("TEST","In Main Activity in onClick listener is working: " + searchText);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onButtonSelectedSignIn() {

        final Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onButtonSelectedRegister() {

        final Intent intent = new Intent(this, RegisterActivity.class);

        Button registerButton = (Button) findViewById(R.id.main_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }


    public void onButtonFilterRegister(){

        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }

    //////// Loaders
    @NonNull
    @Override
    public Loader<Integer> onCreateLoader(int id, @Nullable Bundle args) {
        Log.e("CHECKPOINT","Inside onCreateLoader");
        return new RegisterLoader(this,USGS_REQUEST_URL_clientid);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Integer> loader, Integer data) {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Boolean isConnected = activeNetwork !=null && activeNetwork.isConnected();
        Log.e("CHECKPOINT","isConnected is "+isConnected);

        if (isConnected) {
            Log.v("CLIENTID","Cleint id is "+data);
            clientId=data;

            SharedPreferences sp = getSharedPreferences("your_prefs", MainActivity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("your_int_key", data);
            editor.commit();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Integer> loader) {
        Log.e("CHECKPOINT","Inside onLoaderReset");
    }

    //////////////Preference Code /////////////
    private void setupSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


    }

    @Override
    public void onEnterCodeRegister() {

        final Intent intent = new Intent(this, EnterCodeActivity.class);
        startActivity(intent);
    }
}