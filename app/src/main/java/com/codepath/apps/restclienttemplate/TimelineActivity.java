package com.codepath.apps.restclienttemplate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    RecyclerView rvTweets;
    private TweetsAdapter adapter;
    private List<Tweet> tweets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client=TwitterApp.getRestClient(this);

        //find the recycler view
        rvTweets=findViewById(R.id.rvTweets);
        //Initialize list of tweets and adapter form the data source
        tweets=new ArrayList<>();
        adapter=new TweetsAdapter(this,tweets);
        // Recycler View seetup: layout manager and setting the adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(adapter);
        populateHomeTimeLine();
    }

    public void populateHomeTimeLine()
    {
        client.getHomeTimeline(new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

               // Log.d("TwitterClient", response.toString());
                //Iterate through the list of tweet
                for(int i=0; i< response.length(); i++)
                {
                    try
                    {
                        JSONObject jsonTweetObject=response.getJSONObject(i);
                        Tweet tweet=Tweet.fromJson(jsonTweetObject);
                        // Add the tweet into our data source
                        tweets.add(tweet);
                        // Notify adapter
                        adapter.notifyItemInserted(tweets.size() - 1);
                    }catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                // Convert each JSON object into a Tweet object

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("TwitterClient", responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("TwitterClient", errorResponse.toString());
            }
        });
    }
}
