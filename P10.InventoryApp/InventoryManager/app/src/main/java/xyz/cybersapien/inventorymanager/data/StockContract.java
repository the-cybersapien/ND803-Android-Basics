package xyz.cybersapien.inventorymanager.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ogcybersapien on 19/10/16.
 * The Contract class provides us with two different Tables in our database
 * A supplier database and an items database.
 */
public final class StockContract {

    /*Content Authority for the Content Providers URI*/
    public static final String CONTENT_AUTHORITY = "xyz.cybersapien.inventorymanager";

    /*URI with the schema and Content Authority*/
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /*PATH entries for the Content Provider*/
    public static final String PATH_ITEMS = ItemEntry.TABLE_NAME;
    public static final String PATH_SUPPLIERS = SuppliersEntry.TABLE_NAME;

    public static abstract class ItemEntry implements BaseColumns{

        /*Table Name*/
        public static final String TABLE_NAME = "items";

        /*Content URI for the items Table*/
        public static final Uri ITEMS_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);

        /*Columns of the Table*/
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_ITEM_NAME = "name";
        public static final String COLUMN_ITEM_QUANTITY = "quantity";
        public static final String COLUMN_ITEM_PRICE = "price";
        public static final String COLUMN_ITEM_SUPPLIER_ID = "supId";
        public static final String COLUMN_ITEM_PICTURE = "picture";
        public static final String COLUMN_ITEM_SUPPLIER_EMAIL = "email";
        public static final String COLUMN_ITEM_SUPPLIER_PHONE = "phone";


        /*MIME types for different Content URIs*/
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
    }


    public static abstract class SuppliersEntry implements BaseColumns{

        /*Table Name*/
        public static final String TABLE_NAME = "suppliers";

        /*Content URI for the suppliers Table*/
        public static final Uri SUPPLIERS_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SUPPLIERS);

        /*Columns of the Table*/
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_SUPPLIER_NAME = "name";
        public static final String COLUMN_SUPPLIER_PHONE = "phone";
        public static final String COLUMN_SUPPLIER_EMAIL = "email";

        /*MIME types for different Content URIs*/
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_SUPPLIERS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_SUPPLIERS;
    }
}
