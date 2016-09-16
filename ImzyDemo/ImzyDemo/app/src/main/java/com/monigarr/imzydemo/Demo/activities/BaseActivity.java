/*
 * Copyright (c) 2016.
 */

package com.monigarr.imzydemo.Demo.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.monigarr.imzydemo.R;

import static java.security.AccessController.getContext;

/**
 * Created by monicapeters on 9/15/16.
 */

class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    static final String FLICKR_QUERY = "FLICKR_QUERY";
    static final String IMAGE_TRANSFER = "IMAGE_TRANSFER";

    void activateToolbar(boolean enableHome){
        Log.d(TAG, "activateToolbar: starts");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null){
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if(toolbar != null){
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }

    }
}