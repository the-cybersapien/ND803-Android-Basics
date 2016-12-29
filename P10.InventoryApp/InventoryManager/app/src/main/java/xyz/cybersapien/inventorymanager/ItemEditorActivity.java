package xyz.cybersapien.inventorymanager;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import xyz.cybersapien.inventorymanager.data.StockContract;

public class ItemEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private String LOG_TAG = ItemEditorActivity.class.getName();

    /*Spinner for selecting the Supplier*/
    private Spinner supplierSpinner;

    /*Name Text Field*/
    private EditText nameEditText;

    /*Quantity Text Field*/
    private TextView quantityTextView;
    private Integer quantity;

    /*Price Text Field*/
    private EditText priceEditText;

    /*Image View*/
    private ImageView itemImageView;

    /*Supplier ID*/
    private long supplierID;

    /*Boolean to see if this is a new Item entry or to update an existing one*/
    private boolean newItem;

    /*Uri for the data in case this instance is to update an entry*/
    private Uri data;

    /*Uri for the photo*/
    private Uri itemPhotoUri;

    /*Supplier Cursor*/
    private Cursor supplierCursor;

    /*Supplier Email*/
    private String supplierEmail;

    /*Supplier Phone*/
    private String supplierPhone;

    private static final int ITEM_LOADER = 100;
    private static final int SUPPLIER_LOADER = 200;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_editor);

        data = getIntent().getData();
        newItem = data==null;

        quantity = 0;
        ImageView addQuantityButton = (ImageView) findViewById(R.id.modifier_add);
        addQuantityButton.setOnClickListener(addQuantity);

        ImageView decQuantityButton = (ImageView) findViewById(R.id.modifier_remove);
        decQuantityButton.setOnClickListener(decQuantity);

        //Initialize the ImageView
        itemImageView = (ImageView) findViewById(R.id.item_image_view);

        //Initialize Spinner
        supplierSpinner = (Spinner) findViewById(R.id.suppliers_spinner);
        getLoaderManager().initLoader(SUPPLIER_LOADER,null,this);

        //Initialize the TextFields
        nameEditText = (EditText) findViewById(R.id.item_name_edit_text);
        quantityTextView = (TextView) findViewById(R.id.item_quantity_text_view);
        quantityTextView.setText(String.valueOf(quantity));
        priceEditText = (EditText) findViewById(R.id.item_price_edit_text);

        Button submitButton = (Button) findViewById(R.id.submit_button);

        if (!newItem){
            getLoaderManager().initLoader(ITEM_LOADER,null,this);
            submitButton.setText(R.string.update);
        } else {
            submitButton.setText(R.string.add_item);
        }

        Log.d(LOG_TAG, "In OnCreate");

        initChangeImageButton();
        initSubmitButton();
    }

    private void initChangeImageButton() {

        Button changeImageButton = (Button) findViewById(R.id.take_image_button);
        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //Ensure that there is a camera
                if (itemPhotoUri != null) {
                    File file = new File(itemPhotoUri.getPath());
                    if (file.exists()){
                        file.delete();
                    }
                }
                if (takePictureIntent.resolveActivity(getPackageManager()) != null){
                    File picture = null;
                    try {
                        picture = createImageFile();
                    } catch (IOException e){
                        Log.e(LOG_TAG, "Error getting photo");
                    }
                    if (picture != null){
                        Uri photoURI = FileProvider.getUriForFile(getBaseContext(),"xyz.cybersapien.inventorymanager.fileprovider", picture);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Picasso.with(this).load(itemPhotoUri).resize(itemImageView.getWidth(), itemImageView.getHeight()).into(itemImageView);
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_" + nameEditText.getText().toString();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        itemPhotoUri = Uri.parse("file:" + image.getAbsolutePath());
        return image;
    }

    private void initSubmitButton() {

        Button submitButton = (Button) findViewById(R.id.submit_button);

        submitButton.setVisibility(View.VISIBLE);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newItem){
                    ContentValues values = new ContentValues();
                    values.put(StockContract.ItemEntry.COLUMN_ITEM_NAME, nameEditText.getText().toString());
                    String priceString = priceEditText.getText().toString();
                    if (!TextUtils.isEmpty(priceString)){
                        values.put(StockContract.ItemEntry.COLUMN_ITEM_PRICE, Double.parseDouble(priceString));
                    } else {
                        values.put(StockContract.ItemEntry.COLUMN_ITEM_PRICE, "-1");
                    }
                    values.put(StockContract.ItemEntry.COLUMN_ITEM_QUANTITY, quantity);
                    values.put(StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_ID, supplierID);
                    values.put(StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE, supplierPhone);
                    values.put(StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_EMAIL, supplierEmail);
                    if (itemPhotoUri!=null) {
                        values.put(StockContract.ItemEntry.COLUMN_ITEM_PICTURE, itemPhotoUri.toString());
                    } else {
                        values.put(StockContract.ItemEntry.COLUMN_ITEM_PICTURE, "");
                    }
                    if (validation(values)){
                        getContentResolver().insert(StockContract.ItemEntry.ITEMS_CONTENT_URI, values);
                        getLoaderManager().destroyLoader(ITEM_LOADER);
                        getLoaderManager().destroyLoader(SUPPLIER_LOADER);
                        finish();
                    }
                } else {
                    ContentValues values = new ContentValues();
                    values.put(StockContract.ItemEntry.COLUMN_ITEM_NAME, nameEditText.getText().toString());
                    values.put(StockContract.ItemEntry.COLUMN_ITEM_PRICE, Double.parseDouble(priceEditText.getText().toString()));
                    values.put(StockContract.ItemEntry.COLUMN_ITEM_QUANTITY, quantity);
                    values.put(StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_ID, supplierID);
                    values.put(StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_EMAIL, supplierEmail);
                    values.put(StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE, supplierPhone);
                    if (itemPhotoUri != null) {
                        values.put(StockContract.ItemEntry.COLUMN_ITEM_PICTURE, itemPhotoUri.toString());
                    } else {
                        values.put(StockContract.ItemEntry.COLUMN_ITEM_PICTURE, "");
                    }
                    if (validation(values)){
                        String where = StockContract.ItemEntry._ID + "=?";
                        String[] whereArgs = new String[] {String.valueOf(ContentUris.parseId(data))};
                        getContentResolver().update(StockContract.ItemEntry.ITEMS_CONTENT_URI, values, where, whereArgs);
                        getLoaderManager().destroyLoader(ITEM_LOADER);
                        getLoaderManager().destroyLoader(SUPPLIER_LOADER);
                        finish();
                    }
                }
            }
        });
    }

    private boolean validation(ContentValues values){
        boolean isValid = true;
        String name = values.getAsString(StockContract.ItemEntry.COLUMN_ITEM_NAME);
        if (TextUtils.isEmpty(name)){
            nameEditText.setError("Name is required!");
            isValid = false;
        }
        Double price = values.getAsDouble(StockContract.ItemEntry.COLUMN_ITEM_PRICE);
        if (price == null || price.isNaN() || price<0){
            priceEditText.setError("Price is invalid!");
            isValid = false;
        }
        return isValid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editors, menu);
        if (newItem){
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case SUPPLIER_LOADER:
                String[] supplierProjections = new String[] {
                        StockContract.SuppliersEntry._ID,
                        StockContract.SuppliersEntry.COLUMN_SUPPLIER_NAME,
                        StockContract.SuppliersEntry.COLUMN_SUPPLIER_EMAIL,
                        StockContract.SuppliersEntry.COLUMN_SUPPLIER_PHONE
                };
                return  new CursorLoader(this, StockContract.SuppliersEntry.SUPPLIERS_CONTENT_URI, supplierProjections,
                        null, null,null);

            case ITEM_LOADER:
                String[] itemProjections = new String[] {
                        StockContract.ItemEntry._ID,
                        StockContract.ItemEntry.COLUMN_ITEM_NAME,
                        StockContract.ItemEntry.COLUMN_ITEM_PRICE,
                        StockContract.ItemEntry.COLUMN_ITEM_QUANTITY,
                        StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_ID,
                        StockContract.ItemEntry.COLUMN_ITEM_PICTURE,
                        StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_EMAIL,
                        StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE
                };
                String selection = StockContract.ItemEntry._ID + "=?";
                String[] selectionArgs = new String[] {String.valueOf(ContentUris.parseId(data))};
                return new CursorLoader(this, StockContract.ItemEntry.ITEMS_CONTENT_URI, itemProjections, selection, selectionArgs, null);
            default:
                throw new IllegalArgumentException("Error! Loader ID not found");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()){
            case SUPPLIER_LOADER:
                supplierCursor = cursor;
                setupSpinner(cursor);
                break;
            case ITEM_LOADER:
                setupFields(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.abandon();
        supplierSpinner.setAdapter(null);
    }

    private void setupSpinner(final Cursor cursor){
        if (cursor.getCount()!=0){
            String[] fromCursor = new String[] {StockContract.SuppliersEntry.COLUMN_SUPPLIER_NAME};
            int[] toResource = new int[] {android.R.id.text1};
            SimpleCursorAdapter supplierAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursor,fromCursor, toResource, 0);
            supplierSpinner.setAdapter(supplierAdapter);
            supplierSpinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            supplierID = id;
                            setupSupplierData(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            if (!newItem){
                                supplierSpinner.setSelection(getPosition(cursor));
                            }
                        }
                    }
            );
        }
    }

    private void setupSupplierData(Integer position){
        Cursor cursor = supplierCursor;
        if (cursor!=null){
            if(cursor.moveToPosition(position)){
                int emailIndex = cursor.getColumnIndex(StockContract.SuppliersEntry.COLUMN_SUPPLIER_EMAIL);
                int phoneIndex = cursor.getColumnIndex(StockContract.SuppliersEntry.COLUMN_SUPPLIER_PHONE);
                if (emailIndex!=-1) {
                    supplierEmail = cursor.getString(emailIndex);
                }
                if (phoneIndex!=-1) {
                    supplierPhone = cursor.getString(phoneIndex);
                }
                Intent intent;
                if (!newItem){
                    if (!TextUtils.isEmpty(supplierEmail)){
                        intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + supplierEmail.trim()));
                        if (intent.resolveActivity(this.getPackageManager())!=null){
                            initOrderButton(intent);
                        } else{
                            Toast.makeText(this, "No Application to send Email!", Toast.LENGTH_SHORT).show();
                        }
                    } else if (!TextUtils.isEmpty(supplierPhone)){
                        intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + supplierPhone.trim()));
                        if (intent.resolveActivity(this.getPackageManager())!=null) {
                            initOrderButton(intent);
                        } else {
                            Toast.makeText(this, "No Application found to make a call", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.no_contact_info_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


    private void setupFields(Cursor cursor) {
        if (cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_NAME));
            String quantity = cursor.getString(cursor.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_QUANTITY));
            String price = cursor.getString(cursor.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_PRICE));
            supplierID = cursor.getInt(cursor.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_ID));
            nameEditText.setText(name);
            this.quantity = Integer.parseInt(quantity);
            setupSupplierData(getPosition(cursor));
            cursor.moveToFirst();
            quantityTextView.setText(quantity);
            priceEditText.setText(price);
            String pictureEntry = cursor.getString(cursor.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_PICTURE));
            if (!TextUtils.isEmpty(pictureEntry)) {
                itemPhotoUri = Uri.parse(cursor.getString(cursor.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_PICTURE)));
                Picasso.with(this).load(itemPhotoUri).resize(itemImageView.getWidth(), itemImageView.getHeight()).into(itemImageView);
            }
        } else {
            Log.e(LOG_TAG, "setupFields: " + cursor.toString());
        }
    }

    private int getPosition(Cursor c){
        int currentPosition = 0;
        while (c.moveToNext()){
            if (c.getInt(c.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_ID)) == supplierID){
                break;
            }
            currentPosition++;
        }
        return currentPosition;
    }

    private void initOrderButton(final Intent intent) {
        Button orderButton = (Button) findViewById(R.id.order_button);
        orderButton.setVisibility(View.VISIBLE);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    private View.OnClickListener addQuantity = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            quantity++;
            quantityTextView.setText(String.valueOf(quantity));
        }
    };

    private View.OnClickListener decQuantity = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if (quantity>0){
                quantity--;
                quantityTextView.setText(String.valueOf(quantity));
            } else {
                Toast.makeText(ItemEditorActivity.this, R.string.error_negative_quantity, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void showDeleteConfirmationDialog(){

        //Create an AlertDialog and set the message,
        //and click listeners for the positive and negative buttons

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.delete_item);
        alertBuilder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteItem();
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

    private void deleteItem(){
        String where = StockContract.ItemEntry._ID + "=?";
        String[] selectionArgs = new String[] {String.valueOf(ContentUris.parseId(data))};
        getContentResolver().delete(StockContract.ItemEntry.ITEMS_CONTENT_URI, where,selectionArgs);
        finish();
    }
}
