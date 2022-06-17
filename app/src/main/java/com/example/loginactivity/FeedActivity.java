package com.example.loginactivity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    ParseQuery<Post> query;
    ArrayList<Post> allPosts = new ArrayList<>();
    FeedAdapter feedAdapter = new FeedAdapter(this,allPosts);
    RecyclerView rvPosts;
    SwipeRefreshLayout swipeContainer;
    int offset = 0;
    int currentLimit = 0;
    private EndlessRecyclerViewScrollListener scrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        queryPost(currentLimit);
        rvPosts = findViewById(R.id.rvFragmentsPosts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPosts.setLayoutManager(linearLayoutManager);
        rvPosts.setAdapter(feedAdapter);
        rvPosts.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        swipeContainer = findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPost(0);
                currentLimit = 0;
                //swipeContainer.setRefreshing(false);
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);
    }

    private void queryPost(int offset){
        query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder("createdAt");
        if (allPosts.size() > 0){
            Post lastPost = allPosts.get(allPosts.size() - 1);
            query.whereLessThan("createdAt",lastPost.getCreatedAt());
        }
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {

                if( e != null){
                    Log.e(TAG,"Issue with getting posts",e);
                    return;
                }
                swipeContainer.setRefreshing(false);
                for (Post post: posts) {
                    Log.i(TAG, "Post: " + post.getDescription());

                }
                int  post_size = posts.size() ;
                allPosts.addAll(posts);
                feedAdapter.notifyDataSetChanged();

            }
        });

    }

    public void loadNextDataFromApi(int offset) {

        queryPost(offset);

        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }
}
