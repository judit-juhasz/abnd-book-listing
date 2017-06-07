package name.juhasz.judit.udacity.googlebooks;

import java.util.List;

public class Book {
    private String mTitle;
    private List<String> mAuthors;
    private String mCoverImagePath;

    public Book(String title, List<String> authors, String coverImagePath) {
        this.mTitle = title;
        this.mAuthors = authors;
        this.mCoverImagePath = coverImagePath;
    }

    public String getTitle() {
        return mTitle;
    }

    public List<String> getAuthors() {
        return mAuthors;
    }

    public String getCoverImagePath() {
        return mCoverImagePath;
    }
}
