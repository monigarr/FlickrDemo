/*
 * Copyright (c) 2016.
 */

package com.monigarr.imzydemo.Demo.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.monigarr.imzydemo.Demo.models.FlImage;
import com.monigarr.imzydemo.R;
import com.squareup.picasso.Picasso;

/**
 * Created by monicapeters on 9/15/16.
 */

public class ImageDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        activateToolbar(true);

        Intent intent = getIntent();
        FlImage photo = (FlImage) intent.getSerializableExtra(IMAGE_TRANSFER);
        if(photo != null){
            Resources resources = getResources();

            TextView photoTitle = (TextView) findViewById(R.id.photo_title);
            photoTitle.setText(resources.getString(R.string.photo_title_text, photo.getTitle()));

            TextView photoTags = (TextView) findViewById(R.id.photo_tags);
            photoTags.setText(resources.getString(R.string.photo_tags_text, photo.getTags()));

            TextView photoAuthor = (TextView) findViewById(R.id.photo_author);
            photoAuthor.setText(photo.getAuthor());

            ImageView photoImage = (ImageView) findViewById(R.id.photo_image);
            Picasso.with(this).load(photo.getLink())
                    .error(R.drawable.placeholder_image)
                    .placeholder(R.drawable.placeholder_image)
                    .into(photoImage);
        }

    }
}