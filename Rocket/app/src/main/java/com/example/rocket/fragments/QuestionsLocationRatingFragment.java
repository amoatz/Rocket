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
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.rocket.adapter.QuestionsListViewAdapter;
import com.example.rocket.classes.CommentWord;
import com.example.rocket.classes.QuestionWord;
import com.example.rocket.loaders.CommentLoader;
import com.example.rocket.loaders.GetQuestionsLoader;

import java.util.ArrayList;
import java.util.List;

public class QuestionsLocationRatingFragment extends Fragment implements QuestionsListViewAdapter.listItemClickListener,
                                                                    LoaderManager.LoaderCallbacks<ArrayList<QuestionWord>>{

    //    ListView list;
    private RecyclerView mList;
    QuestionsListViewAdapter adapter;
    String[] questionList;
    ArrayList<QuestionWord> arraylist = new ArrayList<QuestionWord>();
    ProgressBar progressBar;
    TextView nointernetTextView;
    int gLocalId;

    // url
    public static final String USGS_REQUEST_URL = "http://10.0.2.2:5000/questions"; // link for internal server

    //// Communication between fragments//////
    ///back button
    OnBackButtonClickListener mCallback;
    public interface OnBackButtonClickListener{
        void onButtonSelected ();
        void onButtonDone(List<QuestionWord> questionWordList,int locationId);
    }

    @Override
    public void onListItemClick(List<QuestionWord> questionWord) {
        //TODO: Post the results to the server
        mCallback.onButtonDone(questionWord,gLocalId);
    }

    @Override
    public void onAttach(Context context) {
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

        View rootView = inflater.inflate(R.layout.fragment_questions_location_rating_container,container,false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_spinner_questions);
        nointernetTextView = (TextView) rootView.findViewById(R.id.no_internet_location_rating_textview);

        // Get the Data intent
        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        int locationId = b.getInt("LOCATIONID");
        gLocalId = locationId;
        String locationName = b.getString("LOCATIONNAME");

        ImageView backButton = (ImageView) rootView.findViewById(R.id.back_arrow_questions_location_rating_container);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onButtonSelected();
            }
        });

        // Locate the ListView
        mList = (RecyclerView) rootView.findViewById(R.id.listview_questions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        mList.setLayoutManager(layoutManager);

        // Pass results to ListViewAdapter Class
        adapter = new QuestionsListViewAdapter(arraylist,this);
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


    //////////////// Loader Functions /////////////////
    @NonNull
    @Override
    public Loader<ArrayList<QuestionWord>> onCreateLoader(int id, @Nullable Bundle args) {

        Log.e("CHECKPOINT","Inside onCreateLoader");
        int locationId = args.getInt("LocationId");
        String USGS_REQUEST_URL_LOCATIONID=USGS_REQUEST_URL;
        Log.e("CHECKPOINT","The location Id is :"+locationId+".");
        USGS_REQUEST_URL_LOCATIONID = USGS_REQUEST_URL+"/"+locationId;

        return new GetQuestionsLoader(getActivity(),USGS_REQUEST_URL_LOCATIONID);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<QuestionWord>> loader, ArrayList<QuestionWord> data) {

        progressBar.setVisibility(View.INVISIBLE);
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Boolean isConnected = activeNetwork !=null && activeNetwork.isConnected();
        Log.e("CHECKPOINT","isConnected is "+isConnected);
        if (isConnected){
            adapter.clear();
            ArrayList<QuestionWord> dataInLocationWordFormat = new ArrayList<QuestionWord>();
            dataInLocationWordFormat.add(new QuestionWord("Overall Rating",0));
            Log.e("CHECKPOINT","size of data  is  "+data.size());

            for(int i=0; i<data.size();i++){
                dataInLocationWordFormat.add(new QuestionWord(data.get(i).getQuestion(),
                        0));
            }
            dataInLocationWordFormat.add(new QuestionWord("",0));
            Log.e("CHECKPOINT","size of data in LocationNames Formate is  "+dataInLocationWordFormat.size());
            arraylist.addAll(0,dataInLocationWordFormat);
            adapter.notifyItemRangeInserted(0,arraylist.size()); // update the list
        } else {
            nointernetTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<QuestionWord>> loader) {
        adapter.clear();
    }

}
