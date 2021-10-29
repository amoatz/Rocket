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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.rocketowner.Loader.GetCodeLoader;
import com.example.rocketowner.R;

public class CodeContainerFragment extends Fragment {

    ProgressBar progressBar;
    TextView customerCodeDisplayTextView;
    ImageView backButtonImageView;
    int locationId;

    // Generate Code Button
    OnButtonClickListener mCallback;
    public interface OnButtonClickListener{
        void onButtonSelected (int locationId);

    }

    @Override
    public void onAttach(Context context) {
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

        View rootView = inflater.inflate(R.layout.fragment_code_container,container,false);

        // Get the Data intent
        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        int customerCode = b.getInt("CUSTOMERCODE");
        locationId = b.getInt("LocationId");

        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_spinner_code);
        progressBar.setVisibility(View.INVISIBLE);
        customerCodeDisplayTextView = (TextView) rootView.findViewById(R.id.code_code_text_view);
        backButtonImageView = (ImageView) rootView.findViewById(R.id.back_arrow_code_container);

        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onButtonSelected(locationId);
            }
        });

        customerCodeDisplayTextView.setText(Integer.toString(customerCode));

        return rootView;
    }



}
