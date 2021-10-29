package com.example.rocket.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rocket.R;
import com.example.rocket.classes.CommentWord;
import com.example.rocket.classes.LocationNames;

import java.util.ArrayList;
import java.util.List;

public class CommentListViewAdapter extends RecyclerView.Adapter<CommentListViewAdapter.NumberViewHolder> {

    Context mContext;
    LayoutInflater inflater;
    private List<CommentWord> commentNamesList = null;
    private ArrayList<CommentWord> arraylist;

    // For the adapter
    private static int viewHolderCount;
    private int mNumberItems;

    final private listItemClickListener mOnClickListener;
    public interface listItemClickListener{
        void onListItemClick(int clickedItemIndex, int locationId, String imageUrl, String locationName);
    }

    public CommentListViewAdapter(List<CommentWord> locationNamesList, listItemClickListener listener){

        Log.v("ADAPTER","list is = "+locationNamesList.size());
        // for the search item
//        mContext = context;
        this.commentNamesList = locationNamesList;
        Log.v("ADAPTER","list is = "+this.commentNamesList.size());
//        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<CommentWord>();
        this.arraylist.addAll(locationNamesList);

        //for the adapter
        mNumberItems = locationNamesList.size();
        mOnClickListener = listener;
        viewHolderCount = 0;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutForListItem = R.layout.comment_list_view_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutForListItem,viewGroup,shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        Log.v("ADAPTER","Inside onCreateViewHolder");

        //set View Holder
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
        Log.v("ADAPTER","position is = "+position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        Log.v("ADAPTER","Count is = "+mNumberItems);
        Log.v("ADAPTER","Count of locationNameList  is = "+ commentNamesList.size());
//        Log.v("ADAPTER","Count of  arraylist is = "+arraylist.size());
        mNumberItems = commentNamesList.size();
        arraylist.clear();
        arraylist.addAll(commentNamesList);
        return mNumberItems;
    }

    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nameTextView;
        TextView commentTextView;
        RatingBar ratingBarRatingBar;

        public NumberViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_comment_list);
            commentTextView = (TextView) itemView.findViewById(R.id.review_comment_list);
            ratingBarRatingBar = (RatingBar) itemView.findViewById(R.id.adapter_rating_bar_comment_list);
        }

        void bind (int listIndex){

            nameTextView.setText(commentNamesList.get(listIndex).getName());
            commentTextView.setText(commentNamesList.get(listIndex).getComment());
            ratingBarRatingBar.setRating(commentNamesList.get(listIndex).getRating());

        }

        @Override
        public void onClick(View view) {

        }
    }

    public void clear(){
        int size = commentNamesList.size();
        commentNamesList.clear();
        notifyItemRangeRemoved(0, size);
    }
}
