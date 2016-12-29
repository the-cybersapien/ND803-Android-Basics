package xyz.cybersapien.inventorymanager;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import xyz.cybersapien.inventorymanager.data.StockContract;

public class PurchaseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String LOG_TAG = PurchaseActivity.class.getName();

    /*Spinner for selecting the supplier*/
    private Spinner itemsSpinner;

    /*List of Items*/
    private ArrayList<Item> itemsList;

    /*currently selected item's ID*/
    private Integer currentSelected;

    private Cursor itemsCursor;
    private ItemAdapter adapter;

    private static final int ITEM_LOADER = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_action);

        itemsList = new ArrayList<>();
        ListView itemsView = (ListView) findViewById(R.id.action_items_list);

        adapter = new ItemAdapter(this,itemsList);
        itemsView.setAdapter(adapter);
        itemsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemsList.remove(i);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        //Set up spinner
        itemsSpinner = (Spinner) findViewById(R.id.items_spinner);
        getLoaderManager().initLoader(ITEM_LOADER, null, this);

        Button performPurchaseButton = (Button) findViewById(R.id.perform_action_button);
        performPurchaseButton.setText(R.string.purchase);
        performPurchaseButton.setOnClickListener(purchaseButtonListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projections = new String[] {
                StockContract.ItemEntry._ID,
                StockContract.ItemEntry.COLUMN_ITEM_NAME,
                StockContract.ItemEntry.COLUMN_ITEM_QUANTITY,
                StockContract.ItemEntry.COLUMN_ITEM_PRICE
        };
        return new CursorLoader(this, StockContract.ItemEntry.ITEMS_CONTENT_URI, projections, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
        String[] fromCursor = new String[] {StockContract.ItemEntry.COLUMN_ITEM_NAME};
        int[] toResource = new int[] {android.R.id.text1};
        itemsCursor = cursor;
        SimpleCursorAdapter itemsAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_spinner_item,
                cursor, fromCursor, toResource, 0);
        itemsSpinner.setAdapter(itemsAdapter);
        itemsSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        currentSelected = position;
                        Log.d(LOG_TAG, "onItemSelected: " + currentSelected);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        currentSelected = 0;
                    }
                }
        );
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        itemsSpinner.setAdapter(null);
    }

    public void addItem(View v){
        itemsCursor.moveToPosition(currentSelected);
        Log.d(LOG_TAG, "addItem: " + currentSelected);
        int idIndex = itemsCursor.getColumnIndex(StockContract.ItemEntry._ID);
        int nameIndex = itemsCursor.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_NAME);
        int priceIndex = itemsCursor.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_PRICE);
        int quantityIndex = itemsCursor.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_QUANTITY);
        itemsList.add(new Item(itemsCursor.getLong(idIndex),itemsCursor.getString(nameIndex), itemsCursor.getInt(quantityIndex), itemsCursor.getDouble(priceIndex)));
        adapter.notifyDataSetChanged();
    }

    /**
     * Using ASyncTask to change quantity of Items in the database
     */
    private class MakePurchaseASyncTask extends AsyncTask<ArrayList<Item>, Void, Boolean>{

        @Override
        protected Boolean doInBackground(ArrayList<Item>... lists) {
            ArrayList<Item> purchaseItemsList = lists[0];

            for (Item item : purchaseItemsList) {
                ContentValues values = new ContentValues();
                values.put(StockContract.ItemEntry.COLUMN_ITEM_QUANTITY, item.getQuantity() + 1);
                String selection = StockContract.ItemEntry._ID + "=?";
                String[] selectionArgs = new String[] {String.valueOf(item.getId())};
                getContentResolver().update(StockContract.ItemEntry.ITEMS_CONTENT_URI,
                        values, selection, selectionArgs);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Toast.makeText(PurchaseActivity.this, R.string.purchase_successful, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private View.OnClickListener purchaseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (itemsList.isEmpty()){
                //Show error
                Toast.makeText(getBaseContext(), R.string.purchase_error, Toast.LENGTH_SHORT).show();
                return;
            }
            MakePurchaseASyncTask purchaseASyncTask = new MakePurchaseASyncTask();
            purchaseASyncTask.execute(itemsList);
        }
    };

}
