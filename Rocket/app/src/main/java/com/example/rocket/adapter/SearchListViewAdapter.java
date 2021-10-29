package com.example.rocket.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rocket.R;
import com.example.rocket.classes.LocationNames;
import com.example.rocket.fragments.LocationContainerFragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchListViewAdapter extends RecyclerView.Adapter<SearchListViewAdapter.NumberViewHolder> {

    Context mContext;
    LayoutInflater inflater;
    private List<LocationNames> locationNamesList = null;
    private ArrayList<LocationNames> arraylist;

    // For the adapter
    final private listItemClickListener mOnClickListener;
    private static int viewHolderCount;
    private int mNumberItems;

    public interface listItemClickListener{
        void onListItemClick(int clickedItemIndex, int locationId, String imageUrl, String locationName);
    }

    public SearchListViewAdapter(List<LocationNames> locationNamesList, listItemClickListener listener) {

        Log.v("ADAPTER","list is = "+locationNamesList.size());
        // for the search item
//        mContext = context;
        this.locationNamesList = locationNamesList;
        Log.v("ADAPTER","list is = "+this.locationNamesList.size());
//        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<LocationNames>();
        this.arraylist.addAll(locationNamesList);

        //for the adapter
        mNumberItems = locationNamesList.size();
        mOnClickListener = listener;
        viewHolderCount = 0;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutForListItem = R.layout.search_list_view_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutForListItem,viewGroup,shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        Log.v("ADAPTER","Inside onCreateViewHolder");

        //set View Holder
        return viewHolder;
    }

    @Override
    public void onBindViewHolder( NumberViewHolder holder, int position) {
        Log.v("ADAPTER","position is = "+position);
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        Log.v("ADAPTER","Count is = "+mNumberItems);
        Log.v("ADAPTER","Count of locationNameList  is = "+locationNamesList.size());
//        Log.v("ADAPTER","Count of  arraylist is = "+arraylist.size());
        mNumberItems = locationNamesList.size();
        arraylist.clear();
        arraylist.addAll(locationNamesList);
        return mNumberItems;
    }

    //    public class ViewHolder {
//        TextView name;
//    }

//    @Override
//    public int getCount() {
//        return locationNamesList.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return locationNamesList.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup parent) {
//        final ViewHolder holder;
//        if (view == null) {
//            holder = new ViewHolder();
//            view = inflater.inflate(R.layout.search_list_view_item, null);
//            // Locate the TextViews in listview_item.xml
//            holder.name = (TextView) view.findViewById(R.id.name);
//            view.setTag(holder);
//        } else {
//            holder = (ViewHolder) view.getTag();
//        }
//        // Set the results into TextViews
//        holder.name.setText(locationNamesList.get(position).getLocationName());
//        return view;
//    }

    // Filter Class
    public void filter(String charText) {
        Log.v("ADAPTER","Inside Filter "+locationNamesList.size());
        charText = charText.toLowerCase(Locale.getDefault());
        arraylist.clear();
        arraylist.addAll(locationNamesList);
        locationNamesList.clear();
        if (charText.length() == 0) {
            locationNamesList.addAll(arraylist);
        } else {
            for (LocationNames wp : arraylist) {
                if (wp.getLocationName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    locationNamesList.add(wp);
                }
            }
        }
        //for the adapter
        //modify the size
        mNumberItems = locationNamesList.size();
        notifyDataSetChanged();
    }

    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        TextView type;
        TextView location;
        RatingBar ratingBar;
        ImageView image;

        public NumberViewHolder( View itemView) {
            super(itemView);
            Log.v("ADAPTER","Inside NumberViewHolder");
            image = (ImageView) itemView.findViewById(R.id.image_adapter_imageview);
            name = (TextView) itemView.findViewById(R.id.name_adapter_textview);
            type = (TextView) itemView.findViewById(R.id.type_adapter_textview);
            location = (TextView) itemView.findViewById(R.id.location_adapter_textview);
            ratingBar = (RatingBar) itemView.findViewById(R.id.adapter_rating_bar);
            itemView.setOnClickListener(this);
        }

        void bind (int listIndex){
            Log.v("ADAPTER","listIndex is = "+listIndex);
            Log.v("ADAPTER","locationNamesList size is = "+locationNamesList.size());
            Log.v("ADAPTER","Name is = "+locationNamesList.get(listIndex).getLocationName());

//            String photo_url_str = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1267&q=80";
            String photo_url_str = locationNamesList.get(listIndex).getImageURL();
            new DownloadImageFromInternet(image).execute(photo_url_str);

            name.setText(locationNamesList.get(listIndex).getLocationName());
            type.setText(locationNamesList.get(listIndex).getLocationType());
            location.setText(locationNamesList.get(listIndex).getLocationLocation());
            ratingBar.setRating(locationNamesList.get(listIndex).getLocationrating());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Log.v("ADAPTER","Inside the Adapter click name is = "+locationNamesList.get(clickedPosition).getLocationName());
            Log.v("ADAPTER","Inside the Adapter click ID is = "+locationNamesList.get(clickedPosition).getLocationID());
            mOnClickListener.onListItemClick(clickedPosition, locationNamesList.get(clickedPosition).getLocationID(),
                    locationNamesList.get(clickedPosition).getImageURL(),
                    locationNamesList.get(clickedPosition).getLocationName());
        }

        ///////// Photo Load ////////////
        private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
            ImageView imageView;
            public DownloadImageFromInternet(ImageView imageView) {
                this.imageView=imageView;
//                Toast.makeText(mContext, "Please wait, it may take a few minute...",Toast.LENGTH_SHORT).show();
            }
            protected Bitmap doInBackground(String... urls) {
                String imageURL=urls[0];
                Bitmap bimage=null;
                try {
                    InputStream in=new java.net.URL(imageURL).openStream();
                    bimage= BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error Message", e.getMessage());
                    e.printStackTrace();
                }
                return bimage;
            }
            protected void onPostExecute(Bitmap result) {

                Log.v("CHECKPOINTIMAGE","onPost Image");
                imageView.setImageBitmap(result);
            }
        }
        ////////Load photo finish ////////
    }

    public void clear(){
        int size = locationNamesList.size();
        locationNamesList.clear();
        notifyItemRangeRemoved(0, size);
    }





}
