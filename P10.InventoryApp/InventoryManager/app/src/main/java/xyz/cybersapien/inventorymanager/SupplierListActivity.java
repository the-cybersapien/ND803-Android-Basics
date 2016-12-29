package xyz.cybersapien.inventorymanager;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import xyz.cybersapien.inventorymanager.data.StockContract;

public class SupplierListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOG_TAG = SupplierListActivity.class.getName();

    private static final int STOCK_LOADER = 0;

    //CursorAdapter for the Suppliers
    private SupplierCursorAdapter customCursorAdapter;

    //ListView for the Adapter to display data
    private ListView suppliersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        //fab for creating new Supplier
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newSupplierIntent = new Intent(getBaseContext(), SupplierEditorActivity.class);
                startActivity(newSupplierIntent);
            }
        });

        setTitle(getString(R.string.suppliers));
        suppliersListView = (ListView) findViewById(R.id.main_list);
        TextView emptyHintView = (TextView) findViewById(R.id.empty_list_hint);
        emptyHintView.setText(R.string.no_suppliers_hint);
        suppliersListView.setEmptyView(emptyHintView);
        customCursorAdapter = new SupplierCursorAdapter(this, null);
        suppliersListView.setAdapter(customCursorAdapter);

        suppliersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Uri supplierUri = ContentUris.withAppendedId(StockContract.SuppliersEntry.SUPPLIERS_CONTENT_URI, id);
                Intent intent = new Intent(getBaseContext(), SupplierEditorActivity.class);
                intent.setData(supplierUri);
                startActivity(intent);
            }
        });

        //start the Loader
        getLoaderManager().initLoader(STOCK_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        menu.findItem(R.id.action_menu_list_toggle).setTitle(R.string.items_list);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_data_insert_dummy_data:
                insertSuppliers();
                break;
            case R.id.action_menu_list_toggle:
                Intent itemsIntent = new Intent(this, ItemListActivity.class);
                startActivity(itemsIntent);
                break;
            case R.id.delete_all:
                getContentResolver().delete(StockContract.SuppliersEntry.SUPPLIERS_CONTENT_URI, null,null);
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(SupplierListActivity.this);
                break;
        }
        return true;
    }

    private void insertSuppliers(){
        //Dummy data
        ContentValues values = new ContentValues();
        values.put(StockContract.SuppliersEntry.COLUMN_SUPPLIER_NAME, "Charlie Harper");
        values.put(StockContract.SuppliersEntry.COLUMN_SUPPLIER_PHONE, "7696497298");
        values.put(StockContract.SuppliersEntry.COLUMN_SUPPLIER_EMAIL, "aditya@cybersapien.xyz");
        Uri suppliersUri = StockContract.SuppliersEntry.SUPPLIERS_CONTENT_URI;
        getContentResolver().insert(suppliersUri, values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                StockContract.SuppliersEntry._ID,
                StockContract.SuppliersEntry.COLUMN_SUPPLIER_NAME,
                StockContract.SuppliersEntry.COLUMN_SUPPLIER_PHONE,
                StockContract.SuppliersEntry.COLUMN_SUPPLIER_EMAIL
        };
        return new CursorLoader(this, StockContract.SuppliersEntry.SUPPLIERS_CONTENT_URI,
                projection, null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        customCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        customCursorAdapter.swapCursor(null);
    }
}
