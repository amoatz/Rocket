package com.example.rocketowner.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rocketowner.Classes.QuestionWord;
import com.example.rocketowner.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionsListViewAdapter extends RecyclerView.Adapter<QuestionsListViewAdapter.NumberViewHolder>{

    Context mContext;
    LayoutInflater inflater;
    private List<QuestionWord> questionList = null;
    private ArrayList<Boolean> deleteList;
//    private ArrayList<QuestionWord> arraylist;

    // For the adapter
    private static int viewHolderCount;
    private int mNumberItems;

    final private listItemClickListener mOnClickListener;
    public interface listItemClickListener{
        void onListItemClick(int clickedPosition);
        void onSaveClick(List<QuestionWord> questionWord);
    }

    //// Adapter Constructor
    public QuestionsListViewAdapter(List<QuestionWord> questionsList, listItemClickListener listener){

        this.questionList = questionsList;
//        this.arraylist = new ArrayList<QuestionWord>();
//        this.arraylist.addAll(questionsList);

        //for the adapter
        mNumberItems = questionsList.size();
        Log.v("CHECK"," the in start mNumberItems "+ mNumberItems);
        for (int i=0; i<mNumberItems; i++){
            deleteList.add(false);
        }
        Log.v("CHECK"," the list is "+ deleteList);
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
        Log.v("ADD","inside getItemCount "+mNumberItems);
//        arraylist.clear();
//        arraylist.addAll(questionList);
        return mNumberItems;
    }

    //////// NumberViewHolder
    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView questionTextView;
        EditText questionEditTextView;
        Button addButton;

        public NumberViewHolder(View itemView) {
            super(itemView);
            questionTextView = (TextView) itemView.findViewById(R.id.question_inlistview_text_view);
            questionEditTextView = (EditText) itemView.findViewById(R.id.add_question_edittext);
            questionEditTextView.setVisibility(View.INVISIBLE);
            addButton = (Button) itemView.findViewById(R.id.add_new_question_done_button);
            addButton.setVisibility(View.INVISIBLE);

        }

        void bind (int listIndex){
            Log.v("CHECKPOSITION","listindex "+listIndex);
            Log.v("CHECKPOSITION","rating "+questionList.get(listIndex).getRating());
            Log.v("CHECKPOSITION","quest "+questionList.get(listIndex).getQuestion());
            questionTextView.setText(questionList.get(listIndex).getQuestion());
            addButton.setVisibility(View.INVISIBLE);
            questionEditTextView.setVisibility(View.INVISIBLE);

            questionTextView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    if(questionList.get(listIndex).getRating() == 1){
                        questionTextView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        questionList.set(listIndex,new QuestionWord(questionList.get(listIndex).getQuestion(),0));
                    } else {
                        questionTextView.setBackgroundColor(Color.parseColor("#D6E4F1"));
                        questionList.set(listIndex,new QuestionWord(questionList.get(listIndex).getQuestion(),1));
                    }
                    mOnClickListener.onSaveClick(questionList);
                    Log.v("CHECKPOSITION","listindex inside onClick "+listIndex);
                }
            });

            if(questionList.get(listIndex).getRating() == 1){
                questionTextView.setBackgroundColor(Color.parseColor("#D6E4F1"));
            } else {
                questionTextView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            }

            if (listIndex==0){
                questionTextView.setVisibility(View.INVISIBLE);
                questionEditTextView.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.VISIBLE);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        questionList.add(new QuestionWord(questionEditTextView.getText().toString(),0));
                        notifyItemChanged(mNumberItems);
                        Log.v("ADD","inside addClick "+mNumberItems);
//                        notifyItemRangeInserted(mNumberItems-1,mNumberItems-1);
                        questionEditTextView.setHint(R.string.new_question);
                        questionEditTextView.setText(null);
                        mOnClickListener.onSaveClick(questionList);
                    }
                });
            }
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View view) {

            int clickedPosition = getAdapterPosition();
//            mOnClickListener.onListItemClick(clickedPosition);
//            view.setBackgroundColor(R.color.PrimaryText);
//            view.setDrawingCacheBackgroundColor(R.color.PrimaryText);
        }
    }

    public void clear(){
        int size = questionList.size();
        questionList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void deleteQuestions(){
        for (int i=mNumberItems-1; i>0; i--){
            if(questionList.get(i).getRating()==1){
                questionList.remove(i);
//                notifyItemRemoved(i);
                notifyDataSetChanged();

                Log.v("DELETE","inside deletequestions "+mNumberItems);
                Log.v("DELETE","inside deletequestions i =  "+i);
            }
        }

    }


}
