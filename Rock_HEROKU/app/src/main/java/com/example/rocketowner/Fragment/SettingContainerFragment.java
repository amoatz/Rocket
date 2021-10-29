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
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rocketowner.Adapter.QuestionsListViewAdapter;
import com.example.rocketowner.Classes.QuestionWord;
import com.example.rocketowner.Loader.GetQuestionsLoader;
import com.example.rocketowner.MainActivity;
import com.example.rocketowner.R;

import java.util.ArrayList;
import java.util.List;

public class SettingContainerFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<QuestionWord>>,
                    QuestionsListViewAdapter.listItemClickListener{

    ImageView backImageView;
    Button deleteButton;
    Button doneButton;
    ProgressBar progressBar;
    int locationId;

    //    ListView list;
    private RecyclerView mList;
    QuestionsListViewAdapter adapter;
    ArrayList<QuestionWord> arraylist = new ArrayList<QuestionWord>();
    List<QuestionWord> finalArraylist = new ArrayList<QuestionWord>();

    // url
//    public static final String USGS_REQUEST_URL = "http://10.0.2.2:5000/questions"; // link for internal server
    public static final String USGS_REQUEST_URL = MainActivity.URL_REQUEST+"/questions";

    // Generate Code Button
    OnButtonClickListener mCallback;
    public interface OnButtonClickListener{
        void onBackButtonSelected (int locationId);
        void onDoneButtonSelected(List<QuestionWord> questionWordList, int locationId);
    }

    @Override
    public void onListItemClick(int clickedPosition) {

    }

    @Override
    public void onSaveClick(List<QuestionWord> questionWord) {
        Log.v("onSAveCLICK","The count before onSaveClick is "+finalArraylist.size());
        finalArraylist = questionWord;
        Log.v("onSAveCLICK","The count after onSaveClick is "+finalArraylist.size());
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

        View rootView = inflater.inflate(R.layout.fragment_setting_container,container,false);

        // Get the Data intent
        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        locationId = b.getInt("LocationId");
        Log.v("CHECK","The locationid is "+locationId);

        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_spinner_setting);

        backImageView = (ImageView) rootView.findViewById(R.id.back_arrow_setting_container);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onBackButtonSelected(locationId);
            }
        });

        ///////////////GET the list from server /////////////////
        Bundle bundle = new Bundle();
        bundle.putInt("LocationId", locationId);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(1, bundle, this);

        // Locate the ListView
        mList = (RecyclerView) rootView.findViewById(R.id.listview_questions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        mList.setLayoutManager(layoutManager);

        // Pass results to ListViewAdapter Class
        adapter = new QuestionsListViewAdapter(arraylist,this);

        // Binds the Adapter to the ListView
        mList.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mList.getContext(),
                layoutManager.getOrientation());
        mList.addItemDecoration(dividerItemDecoration);

        deleteButton = (Button) rootView.findViewById(R.id.delete_setting_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapter.deleteQuestions();
//                for (int i= finalArraylist.size()-1; i>0 ;i--){
//                    Log.v("DELETE","The i value is "+i);
//                    Log.v("DELETE","The count value before delete is "+finalArraylist.size());
//                    if(finalArraylist.get(i).getRating()==1){
////                        finalArraylist.remove(i);
//
////                        adapter.notifyItemRemoved(i);
//                        finalArraylist.remove(i);
//                    }
//
//                    Log.v("DELETE","The count value After delete is "+finalArraylist.size());
//                }
//                arraylist.clear();
//                adapter.clear();
//                arraylist.addAll(0,finalArraylist);
//                adapter.notifyItemRangeInserted(0,arraylist.size());

            }
        });

        doneButton = (Button) rootView.findViewById(R.id.done_setting_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onDoneButtonSelected(finalArraylist,locationId);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        return rootView;
    }


    @NonNull
    @Override
    public Loader<ArrayList<QuestionWord>> onCreateLoader(int id, @Nullable Bundle args) {


        int locationId = args.getInt("LocationId");
        String USGS_REQUEST_URL_LOCATIONID=USGS_REQUEST_URL;
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
            dataInLocationWordFormat.add(new QuestionWord("",0));

            for(int i=0; i<data.size();i++){
                dataInLocationWordFormat.add(new QuestionWord(data.get(i).getQuestion(),
                        0));
            }
//            dataInLocationWordFormat.add(new QuestionWord("",0));
            Log.e("CHECKPOINT","size of data in LocationNames Formate is  "+dataInLocationWordFormat.size());
            arraylist.addAll(0,dataInLocationWordFormat);
            adapter.notifyItemRangeInserted(0,arraylist.size()); // update the list
        } else {
            //            mEmptyStateTextView.setText("No internet Connection");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<QuestionWord>> loader) {

        adapter.clear();

    }


}
