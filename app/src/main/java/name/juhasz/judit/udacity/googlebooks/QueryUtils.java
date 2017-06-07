package name.juhasz.judit.udacity.googlebooks;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

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
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static final String JSON_KEY_BOOKS = "items";
    private static final String JSON_KEY_BOOK_INFO = "volumeInfo";
    private static final String JSON_KEY_BOOK_TITLE = "title";
    private static final String JSON_KEY_BOOK_AUTHORS = "authors";

    private static final String URL_ENCODING = "utf-8";

    private QueryUtils() {
    }

    public static List<Book> fetchBookData(Context context, String searchTerm) {
        String requestUrl = buildUrlFromSearchTerm(context, searchTerm);
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Book> books = extractFeatureFromJson(context, jsonResponse);

        return books;
    }

    private static String buildUrlFromSearchTerm(Context context, String searchTerm) {
        String builtUrl = null;

        try {
            String encodedSearchTerm = URLEncoder.encode(searchTerm, URL_ENCODING);
            builtUrl = context.getString(R.string.query_url_template, encodedSearchTerm);
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, "Error with building URL from search term ", e);
        }

        return builtUrl;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    private static List<Book> extractFeatureFromJson(Context context, String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            JSONArray bookArray = baseJsonResponse.optJSONArray(JSON_KEY_BOOKS);

            for (int i = 0; null != bookArray && i < bookArray.length(); i++) {
                JSONObject currentBook = bookArray.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject(JSON_KEY_BOOK_INFO);
                String title = volumeInfo.optString(JSON_KEY_BOOK_TITLE,
                        context.getString(R.string.unknown_title));

                JSONArray authorArray = volumeInfo.optJSONArray(JSON_KEY_BOOK_AUTHORS);
                ArrayList<String> authors = extractAuthorsFromJsonArray(context, authorArray);

                Book book = new Book(title, authors);
                books.add(book);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }

        return books;
    }

    private static ArrayList<String> extractAuthorsFromJsonArray(Context context,
                                                                 JSONArray authorArray) {
        String unknownAuthor = context.getString(R.string.unknown_author);

        ArrayList<String> authors = new ArrayList<>();

        if (null != authorArray && authorArray.length() != 0) {
            for (int i = 0; i < authorArray.length(); i++) {
                try {
                    authors.add(authorArray.getString(i));
                } catch (JSONException e) {
                    authors.add(unknownAuthor);
                }
            }
        } else {
            authors.add(unknownAuthor);
        }

        return authors;
    }
}
