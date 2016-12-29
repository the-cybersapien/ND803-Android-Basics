package xyz.cybersapien.newsdroid;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Story>> {

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int STORY_LOADER_ID = 1;

    /*Log Tag for the Activity*/
    private static final String LOG_TAG = MainActivity.class.getName();

    /*Endpoint of the URL to The Guardian*/
    private static final String GUARDIAN_URL = "https://content.guardianapis.com/";

    /*Adapter for the stories*/
    private StoryAdapter storyAdapter;

    /*Create the Recycler View*/
    private RecyclerView storyListView;

    /*Reference to the progress Bar*/
    private ProgressBar progressBar;

    /*Get a reference to the LoaderManager to intereact with the Loaders*/
    private LoaderManager loaderManager;

    /*Reference to the error(hint) TextView*/
    private TextView hintView;

    /*Reference the stories.*/
    List<Story> stories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loaderManager = getLoaderManager();

        //Initialize Progress bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Initialize TextView
        hintView = (TextView) findViewById(R.id.errorHint);

        //Create RecyclerView
        storyListView = (RecyclerView) findViewById(R.id.stories_list);
        storyListView.setVisibility(View.GONE);

        //Create Story list;
        stories = new ArrayList<>();
        //get StoryAdapter
        storyAdapter = new StoryAdapter(this, stories);

        storyListView.setAdapter(storyAdapter);

        storyListView.setLayoutManager(new LinearLayoutManager(this));

        startLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void startLoad(){
        //Create the connectivity Manager and NetworkInfo  objects to check for Network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo!=null && networkInfo.isConnected()){
            //Initialize the loader. Pass the itn ID constant defined above and pass in null
            loaderManager.initLoader(STORY_LOADER_ID, null, this);
        } else {
            hintView.setText(R.string.no_internet);
            progressBar.setVisibility(View.GONE);
            hintView.setVisibility(View.VISIBLE);
            storyListView.setVisibility(View.GONE);
        }
    }


    @Override
    public Loader<List<Story>> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        hintView.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String section = sharedPreferences.getString(getString(R.string.settings_section_key),
                getString(R.string.settings_section_default_value));
        String pageSize = sharedPreferences.getString(getString(R.string.settings_number_key), getString(R.string.settings_number_default_value));

        Uri baseUri = Uri.parse(GUARDIAN_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(section);
        uriBuilder.appendQueryParameter("format", "json");
        /*Number of Items on this page*/
        uriBuilder.appendQueryParameter("page-size",pageSize);
        /*Extra data to display, such as by-line, image and additional text*/
        uriBuilder.appendQueryParameter("show-fields","trailText,thumbnail,byline");
        /**/
        uriBuilder.appendQueryParameter("api-key","test");

        return new StoryLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Story>> loader, List<Story> storyList) {
        storyAdapter.clearData();
        if (storyList!=null && !storyList.isEmpty()){
            stories.addAll(storyList);
            storyAdapter.notifyDataSetChanged();
            storyListView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            hintView.setVisibility(View.GONE);
        } else {
            storyListView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            hintView.setVisibility(View.VISIBLE);
            hintView.setText(R.string.error_getting_data);
        }
    }

    //On Reset we want to clear out all data.
    @Override
    public void onLoaderReset(Loader<List<Story>> loader) {
        storyAdapter.clearData();
    }
}
