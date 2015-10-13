package com.walmart.instagramclient;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "6383ffe0c644483880df4f4319ead44a";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_title);
        setContentView(R.layout.activity_photos);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        photos = new ArrayList<InstagramPhoto>();
        //1. Create the adapter linking it to the source
        aPhotos = new InstagramPhotosAdapter(this, photos);
        //2. Find the listView from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        //3. Set the adapter binding it to the listView
        lvPhotos.setAdapter(aPhotos);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //Send out API request to popular photos
        fetchPopularPhotos();
    }

    //Trigger API Request
    public void fetchPopularPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        Log.d("DEBUG", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            //onSuccess
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("DEBUG", response.toString());
                JSONArray photosJSON = new JSONArray();
                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.userName = photoJSON.getJSONObject("user").getString("username");
                        if (photoJSON.getJSONObject("caption") != null)
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.profile_picture = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");

                        JSONArray commentJSON = photoJSON.getJSONObject("comments").getJSONArray("data");
                        int commentSize = commentJSON.length();
                        if (commentSize > 0) {
                            JSONObject comment1 = commentJSON.getJSONObject(commentSize - 1);
                            photo.commentName1 = comment1.getJSONObject("from").getString("username");
                            photo.comment1 = comment1.getString("text");
                        }
                        if (commentSize > 1) {
                            JSONObject comment2 = commentJSON.getJSONObject(commentSize - 2);
                            photo.commentName2 = comment2.getJSONObject("from").getString("username");
                            photo.comment2 = comment2.getString("text");
                        }
                        photos.add(photo);
                        swipeContainer.setRefreshing(false);

                    }
                } catch (JSONException e) {
                    Log.d("DEBUG", "Fetch timeline error: " + e.toString());
                }

                //callback
                aPhotos.notifyDataSetChanged();
            }

            //onFailure
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", "Fetch timeline error: " + throwable.toString());
            }
        });
    }
}
