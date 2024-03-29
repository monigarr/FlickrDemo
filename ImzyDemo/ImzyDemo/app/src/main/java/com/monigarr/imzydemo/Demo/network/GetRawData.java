/*
 * Copyright (c) 2016.
 */

package com.monigarr.imzydemo.Demo.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by monicapeters on 9/15/16.
 */

class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";

    private Download mDownloadStatus;
    private final OnDownloadComplete mCallback;

    interface OnDownloadComplete{
        void onDownloadComplete(String data, Download status);
    }

    GetRawData(OnDownloadComplete callBack) {
        this.mDownloadStatus = Download.IDLE;
        mCallback = callBack;
    }

    void runInSameThread(String s){
        Log.d(TAG, "runInSameThread starts");
        onPostExecute(doInBackground(s));
        Log.d(TAG, "runInSameThread ends");
    }

    @Override
    protected void onPostExecute(String s) {
        //Log.d(TAG, "onPostExecute: parameter = " + s);
        if(mCallback != null)
            mCallback.onDownloadComplete(s, mDownloadStatus);
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        if(strings == null){
            mDownloadStatus = Download.NOT_INITIALISED;
            return null;
        }

        try{
            mDownloadStatus = Download.PROCESSING;
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was " + response);

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while(null != (line = reader.readLine())){
                result.append(line).append("\n");
            }

            mDownloadStatus = Download.OK;
            return result.toString();
        } catch (MalformedURLException e){
            Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage());
        } catch (IOException e){
            Log.e(TAG, "doInBackground: IO Exception reading data: " + e.getMessage());
        } catch (SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception. Permission setup in Manifest? " + e.getMessage());
        } finally {
            if(connection != null){
                connection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                } catch (IOException e){
                    Log.e(TAG, "doInBackground: Error closing stream " + e.getMessage());
                }
            }
        }

        mDownloadStatus = Download.FAILED_OR_EMPTY;
        return null;
    }
}