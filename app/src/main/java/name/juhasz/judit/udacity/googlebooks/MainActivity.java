package name.juhasz.judit.udacity.googlebooks;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final int BOOKS_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickSearch(View view) {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.restartLoader(BOOKS_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, "ios swift");
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        TextView searchResultTextView = (TextView) findViewById(R.id.tv_search_result);

        String books = "";
        for (Book book : data) {
            books = books + book.getTitle() + "\n";
        }

        searchResultTextView.setText(books);
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {

    }
}
