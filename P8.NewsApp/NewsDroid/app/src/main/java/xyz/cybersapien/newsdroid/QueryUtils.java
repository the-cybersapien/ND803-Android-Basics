package xyz.cybersapien.newsdroid;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ogcybersapien on 6/10/16.
 * This Class is a helper Class to be used for formatting and getting data from the network.
 * The Sole Purpose of this class is to keep the main classes clean and clutter free.
 */

public final class QueryUtils {


    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Custom static method to get the date from the Given format
     * @param dateTime String value of DateTime in format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     * @return Date formatted to the phone's settings
     */
    public static String getDate(String dateTime) {
        if (dateTime != null) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date thisDate = simpleDateFormat.parse(dateTime);
                dateTime = SimpleDateFormat.getDateInstance().format(thisDate);
                } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dateTime;
    }

    public static List<Story> fetchStories(String queryUrl) {

        URL url = createURL(queryUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem getting HTTP data", e);
        }

        return getDataFromJson(jsonResponse);
    }

    /**
     * Return the URL from the string.
     * This method is used to give the fetchStories method a little abstraction and make sure that there is no crash in case of an error
     * @param urlString String url to convert to URL Object
     * @return URL object after conversion from String.
     */
    private static URL createURL(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Make an HTTP request from given URL and return a String as the response
     * @param url URL to get the data from
     * @return returns JSON String from data from the server
     * */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        /*If url is null no point conitnuing, so return early*/
        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //If request was successful, server will send a response code 200
            //Else it will send an error code. In case of the former, read the input stream.
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Stories from JSON result.");
        } finally {
            /*Close the urlConnection and input stream after we're done getting data*/
            if (urlConnection != null){
                urlConnection.disconnect();
            }

            if (inputStream != null){
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream!=null){
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while (line!=null){
                output.append(line);
                line = reader.readLine();
            };
        }
        return output.toString();
    }

    public static List<Story> getDataFromJson(String storyJSON){

        //If storyJSON is empty, return early
        if (storyJSON.isEmpty()){
            return null;
        }

        //Create an Empty List of Stories to Start
        List<Story> stories = new ArrayList<Story>();

        //Start parsing JSON data to add Stories to.
        try{
            //Create the root JSON Object from the reponse String
            JSONObject rootObject = new JSONObject(storyJSON);

            //Extract the response JSONOnject from the root Object
            JSONObject responseObject = rootObject.getJSONObject("response");

            //Get result Array from JSON
            JSONArray resultsArray = responseObject.getJSONArray("results");

            for (int i=0;i<resultsArray.length();i++){
                JSONObject currentStory = resultsArray.getJSONObject(i);

                //Get story Title
                String title = currentStory.getString("webTitle");

                //Get Story publication Date
                String pubDate = getDate(currentStory.getString("webPublicationDate"));

                //Get Story URL
                String storyURL = currentStory.getString("webUrl");

                //Get the fields JSON Object
                JSONObject currentStoryField = currentStory.getJSONObject("fields");

                //Get associated Image URL
                String storyImageURL = currentStoryField.getString("thumbnail");

                //Get the Authors Details
                String storyByLine = currentStoryField.optString("byline", Story.byLineDefault);

                //Get the trail Text
                String trailText = currentStoryField.getString("trailText");

                stories.add(new Story(title, trailText, storyImageURL, storyByLine, storyURL,pubDate));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stories;
    }
}
