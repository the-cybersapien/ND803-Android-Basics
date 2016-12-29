package xyz.cybersapien.inventorymanager.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by ogcybersapien on 19/10/16.
 * The StockProvider class is the main driving force here.
 * It enables us to use Content providers and work with the databases Efficiently.
 */
public class StockProvider extends ContentProvider {

    /*LOG TAG*/
    public static final String LOG_TAG = StockProvider.class.getName();

    /*Database Helper Object*/
    private StockDbHelper stockDbHelper;

    /*codes for different URI matches from Items table*/
    private static final int ITEMS = 100;
    private static final int ITEMS_ID = 101;

    /*Codes for different URI matches from Suppliers table*/
    private static final int SUPPLIERS = 200;
    private static final int SUPPLIERS_ID = 201;

    //URI matcher object for matching the URIs to appropriate tasks.
    private static final UriMatcher stockUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        stockUriMatcher.addURI(StockContract.CONTENT_AUTHORITY, StockContract.PATH_ITEMS, ITEMS);
        stockUriMatcher.addURI(StockContract.CONTENT_AUTHORITY, StockContract.PATH_ITEMS + "/#", ITEMS_ID);
        stockUriMatcher.addURI(StockContract.CONTENT_AUTHORITY, StockContract.PATH_SUPPLIERS, SUPPLIERS);
        stockUriMatcher.addURI(StockContract.CONTENT_AUTHORITY, StockContract.PATH_SUPPLIERS + "/#", SUPPLIERS_ID);
    }

    @Override
    public boolean onCreate() {
        stockDbHelper = new StockDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //Get readable database
        SQLiteDatabase database = stockDbHelper.getReadableDatabase();

        //Cursor to hold the data
        Cursor cursor;

        int match = stockUriMatcher.match(uri);
        switch(match){

            /**To get Items list, the match will contain value ITEMS*/
            case ITEMS:
                cursor = database.query(StockContract.ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null,null,sortOrder);
                break;
            /*To get a particular Item with the specified ITEM ID*/
            case ITEMS_ID:
                selection = StockContract.ItemEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(StockContract.ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            /*To get Suppliers list*/
            case SUPPLIERS:
                cursor = database.query(StockContract.SuppliersEntry.TABLE_NAME, projection, selection, selectionArgs, null,null,sortOrder);
                break;
            /*To get a particular Supplier with the specified SUPPLIER_ID*/
            case SUPPLIERS_ID:
                selection = StockContract.SuppliersEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(StockContract.SuppliersEntry.TABLE_NAME, projection, selection, selectionArgs, null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot find a match for the specified URI.");
        }

        //Set a cursor notification to notify the cursor of a change in this URIs results
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        int match = stockUriMatcher.match(uri);
        switch (match){

            case ITEMS:
                return StockContract.ItemEntry.CONTENT_LIST_TYPE;
            case ITEMS_ID:
                return StockContract.ItemEntry.CONTENT_ITEM_TYPE;
            case SUPPLIERS:
                return StockContract.SuppliersEntry.CONTENT_LIST_TYPE;
            case SUPPLIERS_ID:
                return StockContract.SuppliersEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("No Match found for the URI " + uri);
        }
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = stockUriMatcher.match(uri);
        Long id;
        switch (match){
            case ITEMS:
                id = addItem(values);
                break;
            case SUPPLIERS:
                id = addSupplier(values);
                break;
            default:
                throw new IllegalArgumentException("No table for uri " + uri);
        }

        if (id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = stockUriMatcher.match(uri);
        int rowsDeleted;

        SQLiteDatabase db = stockDbHelper.getWritableDatabase();

        switch (match){
            case ITEMS:
                rowsDeleted = db.delete(StockContract.ItemEntry.TABLE_NAME, selection,selectionArgs);
                break;
            case ITEMS_ID:
                selection = StockContract.ItemEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(StockContract.ItemEntry.TABLE_NAME, selection,selectionArgs);
                break;
            case SUPPLIERS:
                rowsDeleted = db.delete(StockContract.SuppliersEntry.TABLE_NAME, selection,selectionArgs);
                break;
            case SUPPLIERS_ID:
                selection = StockContract.SuppliersEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(StockContract.SuppliersEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("No match found for uri " + uri);
        }
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int match = stockUriMatcher.match(uri);
        int rowsUpdated;
        switch (match){
            case ITEMS:
                rowsUpdated =  updateItem(values,selection,selectionArgs);
                break;
            case ITEMS_ID:
                selection = "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = updateItem(values, selection, selectionArgs);
                break;
            case SUPPLIERS:
                rowsUpdated = updateSupplier(values,selection,selectionArgs);
                break;
            case SUPPLIERS_ID:
                selection = "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = updateSupplier(values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("No valid match for URI: " + uri);
        }

        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsUpdated;
    }

    private int updateItem(ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0){
            return 0;
        }
        //Name Validation
        if (values.containsKey(StockContract.ItemEntry.COLUMN_ITEM_NAME) && values.getAsString(StockContract.ItemEntry.COLUMN_ITEM_NAME) == null){
            throw new IllegalArgumentException("Item requires a name");
        }
        //Price Validation
        if (values.containsKey(StockContract.ItemEntry.COLUMN_ITEM_PRICE) && values.getAsDouble(StockContract.ItemEntry.COLUMN_ITEM_PRICE)==null){
            throw new IllegalArgumentException("Item requires a price");
        }
        //Quantity Validation
        if (values.containsKey(StockContract.ItemEntry.COLUMN_ITEM_QUANTITY) && values.getAsInteger(StockContract.ItemEntry.COLUMN_ITEM_QUANTITY)==null){
            throw new IllegalArgumentException("Item requires a quantity");
        }
        //Supplier Validation
        if (values.containsKey(StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_ID) && values.getAsInteger(StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_ID)==null){
            throw new IllegalArgumentException("Item requires a valid Supplier");
        }

        SQLiteDatabase db = stockDbHelper.getWritableDatabase();
        return db.update(StockContract.ItemEntry.TABLE_NAME,values,selection,selectionArgs);
    }

    private long addItem(ContentValues values){

        String name = values.getAsString(StockContract.ItemEntry.COLUMN_ITEM_NAME);
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Item requires a valid name");
        }
        Integer quantity = values.getAsInteger(StockContract.ItemEntry.COLUMN_ITEM_QUANTITY);
        if (quantity == null){
            throw new IllegalArgumentException("Item requires a valid quantity");
        }

        Double price = values.getAsDouble(StockContract.ItemEntry.COLUMN_ITEM_PRICE);
        if (price == null){
            throw new IllegalArgumentException("Item requires a valid price");
        }
        Integer supplierId = values.getAsInteger(StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_ID);
        if (supplierId == null){
            throw new IllegalArgumentException("Item requires a valid Supplier Id");
        }

        SQLiteDatabase db = stockDbHelper.getWritableDatabase();
        return db.insert(StockContract.ItemEntry.TABLE_NAME,null,values);
    }

    private long addSupplier(ContentValues values){

        String supplierName = values.getAsString(StockContract.SuppliersEntry.COLUMN_SUPPLIER_NAME);
        if (TextUtils.isEmpty(supplierName)){
            throw new IllegalArgumentException("Supplier requires a valid name");
        }
        String email = values.getAsString(StockContract.SuppliersEntry.COLUMN_SUPPLIER_EMAIL);
        String phone = values.getAsString(StockContract.SuppliersEntry.COLUMN_SUPPLIER_PHONE);
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(phone)){
            throw new IllegalArgumentException("Atleast one among email and string must be selected");
        }
        SQLiteDatabase db = stockDbHelper.getWritableDatabase();
        return db.insert(StockContract.SuppliersEntry.TABLE_NAME, null,values);
    }

    private int updateSupplier(ContentValues values, String selection, String[] selectionArgs) {
        if (values.size()==0){
            return 0;
        }
        //Name Validation
        if (values.containsKey(StockContract.SuppliersEntry.COLUMN_SUPPLIER_NAME) && values.getAsString(StockContract.SuppliersEntry.COLUMN_SUPPLIER_NAME)==null){
            throw new IllegalArgumentException("Supplier requires a name");
        }
        //Email and phone number, atleast one must exist
        if (values.containsKey(StockContract.SuppliersEntry.COLUMN_SUPPLIER_EMAIL)
                && values.getAsString(StockContract.SuppliersEntry.COLUMN_SUPPLIER_EMAIL)==null
                && values.containsKey(StockContract.SuppliersEntry.COLUMN_SUPPLIER_PHONE)
                && values.getAsString(StockContract.SuppliersEntry.COLUMN_SUPPLIER_PHONE)==null){
            throw new IllegalArgumentException("Atleast one from email and phone number must exist");
        }
        SQLiteDatabase db = stockDbHelper.getWritableDatabase();
        return db.update(StockContract.SuppliersEntry.TABLE_NAME, values,selection,selectionArgs);
    }
}
