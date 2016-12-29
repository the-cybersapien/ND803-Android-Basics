package xyz.cybersapien.givemebooks;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView hintingView;
    ListView resultsView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hintingView = (TextView) findViewById(R.id.hint_textView);
        resultsView = (ListView) findViewById(R.id.books_list);
        hintingView.setVisibility(View.VISIBLE);
        resultsView.setVisibility(View.GONE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        final ImageButton searchButton = (ImageButton) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchQueryText = (EditText) findViewById(R.id.search_src_text);
                String searchQuery = searchQueryText.getText().toString();
                GetBooksASyncTask getBooks = new GetBooksASyncTask();
                getBooks.execute(searchQuery);
            }
        });

    }

    private class GetBooksASyncTask extends AsyncTask<String, Void, ArrayList<Book>>{

        /**
         * The doInBackground AsyncTask takes the search query as a parameter
         * and then uses the Uri.Builder to create a URL to fetch the JSON data
         * @param params single parameter in the form of the search Query
         * @return returns an ArrayList of Books to be used by the PostExecute() Method.
         */
        @Override
        protected ArrayList<Book> doInBackground(String... params) {
            Uri.Builder BooksJSONRequestBuilder = new Uri.Builder();
            try {
                BooksJSONRequestBuilder.scheme("https")
                        .authority("www.googleapis.com")
                        .appendPath("books")
                        .appendPath("v1")
                        .appendPath("volumes")
                        .appendQueryParameter("q", URLEncoder.encode(params[0],"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String jsonResponse;
            try {
                URL booksRequestURL = new URL(BooksJSONRequestBuilder.build().toString());
                jsonResponse = makeHttpRequest(booksRequestURL);
                return parseJSONList(jsonResponse);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        /**
         * The onPostExecute method in invoked after the execution of the doInBackGround Process.
         * This method's task is to process the data Gathered by the AsyncTask and then it sets the visibility of the required Views
         * i.e, It sets the BooksAdapter to the resultsView List and then it hides the hint TextView if the task is successful.
         * Else, it keeps the results List hidden and then Updates the Hint TextView.
         * @param books ArrayList of books to work with.
         */
        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            if (books != null){
                BooksAdapter booksAdapter = new BooksAdapter(getApplicationContext(), books);
                resultsView.setAdapter(booksAdapter);
                resultsView.setVisibility(View.VISIBLE);
                hintingView.setVisibility(View.GONE);
            } else {
                resultsView.setVisibility(View.GONE);
                hintingView.setText(R.string.error_hinting);
                hintingView.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);
        }

        /**
         * This Method is used for a little abstraction on our part.
         * It retrieves all the data from the server and then calls the readFromStream() method to parse it into String form so that it can be further processed.
         * @param url takes the URL to make the GET request to
         * @return returns a String containing the complete JSON information from the server.
         * @throws IOException Exception thrown in case of problem Connecting to given URL.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection)   url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                if(urlConnection.getResponseCode()!=200){
                    jsonResponse = "";
                } else {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Reads data from RAW input stream and converts it into string.
         * @param inputStream Raw input stream for the server
         * @return String after parsing the input Stream to text format
         * @throws IOException
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * This method does the parsing of our JSONData from the server to get the relevant information and creating a list of books from that data
         * @param JSONString String Containing data in JSON format
         * @return an ArrayList of Books after Parsing the required information from the JSONString.
         */
        private ArrayList<Book> parseJSONList(String JSONString){
            ArrayList<Book> booksList = new ArrayList<>();

            try{
                //Create root JSON Object
                JSONObject root = new JSONObject(JSONString);
                //Get the items Array from the root data
                JSONArray itemsArray = root.getJSONArray("items");
                //Processing all the items in the array iteratively
                for(int i=0;i<itemsArray.length();i++) {

                    //Get the current Book from the Array
                    JSONObject currentBook = itemsArray.getJSONObject(i);

                    //Get the volume Info Object from the current book
                    JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                    //Get the title of the book
                    String bookTitle = volumeInfo.getString("title");

                    //Get the Subtitle of the book, if any, or else use the default null value
                    String bookSubtitle = volumeInfo.optString("subtitle",Book.NO_SUBTITLE);

                    //Get the Image URL of the book, if any or use use the default null value
                    JSONObject bookImagesObject = volumeInfo.optJSONObject("imageLinks");
                    String bookImageURL;
                    if (bookImagesObject!=null) {
                        bookImageURL = bookImagesObject.optString("thumbnail", Book.NO_IMAGE);
                    } else {
                        bookImageURL = Book.NO_IMAGE;
                    }

                    //Since there is a possibility of multiple Authors, we use a StringBuilder to create a String of authors
                    StringBuilder authorBuilder = new StringBuilder();
                    JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                    String bookAuthors;
                    if (authorsArray!=null){
                        for (int j=0;j<authorsArray.length();j++){
                            authorBuilder.append(authorsArray.get(j));

                            if (j+1<authorsArray.length()){ /*We only require a "," at the end of the author's name if more authors are present*/
                                authorBuilder.append(", ");
                            }
                        }
                        bookAuthors = authorBuilder.toString();
                    } else {
                        bookAuthors = Book.NO_AUTHOR;
                    }

                    //finally add a new Book to the booksList with all the relevant information
                    booksList.add(new Book(bookTitle,bookSubtitle, bookAuthors, bookImageURL));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return booksList;
        }
    }

}


