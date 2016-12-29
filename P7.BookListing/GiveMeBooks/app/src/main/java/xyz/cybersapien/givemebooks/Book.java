package xyz.cybersapien.givemebooks;

/**
 * Created by Cybersapien on 1/10/16.
 * The Book class will serve to create the books Objects we will be getting from the JSON data and then populating our List with.
 * The Book Class contains the following parameters:
 * 1. Book Title
 * 2. Book SubTitle
 * 3. Author's Name
 * 4. The Front Cover of the book
 */

public class Book {

    private String Title;
    private String SubTitle;
    private String Author;
    private String imageURL;
    public static final String NO_SUBTITLE = "NO_SUBSTRING_ATTACHED";
    public static final String NO_AUTHOR = "NO_AUTHORS_FOUND";
    public static final String NO_IMAGE = "NO_IMAGE_FOUND";

    public Book(String title, String subTitle, String author, String imageURL) {
        Title = title;
        SubTitle = subTitle;
        Author = author;
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return Title;
    }

    public String getSubTitle() {
        return SubTitle;
    }

    public String getAuthor() {
        return Author;
    }

    public String getImageURL() {
        return imageURL;
    }
}
