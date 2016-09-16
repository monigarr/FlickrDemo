/*
 * Copyright (c) 2016.
 */

package com.monigarr.imzydemo.Demo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.monigarr.imzydemo.Demo.models.FlImage;
import com.monigarr.imzydemo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by monicapeters on 9/15/16.
 */

public class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder>{
    private static final String TAG = "FlickrRecyclerViewAdapter";

    private List<FlImage> mPhotoList;
    private Context mContext;

    public FlickrRecyclerViewAdapter(Context context, List<FlImage> photoList) {
        mContext = context;
        mPhotoList = photoList;
    }

    @Override
    public FlickrImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlickrImageViewHolder holder, int position) {
        if(mPhotoList == null || mPhotoList.size() == 0){
            holder.thumbnail.setImageResource(R.drawable.placeholder_image);
            holder.title.setText(R.string.empty_photo);
        } else {
            FlImage photoItem = mPhotoList.get(position);
            Log.d(TAG, "onBindViewHolder" + photoItem.getTitle() + " > " + position);
            Picasso.with(mContext).load(photoItem.getImage())
                    .error(R.drawable.placeholder_image)
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.thumbnail);
            holder.title.setText(photoItem.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return ((mPhotoList != null) && (mPhotoList.size() != 0) ? mPhotoList.size() : 1);
    }

    public void loadNewData(List<FlImage> newPhotos){
        mPhotoList = newPhotos;
        notifyDataSetChanged();
    }

    public FlImage getPhoto(int position){
        return ((mPhotoList != null) && (mPhotoList.size() != 0) ? mPhotoList.get(position) : null);
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;

        FlickrImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: starts");
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}