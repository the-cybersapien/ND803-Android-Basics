package xyz.cybersapien.newsdroid;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by ogcybersapien on 7/10/16.
 * Custom Loader for the stories
 */

public class StoryLoader extends AsyncTaskLoader<List<Story>> {

    /* Query URL */
    private String queryUrl;

    public StoryLoader(Context context, String url) {
        super(context);
        queryUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Story> loadInBackground() {
        if (queryUrl == null){
            return null;
        }

        //Perform the network request, parse the response, and extract the stories.
        List<Story> stories = QueryUtils.fetchStories(queryUrl);
        return stories;
    }
}
