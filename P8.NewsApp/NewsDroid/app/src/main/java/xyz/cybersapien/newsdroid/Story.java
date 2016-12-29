package xyz.cybersapien.newsdroid;

/**
 * Created by ogcybersapien on 6/10/16.
 * A custom Object for Story
 */

public class Story {


    /*Title of the Story*/
    private String title;
    /*Trailing text of the Story*/
    private String subText;
    /*URL for the Image of the Story*/
    private String imgURL;
    /*Author of the Story*/
    private String byLine;
    /*Default Value of the Story*/
    public final static String byLineDefault = "NO_AUTHOR";
    /*URL to The Guardian page of the Story*/
    private String webLink;
    /*Publication date of the Story*/
    private String publicationDate;

    /**
     * Constructor for creating a new Story Object
     * @param title Title of the Story
     * @param subText trailText of the Story
     * @param imgURL imageURL of the story
     * @param byLine Name of the Author
     * @param webLink URL to the story on the Guardian
     * @param publicationDate date of the Story
     */
    public Story(String title, String subText, String imgURL, String byLine, String webLink, String publicationDate) {
        this.title = title;
        this.subText = subText;
        this.imgURL = imgURL;
        this.byLine = "-".concat(byLine);
        this.webLink = webLink;
        this.publicationDate = publicationDate;
    }

    /**
     * Gets the String representation of the Title of the story
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the String representation of the trailing Text of the Story
     * @return Trailing Text
     */
    public String getTrailingText() {
        return subText;
    }

    /**
     * Gets the URL to the Image for the story in String form
     * @return Image URL
     */
    public String getImgURL() {
        return imgURL;
    }

    /**
     * Gets the String representation of the Author Name
     * @return Author Name
     */
    public String getByLine() {
        return byLine;
    }

    /**
     * Gets the URL to The Guardian web-page for the story in String form
     * @return URL
     */
    public String getWebLink() {
        return webLink;
    }

    /**
     * Gets the String representation of the Date of Publication
     * @return Publication Date
     */
    public String getPublicationDate() {
        return publicationDate;
    }
}
