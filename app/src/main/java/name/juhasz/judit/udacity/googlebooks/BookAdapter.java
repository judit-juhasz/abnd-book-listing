package name.juhasz.judit.udacity.googlebooks;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {

    private static final String AUTHOR_SEPARATOR = ", ";
    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_book, parent, false);
        }

        Book currentBook = getItem(position);

        TextView bookTitleTextView = (TextView) listItemView.findViewById(R.id.tv_title_of_book);
        bookTitleTextView.setText(currentBook.getTitle());

        TextView bookAuthorsTextView = (TextView) listItemView.findViewById(R.id.tv_authors_of_book);
        bookAuthorsTextView.setText(TextUtils.join(AUTHOR_SEPARATOR, currentBook.getAuthors()));

        return listItemView;
    }
}
