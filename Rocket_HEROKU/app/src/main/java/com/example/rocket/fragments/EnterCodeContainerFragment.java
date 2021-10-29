package com.example.rocket.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.rocket.MainActivity;
import com.example.rocket.R;
import com.example.rocket.classes.LocationNames;
import com.example.rocket.loaders.CodeCheckLoader;
import com.example.rocket.loaders.LocationLoader;

public class EnterCodeContainerFragment extends Fragment implements LoaderManager.LoaderCallbacks<LocationNames> {

    EditText enterCodeEditText;
    Button doneButton;
    ImageView backButtonImageView;
    int thecode;

    int locationId;
    String locationName;
    String imageUrl;
    ProgressBar progressBar;
    TextView nointernetTextView;

    // url
//    public static final String USGS_REQUEST_URL = "http://10.0.2.2:5000/Codecheck"; // link for internal server
    public static final String USGS_REQUEST_URL = MainActivity.URL_REQUEST+"/Codecheck";

    ///back button
    OnBackButtonClickListener mCallbackBackButton;
    public interface OnBackButtonClickListener{
        void onBackButtonSelected ();
    }

    // Done button
    OnDoneClickListener mCallbackDoneButton;
    public interface OnDoneClickListener{
        void onDoneButtonSelected (String locationName, int locationId, String imageUrl, int customerCode);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallbackBackButton = (OnBackButtonClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "Must implement OnBackButtonClickListener");
        }
        try{
            mCallbackDoneButton = (OnDoneClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "Must implement OnDoneClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_entercode_container,container,false);

        enterCodeEditText = (EditText) rootView.findViewById(R.id.code_edit_text);
        doneButton = (Button) rootView.findViewById(R.id.done_enter_code_button);
        backButtonImageView = (ImageView) rootView.findViewById(R.id.back_arrow_entercode_imageview);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_spinner_entercode);
        progressBar.setVisibility(View.INVISIBLE);
        nointernetTextView = (TextView) rootView.findViewById(R.id.no_internet_entercode_textview);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                thecode = Integer.parseInt(enterCodeEditText.getText().toString());
                Log.e("CODE", "The code after clicking "+thecode);
                LoaderManager loaderManager = getLoaderManager();
                Bundle bundle = new Bundle();
                bundle.putInt("CODE",thecode);
//                loaderManager.initLoader(1, bundle, EnterCodeContainerFragment.this);
                loaderManager.restartLoader(1, bundle, EnterCodeContainerFragment.this);
            }
        });

        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbackBackButton.onBackButtonSelected();
            }
        });

        return rootView;
    }


    @NonNull
    @Override
    public Loader<LocationNames> onCreateLoader(int id, Bundle args) {

        int theCode = args.getInt("CODE");
        String USGS_REQUEST_URL_withCode = USGS_REQUEST_URL+"/"+theCode;
        return new CodeCheckLoader(getActivity(),USGS_REQUEST_URL_withCode);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<LocationNames> loader, LocationNames data) {

        Log.v("CODE", "The data is "+data);
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Boolean isConnected = activeNetwork !=null && activeNetwork.isConnected();
        if(isConnected){
            if(data.getLocationID()==0){
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(),"The code isn't correct",Toast.LENGTH_LONG).show();
            } else {
                locationId = data.getLocationID();
                locationName = data.getLocationName();
                imageUrl = data.getImageURL();
                mCallbackDoneButton.onDoneButtonSelected(locationName,locationId,imageUrl,thecode);
                nointernetTextView.setVisibility(View.VISIBLE);
//                /** Duration of wait **/
//                int SPLASH_DISPLAY_LENGTH = 2000;
//                new Handler().postDelayed(new Runnable(){
//                    @Override
//                    public void run() {
//                        locationId = data.getLocationID();
//                        locationName = data.getLocationName();
//                        imageUrl = data.getImageURL();
//                        mCallbackDoneButton.onDoneButtonSelected(locationName,locationId,imageUrl,thecode);
//                    }
//                }, SPLASH_DISPLAY_LENGTH);

            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<LocationNames> loader) {

    }

}
