package xyz.cybersapien.inventorymanager;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.text.NumberFormat;

import xyz.cybersapien.inventorymanager.data.StockContract;

/**
 * Created by cybersapien on 20/10/16.
 * This Class defines a custom cursor Adapter for the Items in the Stock Inventory.
 */

public class ItemCursorAdapter extends CursorAdapter {


    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /*flags*/);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_item,parent,false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        /*Get all the views inside the list_item*/
        TextView item_name_view = (TextView) view.findViewById(R.id.item_name_text_view);
        TextView item_price_view = (TextView) view.findViewById(R.id.price_text_view);
        TextView item_quantity_view = (TextView) view.findViewById(R.id.current_quantity_text_view);
        ImageView item_image_view = (ImageView) view.findViewById(R.id.item_image_view);

        //Find the indices of name, price and quantity in the cursor
        final int idIndex = cursor.getColumnIndex(StockContract.ItemEntry._ID);
        int nameIndex = cursor.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_NAME);
        int priceIndex = cursor.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_PRICE);
        int quantityIndex = cursor.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_QUANTITY);
        int imageIndex = cursor.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_PICTURE);
        int emailIndex = cursor.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_EMAIL);
        final int phoneIndex = cursor.getColumnIndex(StockContract.ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE);

        //Get values for the fields
        String itemName = cursor.getString(nameIndex);
        Float price = cursor.getFloat(priceIndex);
        Integer quantity = cursor.getInt(quantityIndex);
        if (imageIndex>-1){
            String image = cursor.getString(imageIndex);
            if (!TextUtils.isEmpty(image))
            Picasso.with(context).load(Uri.parse(image)).resize(250,350).into(item_image_view);
        }

        final String supplierEmail = cursor.getString(emailIndex);
        final String supplierPhone = cursor.getString(phoneIndex);

        //Set values to the respective TextViews
        item_name_view.setText(itemName);
        item_price_view.setText(NumberFormat.getCurrencyInstance().format(price));
        item_quantity_view.setText(NumberFormat.getInstance().format(quantity));

        view.findViewById(R.id.track_sale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(supplierEmail)){
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + supplierEmail.trim()));
                    if (intent.resolveActivity(context.getPackageManager())!=null){
                        context.startActivity(intent);
                    } else{
                        Toast.makeText(context, "No Application to send Email!", Toast.LENGTH_SHORT).show();
                    }
                } else if (!TextUtils.isEmpty(supplierPhone)){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + supplierPhone.trim()));
                    if (intent.resolveActivity(context.getPackageManager())!=null){
                        context.startActivity(intent);
                    }
                } else {
                    Toast.makeText(context, "No Application to make a call!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.findViewById(R.id.openEditor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri itemUri = ContentUris.withAppendedId(StockContract.ItemEntry.ITEMS_CONTENT_URI,cursor.getLong(idIndex));
                Intent intent = new Intent(context, ItemEditorActivity.class);
                intent.setData(itemUri);
                context.startActivity(intent);
            }
        });
    }
}
