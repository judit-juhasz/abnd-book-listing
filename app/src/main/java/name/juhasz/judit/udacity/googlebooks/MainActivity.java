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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final int BOOKS_LOADER_ID = 1;

    private static final String SEARCH_TERM_KEY = "Search Term";

    @BindView(R.id.et_search_term)
    public EditText mSearchTermEditText;

    @BindView(R.id.tv_message_display)
    public TextView mMessageDisplayTextView;

    @BindView(R.id.loading_indicator)
    public ProgressBar mLoadingIndicator;

    @BindView(R.id.lv_books)
    public ListView mBookListView;

    private BookAdapter mBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mBookAdapter = new BookAdapter(this, new ArrayList<Book>());
        mBookListView.setAdapter(mBookAdapter);

        if (isNetworkAvailable()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOKS_LOADER_ID, null, this);
        } else {
            showMessage(getString(R.string.error_no_internet));
        }
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
        boolean searchTermAvailable = null != searchTerm && !searchTerm.isEmpty();
        boolean networkAvailable = isNetworkAvailable();

        if (networkAvailable && searchTermAvailable) {
            showProgressBar();
            Bundle bundle = new Bundle();
            bundle.putString(SEARCH_TERM_KEY, searchTerm);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.restartLoader(BOOKS_LOADER_ID, bundle, this);
        } else if (!networkAvailable) {
            showMessage(getString(R.string.error_no_internet));
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
        boolean booksAvailable = (null != data && !data.isEmpty());

        if (booksAvailable) {
            showBooks(data);
        } else {
            showMessage(getString(R.string.error_no_books));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        showMessage(getString(R.string.error_no_books));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
