package com.example.rocketowner.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.rocketowner.Loader.GetCodeLoader;
import com.example.rocketowner.MainActivity;
import com.example.rocketowner.R;

public class MainContainerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Integer> {

    Button generateCodeButton;
    Button settingButton;
    Button loginButton;
    EditText locationIdEditText;
    ProgressBar progressBar;
    Boolean loginFlag=true; //true:not login_yet --   false:login success
    int locationId;

    // url
//    public static final String USGS_REQUEST_URL = "http://10.0.2.2:5000/CodeGenerator/"; // link for internal server
    public static final String USGS_REQUEST_URL = MainActivity.URL_REQUEST+"/CodeGenerator/";

    // Generate Code Button
    OnButtonClickListener mCallback;
    public interface OnButtonClickListener{
        void onButtonSelected (int code);
        void onSettingSelected();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mCallback = (OnButtonClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "Must implement OnButtonClickListener");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_container,container,false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_spinner_main);

        generateCodeButton = (Button) rootView.findViewById(R.id.generate_code_button);
        generateCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.restartLoader(1, null, MainContainerFragment.this);
            }
        });

        settingButton = (Button) rootView.findViewById(R.id.settings_main_button);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onSettingSelected();
            }
        });

        locationIdEditText = (EditText) rootView.findViewById(R.id.loation_id_main_editText);

        loginButton = (Button) rootView.findViewById(R.id.signIn_main_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginFlag){
                    locationId = Integer.parseInt(locationIdEditText.getText().toString());
                    loginFlag=false;
                    loginButton.setText(R.string.logout);
                    locationIdEditText.setVisibility(View.INVISIBLE);
                    settingButton.setVisibility(View.VISIBLE);
                    generateCodeButton.setVisibility(View.VISIBLE);
                } else {
                    locationId = 0;
                    loginFlag=true;
                    loginButton.setText(R.string.login);
                    locationIdEditText.setVisibility(View.VISIBLE);
                    settingButton.setVisibility(View.INVISIBLE);
                    generateCodeButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Get the Data intent
        Intent intent = getActivity().getIntent();
        Log.v("LOCATIONID","true or false :"+intent.hasExtra("LocationId"));
        if (intent.hasExtra("LocationId")){
            Bundle b = intent.getExtras();
            locationId = b.getInt("LocationId");
        }


        if (locationId==0){
            loginFlag=true;
            loginButton.setText(R.string.login);
            locationIdEditText.setVisibility(View.VISIBLE);
            settingButton.setVisibility(View.INVISIBLE);
            generateCodeButton.setVisibility(View.INVISIBLE);
        } else {
            loginFlag=false;
            loginButton.setText(R.string.logout);
            locationIdEditText.setVisibility(View.INVISIBLE);
            settingButton.setVisibility(View.VISIBLE);
            generateCodeButton.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    ///////////////Loaders/////////////////////////////////////////////////

    @NonNull
    @Override
    public Loader<Integer> onCreateLoader(int id, @Nullable Bundle args) {

        String USGS_REQUEST_URL_locationId = USGS_REQUEST_URL + locationId;
        return new GetCodeLoader(getActivity(),USGS_REQUEST_URL_locationId);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Integer> loader, Integer data) {

        Log.v("CODE", "The data is "+data);
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Boolean isConnected = activeNetwork !=null && activeNetwork.isConnected();
        if(isConnected){
            if(data==0){
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(),"Please Login",Toast.LENGTH_LONG).show();
            } else {
                mCallback.onButtonSelected(data);
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Integer> loader) {

    }
}
