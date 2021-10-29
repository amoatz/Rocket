package com.example.rocket.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.rocket.R;

public class FilterContainerFragment extends Fragment {

    ImageView backButtonImageView;

    Button unratedButon;
    Button oneStarButton;
    Button twoStarButton;
    Button threeStarButton;
    Button fourStarButton;
    Button fiveStarButton;

    Button doneButton;
    TextView nointernetTextView;

    public Boolean unratedFlag=true;
    public Boolean oneStarFlag=true;
    public Boolean twoStarFlag=true;
    public Boolean threeStarFlag=true;
    public Boolean fourStarFlag=true;
    public Boolean fiveStarFlag=true;

    String searchWord;

    ///back button
    OnBackButtonClickListener mCallback;
    public interface OnBackButtonClickListener{
        void onButtonSelected (String searchWord);
        void onDoneButtonSelected(String searchWord, int unrated,
                                  int oneStar, int twoStar, int threeStar, int fourStar, int fiveStar);
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

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_filter_container,container,false);

        // Get the Data intent
        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        searchWord = b.getString("SEARCHTEXT");

        unratedButon = (Button) rootView.findViewById(R.id.unrated_starts_filter_checkbox);
        unratedButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unratedFlag = ButtonClick(unratedButon,unratedFlag);
            }
        });
        oneStarButton = (Button) rootView.findViewById(R.id.one_starts_filter_checkbox);
        oneStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneStarFlag = ButtonClick(oneStarButton,oneStarFlag);
            }
        });
        twoStarButton = (Button) rootView.findViewById(R.id.two_starts_filter_checkbox);
        twoStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twoStarFlag = ButtonClick(twoStarButton,twoStarFlag);
            }
        });
        threeStarButton = (Button) rootView.findViewById(R.id.three_starts_filter_checkbox);
        threeStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threeStarFlag = ButtonClick(threeStarButton,threeStarFlag);
            }
        });
        fourStarButton = (Button) rootView.findViewById(R.id.four_starts_filter_checkbox);
        fourStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fourStarFlag = ButtonClick(fourStarButton,fourStarFlag);
            }
        });
        fiveStarButton = (Button) rootView.findViewById(R.id.five_starts_filter_checkbox);
        fiveStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fiveStarFlag = ButtonClick(fiveStarButton,fiveStarFlag);
            }
        });

        backButtonImageView = (ImageView) rootView.findViewById(R.id.back_arrow_filter_container);
        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onButtonSelected(searchWord);
            }
        });

        doneButton = (Button) rootView.findViewById(R.id.Done_inFilter_Button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int disabledButton = 10;
                int unrated=0;
                int oneStar=1; int twoStar=2; int threeStar=3; int fourStar=4; int fiveStar=5;
                mCallback.onDoneButtonSelected(searchWord,
                                    Evaluateflags(unratedFlag,0),
                                    Evaluateflags(oneStarFlag,1),
                                    Evaluateflags(twoStarFlag,2),
                                    Evaluateflags(threeStarFlag,3),
                                    Evaluateflags(fourStarFlag,4),
                                     Evaluateflags(fiveStarFlag,5));
            }
        });

        nointernetTextView = (TextView) rootView.findViewById(R.id.no_internet_filter_textview);

        return rootView;
    }

    Boolean ButtonClick(Button button, Boolean flag){
        if(flag){
            button.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.black));
            return false;
        } else {
            button.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.PrimaryText));
            return true;
        }
    }

    int Evaluateflags(Boolean flag,int value){
        if (flag){
            return value;
        } else {
            return 10;
        }
    }
}
