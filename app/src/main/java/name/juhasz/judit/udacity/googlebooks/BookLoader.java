package name.juhasz.judit.udacity.googlebooks;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    public BookLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        return getDummyData();
    }

    // Dummy Data, do not need to use strings.xml
    private List<Book> getDummyData() {
        ArrayList<String> authors = new ArrayList<>();
        authors.add("Judit Juhasz");

        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book("Android by Example", authors));
        books.add(new Book("Android, the best part", authors));

        return books;
    }
}
