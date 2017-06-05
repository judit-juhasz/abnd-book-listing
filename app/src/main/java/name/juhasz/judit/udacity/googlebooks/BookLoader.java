package name.juhasz.judit.udacity.googlebooks;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private static final String QUERY_URL =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=10";

    public BookLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        List<Book> books = QueryUtils.fetchBookData(QUERY_URL);
        return books;
    }
}
