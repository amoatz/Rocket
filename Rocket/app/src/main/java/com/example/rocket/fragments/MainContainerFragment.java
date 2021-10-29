package com.example.rocket.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.rocket.R;

public class MainContainerFragment extends Fragment {

    public MainContainerFragment(){
    }
    TextView nointernetTextView;

    ///////// Communication between fragments //////////////////
    // search Button
    OnButtonClickListener mCallback;
    public interface OnButtonClickListener{
        void onButtonSelected (String searchText);
    }

    // SignIn Button
    onButtonSignInListener mCallbackSignIn;
    public interface onButtonSignInListener{
        void onButtonSelectedSignIn();
    }

    //Register Button
    onButtonRegisterListener mCallbackRegister;
    public interface onButtonRegisterListener{
        void onButtonSelectedRegister();
    }

    //Filter Button
    onButtonFilterListener mCallbackfilter;
    public interface onButtonFilterListener{
        void onButtonFilterRegister();
    }

    // EnterCode Button
    onEnterCodeListener mCallbackEnterCode;
    public interface onEnterCodeListener{
        void onEnterCodeRegister();
    }

    ///////////// onAttach //////////
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v("TEST","in OnAttach");
        try{
            mCallback = (OnButtonClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "Must implement OnButtonClickListener");
        }
        try{
            mCallbackSignIn = (onButtonSignInListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "Must implement onButtonSignInListener");
        }
        try{
            mCallbackRegister = (onButtonRegisterListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "Must implement onButtonRegisterListener");
        }
        try{
            mCallbackfilter = (onButtonFilterListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()+
                    "Must implement onButtonFilterListener");
        }
        try{
            mCallbackEnterCode = (onEnterCodeListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "Must implement onEnterCodeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v("TEST","in onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v("TEST","in onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("TEST","in onDestroy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("TEST","in onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_main_container,container,false);

        final TextView rocketTextView = (TextView) rootView.findViewById(R.id.rocket_textview);
        final EditText editText = (EditText) rootView.findViewById(R.id.main_edit_text);
        final Button searchButton = (Button) rootView.findViewById(R.id.search_button);

        final Button signInButton = (Button) rootView.findViewById(R.id.main_signin);
        final Button registerButton = (Button) rootView.findViewById(R.id.main_register);

        final Button filterButton = (Button) rootView.findViewById(R.id.filter_main_container_Button);

        final Button codeButton = (Button) rootView.findViewById(R.id.enter_code_button);

        nointernetTextView = (TextView) rootView.findViewById(R.id.no_internet_main_textview);

//        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//
//                Log.v("TEST","listener is working");
//                String searchText;
//                // check if the text is empty
//                if (editText.getText().toString() != null){
//                    Toast toast = Toast.makeText(
//                            rootView.getContext(),
//                            editText.getText().toString(),
//                            Toast.LENGTH_LONG);
//                    toast.show();
//                    Log.v("TEST","text is "+editText.getText().toString());
//                    searchText = editText.getText().toString();
//                } else {
//                    searchText = "%code%";
//                }
//                mCallback.onButtonSelected(searchText);
//
//            }
//        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("TEST","listener is working");
                String searchText;
                // check if the text is empty
                if (editText.getText().toString() != null){
//                    Toast toast = Toast.makeText(
//                            rootView.getContext(),
//                            editText.getText().toString(),
//                            Toast.LENGTH_LONG);
//                    toast.show();
                    searchText = editText.getText().toString();
                } else {
                    searchText = "%code%";
                }
                mCallback.onButtonSelected(searchText);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCallbackSignIn.onButtonSelectedSignIn();

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbackRegister.onButtonSelectedRegister();
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbackfilter.onButtonFilterRegister();
            }
        });

        codeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbackEnterCode.onEnterCodeRegister();
            }
        });

        return rootView;
    }
}
