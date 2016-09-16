/*
 * Copyright (c) 2016.
 */

package com.monigarr.imzydemo.Demo.network;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.monigarr.imzydemo.Demo.models.FlImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monicapeters on 9/15/16.
 */

public class GetJSON extends AsyncTask<String, Void, List<FlImage>> implements GetRawData.OnDownloadComplete{
    private static final String TAG = "GetJSON";

    private List<FlImage> mPhotoList = null;
    private String mBaseURL;
    private String mLanguage;
    private boolean mMatchAll;

    private final OnDataAvailable mCallBack;

    private boolean runningOnSameThread = false;

    public interface OnDataAvailable{
        void onDataAvailable(List<FlImage> data, Download status);
    }

    public GetJSON(OnDataAvailable callBack, String baseURL, String language, boolean matchAll) {
        Log.d(TAG, "GetJSON called");
        mBaseURL = baseURL;
        mCallBack = callBack;
        mLanguage = language;
        mMatchAll = matchAll;
    }

    @Override
    protected void onPostExecute(List<FlImage> photos) {
        Log.d(TAG, "onPostExecute starts");

        if(mCallBack != null){
            mCallBack.onDataAvailable(mPhotoList, Download.OK);
        }
        Log.d(TAG, "onPostExecute ends");
    }

    @Override
    protected List<FlImage> doInBackground(String... params) {
        Log.d(TAG, "doInBackground starts");
        String destinationUri = createUri(params[0], mLanguage, mMatchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground ends");
        return mPhotoList;
    }

    private String createUri(String searchCriteria, String lang, boolean matchAll){
        Log.d(TAG, "createUri starts");

        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", lang)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1").build().toString();
    }

    @Override
    public void onDownloadComplete(String data, Download status) {
        Log.d(TAG, "onDownloadComplete starts. Status = " + status);

        if(status == Download.OK){
            mPhotoList = new ArrayList<>();

            try{
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");

                for (int i = 0; i < itemsArray.length(); i++){
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");

                    String link = photoUrl.replaceFirst("_m.", "_b.");

                    FlImage photoObject = new FlImage(title, author, authorId, link, tags, photoUrl);
                    mPhotoList.add(photoObject);

                    Log.d(TAG, "onDownloadComplete " + photoObject.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json data " + e.getMessage());
                status = Download.FAILED_OR_EMPTY;
            }
        }

        if(runningOnSameThread && mCallBack != null){
            mCallBack.onDataAvailable(mPhotoList, status);
        }

        Log.d(TAG, "onDownloadComplete: ends");
    }
}