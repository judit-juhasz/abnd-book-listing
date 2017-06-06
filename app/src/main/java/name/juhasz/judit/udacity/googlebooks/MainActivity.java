package name.juhasz.judit.udacity.googlebooks;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final int BOOKS_LOADER_ID = 1;
    
    private static final String SEARCH_TERM_KEY = "Search Term";

    private EditText mSearchTermEditText;
    private TextView mSearchResultTextView;
    private View mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchTermEditText = (EditText) findViewById(R.id.et_search_term);
        mLoadingIndicator = findViewById(R.id.loading_indicator);
        mSearchResultTextView = (TextView) findViewById(R.id.tv_search_result);
    }

    private void showProgressBar() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mSearchResultTextView.setVisibility(View.GONE);
    }

    private void showBooks(List<Book> books) {
        mLoadingIndicator.setVisibility(View.GONE);

        String result = "";
        for (Book book : books) {
            result = result + book.getTitle() + "\n";
        }

        mSearchResultTextView.setText(result);

        mSearchResultTextView.setVisibility(View.VISIBLE);
    }

    public void onClickSearch(View view) {
        String searchTerm = mSearchTermEditText.getText().toString().trim();
        if (!searchTerm.isEmpty()) {
            showProgressBar();
            Bundle bundle = new Bundle();
            bundle.putString(SEARCH_TERM_KEY, searchTerm);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.restartLoader(BOOKS_LOADER_ID, bundle, this);
        } else {
            Toast.makeText(this, R.string.error_enter_search_term, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        String searchTerm = args.getString(SEARCH_TERM_KEY);
        return new BookLoader(this, searchTerm);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        showBooks(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {

    }
}
