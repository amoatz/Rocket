package com.example.rocket.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rocket.MainActivity;
import com.example.rocket.R;
import com.example.rocket.adapter.SearchListViewAdapter;
import com.example.rocket.classes.LocationNames;
import com.example.rocket.classes.LocationWord;
import com.example.rocket.loaders.LocationLoader;

import java.util.ArrayList;
import java.util.List;

public class SearchContainerFragment<ListViewAdapter>
        extends Fragment
        implements SearchListViewAdapter.listItemClickListener, SearchView.OnQueryTextListener,
        LoaderManager.LoaderCallbacks<ArrayList<LocationWord>> {

//    ListView list;
    private RecyclerView mList;
    SearchListViewAdapter adapter;

    SearchView editsearch;
    ImageView backButtonImageView;
    Button filterButton;
    ProgressBar progressBar;
    TextView nointernetTextView;

    String[] locationNameList;
    ArrayList<LocationNames> arraylist = new ArrayList<LocationNames>();

    String searchText;
    int unrated;
    int oneStar;
    int twoStar;
    int threeStar;
    int fourStar;
    int fiveStar;

    ////////////// Fragments Communication ////////////////////////////
    // ClickListener to go to next fragment and activity
    OnItemClickListener mCallback;
    public interface OnItemClickListener{
        void onItemSelected(int itemId, int locationId, String Imageurl, String name);
    }

    ///back button
    OnBackButtonClickListener mCallbackBackButton;
    public interface OnBackButtonClickListener{
        void onBackButtonSelected ();
    }

    ////Filter Button
    OnFilterButtonClickListener mcallbackFilterButton;
    public interface OnFilterButtonClickListener{
        void onFilterButtonSelected(String searchWord);
    }

    // url
    public static final String USGS_REQUEST_URL = MainActivity.URL_REQUEST+"/search"; // link for internal server


    //////////////////////// Loader Override Functions/////////////////////////////////////////
    @Override
    public Loader<ArrayList<LocationWord>> onCreateLoader(int id,Bundle args) {

        progressBar.setVisibility(View.VISIBLE);
        Log.e("CHECKPOINT","Inside onCreateLoader");
        String searchWordSent = args.getString("SEARCHWORD");
        String USGS_REQUEST_URL_SEARCHWORD=USGS_REQUEST_URL;
        Log.e("CHECKPOINT","The searword is :"+searchWordSent+".");
        if (searchWordSent.isEmpty()){
            USGS_REQUEST_URL_SEARCHWORD = USGS_REQUEST_URL;
            Log.e("CHECKPOINT","Inside if condition");
        } else {
            USGS_REQUEST_URL_SEARCHWORD = USGS_REQUEST_URL+"/"+searchWordSent+"/"+unrated
                    +"/"+oneStar+"/"+twoStar+"/"+threeStar+"/"+fourStar+"/"+fiveStar;
        }

        return new LocationLoader(getActivity(),USGS_REQUEST_URL_SEARCHWORD);
    }

    @Override
    public void onLoadFinished( Loader<ArrayList<LocationWord>> loader, ArrayList<LocationWord> data) {

        Log.e("CHECKPOINT","Inside onLoadFinished");
        progressBar.setVisibility(View.INVISIBLE);
//        ProgressBar mLoadingIndicator = (ProgressBar) findViewById(R.id.progress_bar);
//        mLoadingIndicator.setVisibility(View.GONE);
//        mEmptyStateTextView.setText(R.string.no_earthquake);

        Boolean isConnected;
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork !=null && activeNetwork.isConnected();


        Log.e("CHECKPOINT","isConnected is "+isConnected);

        if (isConnected){
            adapter.clear();
//            adapter.addAll(data);
            // Change the arraylist from LocationWord to LocationNames
            ArrayList<LocationNames> dataInLocationWordFormat = new ArrayList<LocationNames>();
            Log.e("CHECKPOINT","size of data  is  "+data.size());

            for(int i=0; i<data.size();i++){
                dataInLocationWordFormat.add(new LocationNames(data.get(i).getmName(),
                        data.get(i).getmid(),
                        data.get(i).getmRating(),
                        data.get(i).getmTypeOf(),
                        data.get(i).getmZone()+", "+data.get(i).getmCity(),
                        data.get(i).getmIcon()));
                Log.e("CHECKPOINT","Location "+ i +" word Name is  "+dataInLocationWordFormat.get(i).getLocationName());
            }
            Log.e("CHECKPOINT","size of data in LocationNames Formate is  "+dataInLocationWordFormat.size());
            dataInLocationWordFormat.add(new LocationNames("",0,0,"","",""));
            arraylist.addAll(0,dataInLocationWordFormat);
            adapter.notifyItemRangeInserted(0,arraylist.size()); // update the list
        } else {
            nointernetTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset( Loader<ArrayList<LocationWord>> loader) {

        Log.e("CHECKPOINT","Inside onLoaderReset");
        adapter.clear();

    }


    //////////////////// Loader Functions Finished//////////////////////////////////

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnItemClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "must implement OnItemClickListener");
        }

        try{
            mCallbackBackButton = (OnBackButtonClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()+
                    "must implement OnBackButtonClickListener");
        }

        try{
            mcallbackFilterButton = (OnFilterButtonClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()+
                    "must implement OnFilterButtonClickListener");
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search_container,container,false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_spinner_search);
        progressBar.setVisibility(View.VISIBLE);
        nointernetTextView = (TextView) rootView.findViewById(R.id.no_internet_search_textview);
        // Get the Data intent
        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        searchText = b.getString("SEARCHTEXT");

        if (b.containsKey("UNRATED")){
            unrated = b.getInt("UNRATED");
            oneStar = b.getInt("ONESTAR");
            twoStar = b.getInt("TWOSTAR");
            threeStar = b.getInt("THREESTAR");
            fourStar = b.getInt("FOURSTAR");
            fiveStar = b.getInt("FIVESTAR");
        } else {
            unrated=0;
            oneStar=1;
            twoStar=2;
            threeStar=3;
            fourStar=4;
            fiveStar=5;
        }


//
        Toast toast = Toast.makeText(
                rootView.getContext(),
                "The Text is : "+searchText,
                Toast.LENGTH_LONG);
        toast.show();
        Log.v("ADAPTER","Recieved Bundle Text is : "+searchText);


        // Create a fake list of  locations.
        ArrayList<LocationWord> locationNameList = new ArrayList<LocationWord>();

        // Locate the ListView in listview_main.xml
        mList = (RecyclerView) rootView.findViewById(R.id.listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        mList.setLayoutManager(layoutManager);

        // Pass results to ListViewAdapter Class
        adapter = new SearchListViewAdapter(arraylist,this);
        Log.v("ADAPTER","In Main before set adapter");

        // Binds the Adapter to the ListView
        mList.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mList.getContext(),
                layoutManager.getOrientation());
        mList.addItemDecoration(dividerItemDecoration);


        ///////////////GET the list from server /////////////////

        final List<LocationWord> finalLocations = locationNameList;

        LoaderManager loaderManager = getLoaderManager();
        Bundle bundle = new Bundle();
        bundle.putString("SEARCHWORD",searchText);
//        loaderManager.initLoader(1, bundle, this);
        loaderManager.restartLoader(1, bundle, this);

        // Locate the EditText
        editsearch = (SearchView) rootView.findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
        editsearch.setQuery(searchText,false);

        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                bundle.putString("SEARCHWORD",s);
                loaderManager.restartLoader(1, bundle, SearchContainerFragment.this);
                searchText = s;
                return false;
            }
        });

//        adapter.filter(searchText);

        backButtonImageView = (ImageView) rootView.findViewById(R.id.back_arrow_search_container);
        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbackBackButton.onBackButtonSelected();
            }
        });

        filterButton = (Button) rootView.findViewById(R.id.filter_search_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mcallbackFilterButton.onFilterButtonSelected(searchText);
            }
        });

        return rootView;
    }


    @Override
    public void onListItemClick(int clickedItemIndex, int locationId, String imageurl, String locationName) {
        Log.v("TEST","The item clicked : " + clickedItemIndex );
        Log.v("TEST","The item clicked id : " + locationId );
        mCallback.onItemSelected(clickedItemIndex,locationId,imageurl,locationName);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    // When the text is changed
    @Override
    public boolean onQueryTextChange(String newText) {

        progressBar.setVisibility(View.VISIBLE);
        Log.v("ADAPTER","In Main in OnQueryTextChange and Text is :"+newText);
        String text = newText;
        LoaderManager loaderManager = getLoaderManager();
        Bundle bundle = new Bundle();
        bundle.putString("SEARCHWORD",newText);
        loaderManager.initLoader(1, bundle, this);
        adapter.filter(text);
//        progressBar.setVisibility(View.INVISIBLE);
        return false;
    }
}
