package name.juhasz.judit.udacity.googlebooks;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mSearchTerm;

    public BookLoader(Context context, String searchTerm) {
        super(context);
        mSearchTerm = searchTerm;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        List<Book> books = null;
        if (null != mSearchTerm && !mSearchTerm.isEmpty()) {
            books = QueryUtils.fetchBookData(getContext(), mSearchTerm);
        }
        return books;
    }
}
