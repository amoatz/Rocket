package com.example.rocket.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rocket.R;
import com.example.rocket.classes.QuestionWord;

import java.util.ArrayList;
import java.util.List;

public class QuestionsListViewAdapter extends RecyclerView.Adapter<QuestionsListViewAdapter.NumberViewHolder>{

    Context mContext;
    LayoutInflater inflater;
    private List<QuestionWord> questionList = null;
//    private ArrayList<QuestionWord> arraylist;

    // For the adapter
    private static int viewHolderCount;
    private int mNumberItems;

    final private listItemClickListener mOnClickListener;
    public interface listItemClickListener{
        void onListItemClick(List<QuestionWord> questionWord);
    }

    //// Adapter Constructor
    public QuestionsListViewAdapter(List<QuestionWord> questionsList, listItemClickListener listener){

        this.questionList = questionsList;
//        this.arraylist = new ArrayList<QuestionWord>();
//        this.arraylist.addAll(questionsList);

        //for the adapter
        mNumberItems = questionsList.size();
        mOnClickListener = listener;
        viewHolderCount = 0;

    }

    ///////////Adaptor functions
    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutForListItem = R.layout.questions_list_view_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutForListItem,parent,shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        Log.v("ADAPTER","Inside onCreateViewHolder");

        //set View Holder
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {

        Log.v("ADAPTER","position is = "+position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        mNumberItems = questionList.size();
//        arraylist.clear();
//        arraylist.addAll(questionList);
        return mNumberItems;
    }

    //////// NumberViewHolder
    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView questionTextView;
        RatingBar ratingBar;
        Button doneButton;

        public NumberViewHolder(View itemView) {
            super(itemView);
            questionTextView = (TextView) itemView.findViewById(R.id.question_inlistview_text_view);
            ratingBar = (RatingBar) itemView.findViewById(R.id.question_listview_rating_bar);
            doneButton = (Button) itemView.findViewById(R.id.done_button_question_listview);
        }

        void bind (int listIndex){
            questionTextView.setText(questionList.get(listIndex).getQuestion());

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    questionList.set(listIndex,new QuestionWord(questionList.get(listIndex).getQuestion(),(int)v));
                }
            });

            if (listIndex==mNumberItems-1){
                questionTextView.setVisibility(View.INVISIBLE);
                ratingBar.setVisibility(View.INVISIBLE);
                doneButton.setVisibility(View.VISIBLE);
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnClickListener.onListItemClick(questionList);
                    }
                });
            }
        }

        @Override
        public void onClick(View view) {
        }
    }

    public void clear(){
        int size = questionList.size();
        questionList.clear();
        notifyItemRangeRemoved(0, size);
    }
}
