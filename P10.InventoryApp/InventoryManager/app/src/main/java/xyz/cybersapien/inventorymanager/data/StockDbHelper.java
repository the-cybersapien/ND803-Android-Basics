package xyz.cybersapien.inventorymanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import xyz.cybersapien.inventorymanager.data.StockContract.ItemEntry;
import xyz.cybersapien.inventorymanager.data.StockContract.SuppliersEntry;

/**
 * Created by ogcybersapien on 19/10/16.
 * The StockDbHelper class helps in creating and upgrading tables in the database.
 */

public class StockDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "stock.db";
    public static final int DATABASE_VERSION = 1;

    public StockDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME
                + " (" + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_PRICE + " REAL NOT NULL DEFAULT 0, "
                + ItemEntry.COLUMN_ITEM_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + ItemEntry.COLUMN_ITEM_SUPPLIER_ID + " INTEGER NOT NULL, "
                + ItemEntry.COLUMN_ITEM_SUPPLIER_EMAIL + " TEXT, "
                + ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE + " TEXT, "
                + ItemEntry.COLUMN_ITEM_PICTURE + " TEXT);";

        db.execSQL(SQL_CREATE_ITEMS_TABLE);

        String SQL_CREATE_SUPPLIERS_TABLE = "CREATE TABLE " + SuppliersEntry.TABLE_NAME
                + " (" + SuppliersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SuppliersEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + SuppliersEntry.COLUMN_SUPPLIER_PHONE + " TEXT NOT NULL DEFAULT '0', "
                + SuppliersEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL DEFAULT 'NA');";
        db.execSQL(SQL_CREATE_SUPPLIERS_TABLE);
    }

    //Not adding anything here since this is the first version of the database.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
