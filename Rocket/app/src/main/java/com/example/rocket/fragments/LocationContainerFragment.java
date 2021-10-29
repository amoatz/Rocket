package com.example.rocket.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rocket.R;
import com.example.rocket.adapter.DetailAdapter;
import com.example.rocket.adapter.SearchListViewAdapter;
import com.example.rocket.classes.DetailWord;
import com.example.rocket.classes.LocationNames;
import com.example.rocket.classes.LocationWord;
import com.example.rocket.loaders.LocationLoader;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class LocationContainerFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<LocationWord>>,
                        DetailAdapter.DetailItemClickListener{


    // url
    public static String SERVER_URL = "http://10.0.2.2:5000/location/"; // link for internal server
    public static String USGS_REQUEST_URL_Location;



    // Views
    ImageView locationImage;
    TextView locationText;
    RatingBar locationRating;
    Button locationVoteButton;
    Button locationReviewsButton;
    ImageView backButtonImageView;
    ProgressBar progressBar;
    TextView nointernetTextView;

    //Adapters
    //ListView list
    private RecyclerView mList;
    DetailAdapter adapter;
    ArrayList<DetailWord> arraylist = new ArrayList<DetailWord>();

    ///////// Communication between fragments //////////////////
    // search Button
    OnButtonVoteClickListener mCallback;

    // Click Listener of the adapter
    @Override
    public void onListItemClick(int clickedItemIndex, int locationId) {
    }

    public interface OnButtonVoteClickListener{
        void onButtonSelected (int locationId, String imageUrl, String locationName);
    }

    //// Comments Button
    OnCommentsButtonClickListener mCallbackComments;
    public interface OnCommentsButtonClickListener{
        void onCommentButton (int locationId, String imageUrl, String locationName);
    }

    ///back button
    OnBackButtonClickListener mCallbackBackButton;
    public interface OnBackButtonClickListener{
        void onButtonSelected (int locationPosition,
                               String searchWord);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v("TEST","in OnAttach");
        try{
            mCallback = (OnButtonVoteClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "Must implement OnButtonClickListener");
        }
        try{
            mCallbackComments = (OnCommentsButtonClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "Must implement OnCommentsButtonClickListener");
        }
        try {
            mCallbackBackButton = (OnBackButtonClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "Must implement OnBackButtonClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_location_container,container,false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_spinner_location);
        nointernetTextView = (TextView) rootView.findViewById(R.id.no_internet_location_textview);

        // Get the Data intent
        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        int LocationId = b.getInt("LOCATIONID");
        String imageUrl = b.getString("LOCATIONIMAGEURL");
        String locationName = b.getString("LOCATIONNAME");
        String LocationId_string = Integer.toString(LocationId);
        USGS_REQUEST_URL_Location = SERVER_URL + LocationId_string;

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, this);

        ////////// Locate the ListView in listview_main.xml
        mList = (RecyclerView) rootView.findViewById(R.id.detail_listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        mList.setLayoutManager(layoutManager);


        // Pass results to ListViewAdapter Class
        adapter = new DetailAdapter(arraylist,this);
        Log.v("ADAPTER","In Main before set adapter");

        // Binds the Adapter to the ListView
        mList.setAdapter(adapter);

        //Views
        locationImage = (ImageView) rootView.findViewById(R.id.location_imageView);
        locationText = (TextView) rootView.findViewById(R.id.name_location_textView);
        locationRating = (RatingBar) rootView.findViewById(R.id.location_rating_bar);
        locationVoteButton = (Button) rootView.findViewById(R.id.location_vote_button);
        locationReviewsButton = (Button) rootView.findViewById(R.id.location_comments_button);
        backButtonImageView = (ImageView) rootView.findViewById(R.id.back_arrow_location_container);

        locationVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onButtonSelected(LocationId,imageUrl,locationName);
            }
        });

        locationReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbackComments.onCommentButton(LocationId,imageUrl,locationName);
            }
        });

        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbackBackButton.onButtonSelected(0,locationName);
            }
        });

        return rootView;
    }

    //////////////////// Loader override functions ////////////////////////////////////////
    @Override
    public Loader<ArrayList<LocationWord>> onCreateLoader(int id, Bundle args) {
        Log.e("CHECKPOINT","Inside onCreateLoader");
        return new LocationLoader(getActivity(),USGS_REQUEST_URL_Location);
    }
    @Override
    public void onLoadFinished(Loader<ArrayList<LocationWord>> loader, ArrayList<LocationWord> data) {
        Log.e("CHECKPOINT","Inside onLoadFinished");
        progressBar.setVisibility(View.INVISIBLE);

        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Boolean isConnected = activeNetwork !=null && activeNetwork.isConnected();
        Log.e("CHECKPOINT","isConnected is "+isConnected);

        if (isConnected){

            Log.e("CHECKPOINT","size of data  is  "+data.size());
            if (data.size()==1){
                // TODO: add the data to page
                locationText.setText(data.get(0).getmName());
                locationRating.setRating(data.get(0).getmRating());

//                String photo_url_str = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1267&q=80";
                String photo_url_str = data.get(0).getmIcon();
                new DownloadImageFromInternet(locationImage).execute(photo_url_str);

                arraylist.clear();
                arraylist.add(new DetailWord("Website: ",data.get(0).getmWebsite()));
                arraylist.add(new DetailWord("Address: ",data.get(0).getmFormatted_adress()));
                arraylist.add(new DetailWord("Phone Number: ",data.get(0).getmFormatted_phone_number()));
                arraylist.add(new DetailWord("Type of food: ",data.get(0).getmTypeOf()));
                arraylist.add(new DetailWord("Zone: ",data.get(0).getmZone()));
                arraylist.add(new DetailWord("City: ",data.get(0).getmCity()));

                adapter.notifyItemRangeInserted(0,arraylist.size()); // update the list


            } else {
                nointernetTextView.setVisibility(View.VISIBLE);
            }
//
//            for(int i=0; i<data.size();i++){
//                dataInLocationWordFormat.add(new LocationNames(data.get(i).getmName(),
//                        data.get(i).getmid()));
//                Log.e("CHECKPOINT","Location "+ i +" word Name is  "+dataInLocationWordFormat.get(i).getLocationName());
//            }
//            Log.e("CHECKPOINT","size of data in LocationNames Formate is  "+dataInLocationWordFormat.size());
//            arraylist.addAll(0,dataInLocationWordFormat);
//            adapter.notifyItemRangeInserted(0,arraylist.size()); // update the list
        } else {
            nointernetTextView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onLoaderReset(Loader<ArrayList<LocationWord>> loader) {
        Log.e("CHECKPOINT","Inside onLoaderReset");
        //TODO : clear
    }
    //////////////////// End of Loader override functions ///////////////////////////////////

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
                bimage=BitmapFactory.decodeStream(in);
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
