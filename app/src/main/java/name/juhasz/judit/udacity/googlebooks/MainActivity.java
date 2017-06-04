package name.juhasz.judit.udacity.googlebooks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickSearch(View view) {
        TextView searchResultTextView = (TextView) findViewById(R.id.tv_search_result);
        searchResultTextView.setText(getString(R.string.search_result_description));
    }
}
