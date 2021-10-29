package com.example.rocket.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.rocket.MainActivity;
import com.example.rocket.R;
import com.example.rocket.loaders.PostRatingLoader;

import java.io.InputStream;

public class LocationRatingFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>{

    //url
    public static String USGS_REQUEST_URL_Location_rating = "http://10.0.2.2:5000/vote"; // link for internal server

    int locationId;
    int ratingValue;
    String commentString;
    String locationName;
    String imageUrl;
    ImageView backButtonImageView;
    String customerType;
    int customerVoteCode;
    ProgressBar progressBar;
    TextView nointernetTextView;

    ///////// Communication between fragments //////////////////
    // search Button
    OnButtonVoteClickListener mCallback;
    public interface OnButtonVoteClickListener{
        void onButtonSelected (int locationId, String imageUrl, String locationName, String commentText);
    }

    ///back button
    OnBackButtonClickListener mCallbackBackButton;
    public interface OnBackButtonClickListener{
        void onBackButtonSelected(int locationId,
                                  String locationName,
                                  String imageUrl);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.v("TEST","in OnAttach");
        try{
            mCallback = (OnButtonVoteClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "Must implement OnButtonClickListener");
        }

        try{
            mCallbackBackButton = (OnBackButtonClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "Must implement OnBackButtonClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_location_rating_container,container,false);

        // Get the Data intent
        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        locationId = b.getInt("LOCATIONID");
        imageUrl = b.getString("LOCATIONIMAGEURL");
        locationName = b.getString("LOCATIONNAME");
        customerType = b.getString("CUSTOMERTYPE");
        customerVoteCode = b.getInt("CUSTOMERCODE");

        String LocationId_string = Integer.toString(locationId);

        ImageView image = (ImageView) rootView.findViewById(R.id.location_rating_image);
        TextView name = (TextView) rootView.findViewById(R.id.location_rating_name_textview);
        name.setText(locationName);
        RatingBar rating = (RatingBar) rootView.findViewById(R.id.location_rating_bar_rating_bar);
        Button voteButton = (Button) rootView.findViewById(R.id.vote_final_button);
        EditText commentEditText = (EditText) rootView.findViewById(R.id.add_comment_edit_text);
        backButtonImageView = (ImageView) rootView.findViewById(R.id.back_arrow_location_rating_container);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_spinner_location_rating);
        progressBar.setVisibility(View.INVISIBLE);
        nointernetTextView = (TextView) rootView.findViewById(R.id.no_internet_location_rating_textview);

        String photo_url_str = imageUrl;
        Log.v("IMAGE","the link is"+imageUrl);
        new DownloadImageFromInternet(image).execute(photo_url_str);

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                ratingValue = (int) v;
                Log.v("RATING","rating is "+ratingValue);

            }
        });

        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                loadingtheRatings();
                commentString = commentEditText.getText().toString();
                Log.v("RATING","the comment is"+commentString);
                mCallback.onButtonSelected(locationId,imageUrl,locationName,commentString);
            }
        });

        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                commentString=charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbackBackButton.onBackButtonSelected(locationId,locationName,imageUrl);
            }
        });

        return rootView;
    }

    // function to load the vote
    private void loadingtheRatings(){
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, this);
    }
    ///////////////Loader Methods////////////////////////
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        Log.e("CHECKPOINT","Inside onCreateLoader");
        Log.e("CODE","Inside loader the location Id is "+locationId);
        if (customerVoteCode==0){
            return new PostRatingLoader(getActivity(),USGS_REQUEST_URL_Location_rating,
                    MainActivity.clientId,
                    ratingValue,
                    locationId,
                    commentString);
        } else {
            String USGS_REQUEST_URL_Location_rating_Code = USGS_REQUEST_URL_Location_rating+"/"+customerVoteCode;
            return new PostRatingLoader(getActivity(),USGS_REQUEST_URL_Location_rating_Code,
                    MainActivity.clientId,
                    ratingValue,
                    locationId,
                    commentString);
        }

    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        Log.e("CHECKPOINT","Inside onLoadFinished");
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Boolean isConnected = activeNetwork !=null && activeNetwork.isConnected();
        Log.e("CHECKPOINT","isConnected is "+isConnected);

        if (isConnected) {
            Log.v("RATING",data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        Log.e("CHECKPOINT","Inside onLoaderReset");
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;
            Toast.makeText(getContext(), "Please wait, it may take a few minute...",Toast.LENGTH_SHORT).show();
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {

            Log.v("CHECKPOINTIMAGE","onPost Image");
            imageView.setImageBitmap(result);
        }
    }
}
