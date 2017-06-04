package name.juhasz.judit.udacity.googlebooks;

import java.util.List;

public class Book {
    private String mTitle;
    private List<String> mAuthors;

    public Book(String title, List<String> authors) {
        this.mTitle = title;
        this.mAuthors = authors;
    }

    public String getTitle() {
        return mTitle;
    }

    public List<String> getAuthors() {
        return mAuthors;
    }
}
