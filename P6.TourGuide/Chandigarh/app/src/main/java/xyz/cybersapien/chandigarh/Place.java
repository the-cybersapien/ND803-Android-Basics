package xyz.cybersapien.chandigarh;

import java.util.Objects;

/**
 * Created by Cybersapien on 25/9/16.
 * This is the Object file for the places Objects for my app.
 * The Places will be divided into 4 categories,
 * 1. Tourist Attractions
 * 2. Best Hotels and Restaurants
 * 3. Malls
 * 4. Divine Temples
 *
 * For now, each object is limited to a picture, the name and address, a phone number(in case of Hotels and Restaurants)
 * Clicking on the Object will set off an Intent to the available maps apps for showing the exact location of the place.
 */

public class Place {

    private String placeName;
    private String address;
    private int imageID;
    private String contactNumber = NO_CONTACT_INFO;
    final static public String NO_CONTACT_INFO = "NO INFO";
    private String placeDescription;

    /**
     * Place Constructor for places with no Phone number
     * @param placeName Name of the Place
     * @param address Address of the Place
     * @param placeDescription Brief Description about the Place
     * @param imageID Resource ID for the image for the Place
     */
    public Place(String placeName, String address,String placeDescription, int imageID) {
        this.placeName = placeName;
        this.address = address;
        this.imageID = imageID;
        this.placeDescription = placeDescription;
        this.contactNumber = NO_CONTACT_INFO;
    }

    /**
     * Place Constructor for places with no Phone number
     * @param placeName Name of the Place
     * @param address Address of the Place
     * @param imageID Resource ID for the image for the Place
     * @param contactNumber Contact number of the Place
     * @param placeDescription Brief Description about the Place
     */
    public Place(String placeName, String address, int imageID, String contactNumber, String placeDescription) {
        this.placeName = placeName;
        this.address = address;
        this.imageID = imageID;
        this.contactNumber = contactNumber;
        this.placeDescription = placeDescription;
    }

    /**
     * Returns the Place Name
     * @return String Name of the Place
     */
    public String getPlaceName() {
        return placeName;
    }

    /**
     * Returns the Address of the Place
     * @return String Address of the Place
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the Image Resource ID of the Place
     * @return int resource ID
     */
    public int getImageID() {
        return imageID;
    }

    /**
     * Returns the Contact Number of the Place
     * @return String Contact Number of the Place
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * To check if the Place has a Phone Number or not
     * @return Boolean true or false about phone number
     */
    public boolean hasContactNumber(){
        return (!this.contactNumber.equals(NO_CONTACT_INFO));
    }

    /**
     * Returns a Brief Description of the Place
     * @return String Description of the Place
     */
    public String getPlaceDescription() {
        return placeDescription;
    }
}
