package com.example.rocket.fragments;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rocket.R;
import com.example.rocket.adapter.CommentListViewAdapter;
import com.example.rocket.adapter.SearchListViewAdapter;
import com.example.rocket.classes.CommentWord;
import com.example.rocket.classes.LocationNames;
import com.example.rocket.classes.LocationWord;
import com.example.rocket.loaders.CommentLoader;
import com.example.rocket.loaders.LocationLoader;

import java.util.ArrayList;
import java.util.List;

public class CommentsContainerFragment extends Fragment  implements CommentListViewAdapter.listItemClickListener,
                                                                LoaderManager.LoaderCallbacks<ArrayList<CommentWord>>{

    //    ListView list;
    private RecyclerView mList;
    CommentListViewAdapter adapter;
    String[] reviewList;
    ArrayList<CommentWord> arraylist = new ArrayList<CommentWord>();
    ProgressBar progressBar;
    TextView nointernetTextView;


    // url
    public static final String USGS_REQUEST_URL = "http://10.0.2.2:5000/Reviews"; // link for internal server

    //// Communication between fragments//////
    ///back button
    OnBackButtonClickListener mCallback;
    public interface OnBackButtonClickListener{
        void onButtonSelected (int locationId,
                               String locationName,
                               String imageUrl);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mCallback = (OnBackButtonClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "Must implement OnBackButtonClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_comments_container,container,false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_spinner_comments);

        // Get the Data intent
        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        int locationId = b.getInt("LOCATIONID");
        String imageUrl = b.getString("LOCATIONIMAGEURL");
        String locationName = b.getString("LOCATIONNAME");

        ImageView backButton = (ImageView) rootView.findViewById(R.id.back_arrow_comments_container);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onButtonSelected(locationId,locationName,imageUrl);
            }
        });
        nointernetTextView = (TextView) rootView.findViewById(R.id.no_internet_comments_textview);

        // Create a fake list of  locations.
        ArrayList<CommentWord> locationNameList = new ArrayList<CommentWord>();

        // Locate the ListView in listview_main.xml
        mList = (RecyclerView) rootView.findViewById(R.id.listview_comments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        mList.setLayoutManager(layoutManager);

        // Pass results to ListViewAdapter Class
        adapter = new CommentListViewAdapter(arraylist,this);
        Log.v("ADAPTER","In Main before set adapter");

        // Binds the Adapter to the ListView
        mList.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mList.getContext(),
                layoutManager.getOrientation());
        mList.addItemDecoration(dividerItemDecoration);

        ///////////////GET the list from server /////////////////
        Bundle bundle = new Bundle();
        bundle.putInt("LocationId",locationId);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(1, bundle, this);



        return rootView;
    }

    @Override
    public void onListItemClick(int clickedItemIndex, int locationId, String imageUrl, String locationName) {

    }

    //////////////// Loader Functions /////////////////
    @NonNull
    @Override
    public Loader<ArrayList<CommentWord>> onCreateLoader(int id, @Nullable Bundle args) {

        Log.e("CHECKPOINT","Inside onCreateLoader");
        int locationId = args.getInt("LocationId");
        String USGS_REQUEST_URL_LOCATIONID=USGS_REQUEST_URL;
        Log.e("CHECKPOINT","The location Id is :"+locationId+".");
        USGS_REQUEST_URL_LOCATIONID = USGS_REQUEST_URL+"/"+locationId;

        return new CommentLoader(getActivity(),USGS_REQUEST_URL_LOCATIONID);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<CommentWord>> loader, ArrayList<CommentWord> data) {

        Log.e("CHECKPOINT","Inside onLoadFinished");
        progressBar.setVisibility(View.INVISIBLE);
//        ProgressBar mLoadingIndicator = (ProgressBar) findViewById(R.id.progress_bar);
//        mLoadingIndicator.setVisibility(View.GONE);
//        mEmptyStateTextView.setText(R.string.no_earthquake);

        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Boolean isConnected = activeNetwork !=null && activeNetwork.isConnected();
        Log.e("CHECKPOINT","isConnected is "+isConnected);

        if (isConnected){
            adapter.clear();
//            adapter.addAll(data);
            // Change the arraylist from LocationWord to LocationNames
            ArrayList<CommentWord> dataInLocationWordFormat = new ArrayList<CommentWord>();
            Log.e("CHECKPOINT","size of data  is  "+data.size());

            for(int i=0; i<data.size();i++){
                dataInLocationWordFormat.add(new CommentWord(data.get(i).getName(),
                                                            data.get(i).getComment(),
                                                            data.get(i).getRating()));

                Log.e("CHECKPOINT","Location "+ i +" word Comment is  "+dataInLocationWordFormat.get(i).getComment());
            }
            Log.e("CHECKPOINT","size of data in LocationNames Formate is  "+dataInLocationWordFormat.size());
            arraylist.addAll(0,dataInLocationWordFormat);
            adapter.notifyItemRangeInserted(0,arraylist.size()); // update the list
        } else {
            nointernetTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<CommentWord>> loader) {

        Log.e("CHECKPOINT","Inside onLoaderReset");
        adapter.clear();
    }
}
