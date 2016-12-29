package xyz.cybersapien.inventorymanager;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import xyz.cybersapien.inventorymanager.data.StockContract;

public class SupplierEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private String LOG_TAG = SupplierEditorActivity.class.getName();

    /*Uri for when this activity is called to Update existing data*/
    private Uri data;

    /*boolean to see if this instance is for a new Supplier entry or to update an existing one*/
    private boolean newSupplier;

    /*EditText fields in on the activity*/
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_editor);
        data = getIntent().getData();
        newSupplier = data==null;

        nameEditText = (EditText) findViewById(R.id.supplier_name_edit_text);
        emailEditText = (EditText) findViewById(R.id.supplier_email_edit_text);
        phoneEditText = (EditText) findViewById(R.id.supplier_phone_edit_text);

        if (!newSupplier){
            getLoaderManager().initLoader(1, null, this);
        }

        initSaveButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editors, menu);
        if (newSupplier){
            menu.findItem(R.id.action_delete_this).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete_this:
                showDeleteConfirmationDialog();
                break;
        }
        return true;
    }

    private void initSaveButton() {
        Button saveSupplier = (Button) findViewById(R.id.save_supplier_button);
        if (newSupplier){
            saveSupplier.setText("Add Supplier");
        } else {
            saveSupplier.setText("Update Supplier");
        }

        saveSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(StockContract.SuppliersEntry.COLUMN_SUPPLIER_NAME, nameEditText.getText().toString());
                values.put(StockContract.SuppliersEntry.COLUMN_SUPPLIER_PHONE, phoneEditText.getText().toString());
                values.put(StockContract.SuppliersEntry.COLUMN_SUPPLIER_EMAIL, emailEditText.getText().toString());
                if (newSupplier){
                    getContentResolver().insert(StockContract.SuppliersEntry.SUPPLIERS_CONTENT_URI, values);
                    finish();
                } else {
                    String selection = StockContract.SuppliersEntry._ID + "=?";
                    String[] selectionArgs = new String[] {String.valueOf(ContentUris.parseId(data))};
                    getContentResolver().update(StockContract.SuppliersEntry.SUPPLIERS_CONTENT_URI, values, selection, selectionArgs);
                    finish();
                }
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String[] projections = new String[] {
                StockContract.SuppliersEntry._ID,
                StockContract.SuppliersEntry.COLUMN_SUPPLIER_NAME,
                StockContract.SuppliersEntry.COLUMN_SUPPLIER_EMAIL,
                StockContract.SuppliersEntry.COLUMN_SUPPLIER_PHONE
        };

        String selection = StockContract.SuppliersEntry._ID + "=?";
        String[] selectionArgs = new String[] { String.valueOf(ContentUris.parseId(data)) };

        return new CursorLoader(this, StockContract.SuppliersEntry.SUPPLIERS_CONTENT_URI,
                projections, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        setupFields(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        
    }

    private void setupFields(Cursor cursor) {
        if (cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndex(StockContract.SuppliersEntry.COLUMN_SUPPLIER_NAME));
            String email = cursor.getString(cursor.getColumnIndex(StockContract.SuppliersEntry.COLUMN_SUPPLIER_EMAIL));
            String phone = cursor.getString(cursor.getColumnIndex(StockContract.SuppliersEntry.COLUMN_SUPPLIER_PHONE));
            nameEditText.setText(name);
            emailEditText.setText(email);
            phoneEditText.setText(phone);
        } else {
            Log.e(LOG_TAG, "Error!");
        }
    }

    private void showDeleteConfirmationDialog(){

        //Create an AlertDialog and set the message,
        //and click listeners for the positive and negative buttons

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.delete_supplier_title);
        alertBuilder.setMessage(R.string.delete_message_supplier);
        alertBuilder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteSupplier();
            }
        });

        alertBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null){
                    dialogInterface.dismiss();
                }
            }
        });

        AlertDialog deleteAlert = alertBuilder.create();
        deleteAlert.show();
    }

    private void deleteSupplier(){
        String where = StockContract.SuppliersEntry._ID + "=?";
        String[] selectionArgs = new String[] {
                String.valueOf(ContentUris.parseId(data))};
        getContentResolver().delete(StockContract.SuppliersEntry.SUPPLIERS_CONTENT_URI, where, selectionArgs);
        finish();
    }
}
