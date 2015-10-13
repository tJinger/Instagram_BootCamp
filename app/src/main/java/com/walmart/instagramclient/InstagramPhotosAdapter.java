package com.walmart.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tjing on 10/6/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the data item for this position
        InstagramPhoto photo = getItem(position);
        //Check if we are using a recycled view
        if (convertView == null) {
            //Create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        //Lookup the views for populating the data
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        RoundedImageView ivUserImage = (RoundedImageView) convertView.findViewById(R.id.ivUserImage);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvCommentName1 = (TextView) convertView.findViewById(R.id.tvCommentName1);
        TextView tvComment1 = (TextView) convertView.findViewById(R.id.tvComment1);
        TextView tvCommentName2 = (TextView) convertView.findViewById(R.id.tvCommentName2);
        TextView tvComment2 = (TextView) convertView.findViewById(R.id.tvComment2);
        //Insert the model data into each of the view item
        tvUserName.setText(photo.userName);
        ivUserImage.setImageResource(0);
        Picasso.with(getContext()).load(photo.profile_picture).placeholder(R.mipmap.ic_launcher).into(ivUserImage);
        tvName.setText(photo.userName);
        tvCaption.setText(photo.caption);
        //Clear out the imageView
        ivPhoto.setImageResource(0);
        //Insert the image using Picasso
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.mipmap.ic_launcher).into(ivPhoto);
        tvLikes.setText(photo.likesCount + " likes");
        tvCommentName1.setText(photo.commentName1);
        tvComment1.setText(photo.comment1);
        tvCommentName2.setText(photo.commentName2);
        tvComment2.setText(photo.comment2);
        return convertView;
    }
}
