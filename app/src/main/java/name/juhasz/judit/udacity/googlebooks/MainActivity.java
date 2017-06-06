package name.juhasz.judit.udacity.googlebooks;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final int BOOKS_LOADER_ID = 1;

    private static final String SEARCH_TERM_KEY = "Search Term";

    private EditText mSearchTermEditText;
    private TextView mMessageDisplayTextView;
    private ProgressBar mLoadingIndicator;
    private ListView mBookListView;
    private RelativeLayout mSearchBar;

    private BookAdapter mBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchTermEditText = (EditText) findViewById(R.id.et_search_term);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mMessageDisplayTextView = (TextView) findViewById(R.id.tv_message_display);
        mSearchBar = (RelativeLayout) findViewById(R.id.rl_search_bar);

        mBookListView = (ListView) findViewById(R.id.lv_books);
        mBookAdapter = new BookAdapter(this, new ArrayList<Book>());
        mBookListView.setAdapter(mBookAdapter);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOKS_LOADER_ID, null, this);

        if (networkInfo == null || !networkInfo.isConnected()) {
            showFullScreenMessage(getString(R.string.error_no_internet));
        } else {
            showMessage(getString(R.string.error_no_books));
        }
    }

    private void showMessage(String message) {
        mLoadingIndicator.setVisibility(View.GONE);
        mBookListView.setVisibility(View.GONE);

        mMessageDisplayTextView.setText(message);
        mMessageDisplayTextView.setVisibility(View.VISIBLE);
    }

    private void showFullScreenMessage(String message) {
        mSearchBar.setVisibility(View.GONE);
        showMessage(message);
    }

    private void showProgressBar() {
        mSearchBar.setVisibility(View.VISIBLE);
        mMessageDisplayTextView.setVisibility(View.GONE);
        mBookListView.setVisibility(View.GONE);

        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showBooks(List<Book> books) {
        mSearchBar.setVisibility(View.VISIBLE);
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
        String searchTerm = null;
        if (null != args) {
            searchTerm = args.getString(SEARCH_TERM_KEY, null);
        }
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
