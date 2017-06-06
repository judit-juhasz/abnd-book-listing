package name.juhasz.judit.udacity.googlebooks;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final int BOOKS_LOADER_ID = 1;

    private static final String SEARCH_TERM_KEY = "Search Term";

    private EditText mSearchTermEditText;
    private TextView mMessageDisplayTextView;
    private ProgressBar mLoadingIndicator;
    private ListView mBookListView;

    private BookAdapter mBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchTermEditText = (EditText) findViewById(R.id.et_search_term);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mMessageDisplayTextView = (TextView) findViewById(R.id.tv_message_display);

        mBookListView = (ListView) findViewById(R.id.lv_books);
        mBookAdapter = new BookAdapter(this, new ArrayList<Book>());
        mBookListView.setAdapter(mBookAdapter);

        showMessage(getString(R.string.error_no_books));
    }

    private void showMessage(String message) {
        mLoadingIndicator.setVisibility(View.GONE);
        mBookListView.setVisibility(View.GONE);

        mMessageDisplayTextView.setText(message);
        mMessageDisplayTextView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mMessageDisplayTextView.setVisibility(View.GONE);
        mBookListView.setVisibility(View.GONE);

        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showBooks(List<Book> books) {
        mLoadingIndicator.setVisibility(View.GONE);
        mMessageDisplayTextView.setVisibility(View.GONE);

        mBookAdapter.clear();
        mBookAdapter.addAll(books);
        mBookListView.setVisibility(View.VISIBLE);
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
        if (null == data || data.isEmpty()) {
            showMessage(getString(R.string.error_no_books));
        } else {
            showBooks(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        showMessage(getString(R.string.error_no_books));
    }
}
