package com.example.rocket.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rocket.R;
import com.example.rocket.classes.DetailWord;

import java.util.ArrayList;
import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.NumberViewHolder> {

    Context mContext;
    LayoutInflater inflater;
    private List<DetailWord> detailNamesList = null;
    private ArrayList<DetailWord> arraylist;

    // For the adapter
    final private DetailItemClickListener mOnClickListener;
    private static int viewHolderCount;
    private int mNumberItems;

    public interface DetailItemClickListener{
        void onListItemClick(int clickedItemIndex, int locationId);
    }

    public DetailAdapter (List<DetailWord> detailNamesList, DetailItemClickListener listener){

        Log.v("ADAPTER","list is = "+detailNamesList.size());
        // for the search item
//        mContext = context;
        this.detailNamesList = detailNamesList;
        Log.v("ADAPTER","list is = "+this.detailNamesList.size());
//        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<DetailWord>();
        this.arraylist.addAll(detailNamesList);

        //for the adapter
        mNumberItems = detailNamesList.size();
        mOnClickListener = listener;
        viewHolderCount = 0;

    }

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutForListItem = R.layout.detail_list_view_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutForListItem,parent,shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        Log.v("ADAPTER","Inside onCreateViewHolder");

        //set view holder

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {

        holder.bind(position);

    }

    @Override
    public int getItemCount() {

        mNumberItems = detailNamesList.size();
        arraylist.clear();
        arraylist.addAll(detailNamesList);
        return mNumberItems;
    }

    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView titleTextView;
        TextView valueTextView;

        public NumberViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.title_detail_list_textview);
            valueTextView = (TextView) itemView.findViewById(R.id.value_detail_list_textview);

            itemView.setOnClickListener(this);
        }


        void bind (int listIndex){

            titleTextView.setText(detailNamesList.get(listIndex).getTitleWord());
            valueTextView.setText(detailNamesList.get(listIndex).getValueWord());
        }

        @Override
        public void onClick(View view) {

        }
    }


}
