package com.example.youtubeapidemo;

import android.content.Context;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeConnector {
    private YouTube youtube;
    private YouTube.Videos.List query;

    public static final String KEY = "AIzaSyDL2ZqeB3uGYmqv8jJgITwS4N9eFyBz4IA";

    public YoutubeConnector(Context context) {
        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        try {
            query = youtube.videos().list("id, snippet, contentDetails");
            query.setKey(KEY);
            query.setChart("mostPopular");
            //query.setType("video");

            query.setFields("items(id,snippet/title,snippet/description,snippet/thumbnails/default/url,snippet/categoryId, contentDetails/duration)");
        } catch (IOException e) {
            Log.d("YC", "Could not initialize: " + e.getMessage());
        }
    }

    public List<VideoItem> search(String keywords) {
        //query.setQ(keywords);

        try {
            VideoListResponse response = query.execute();
            List<Video> results = response.getItems();
            List<VideoItem> items = new ArrayList<VideoItem>();
            System.out.println("SIZE: " + results.size());

            for (Video result : results) {
                VideoItem item = new VideoItem();
                item.setTitle(result.getSnippet().getTitle());
                item.setDescription(result.getSnippet().getDescription());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setId(result.getId());
                System.out.println("ID--- :" + result.getId());
                System.out.println("ZZZZZ: " + result.getKind());
                System.out.println("YYYYY: " + result.getSnippet().getDescription());
                System.out.println("duration"+result.getContentDetails().getDuration());
                System.out.println("url id "+ result.getId());
                items.add(item);
            }


            return items;
        } catch (IOException e) {
            Log.d("YC", "Could not search: " + e);
            return null;
        }
    }
}