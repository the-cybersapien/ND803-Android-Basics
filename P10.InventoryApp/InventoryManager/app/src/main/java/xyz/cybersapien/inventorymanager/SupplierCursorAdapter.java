package xyz.cybersapien.inventorymanager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Locale;

import xyz.cybersapien.inventorymanager.data.StockContract;

/**
 * Created by cybersapien on 20/10/16.
 * This Class helps in Defining a Custom Cursor Adapter for the Suppliers in the Stock Inventory Database
 */

public class SupplierCursorAdapter extends CursorAdapter {


    public SupplierCursorAdapter(Context context, Cursor c) {
        super(context, c, 0/*flags*/);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.supplier_list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Get all the TextViews
        TextView supplierNameView = (TextView) view.findViewById(R.id.supplier_name_text_view);
        TextView supplierPhoneView = (TextView) view.findViewById(R.id.supplier_phone);
        TextView supplierEmailView = (TextView) view.findViewById(R.id.supplier_email);

        //Get Column indices from cursor
        int nameIndex = cursor.getColumnIndex(StockContract.SuppliersEntry.COLUMN_SUPPLIER_NAME);
        int phoneIndex = cursor.getColumnIndex(StockContract.SuppliersEntry.COLUMN_SUPPLIER_PHONE);
        int emailIndex = cursor.getColumnIndex(StockContract.SuppliersEntry.COLUMN_SUPPLIER_EMAIL);

        //Get Strings containing data from the cursor
        String name = cursor.getString(nameIndex);
        String phone = cursor.getString(phoneIndex);
        String email = cursor.getString(emailIndex);
        supplierNameView.setText(name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            phone = PhoneNumberUtils.formatNumber(phone, Locale.getDefault().getCountry());
        } else {
            phone = PhoneNumberUtils.formatNumber(phone);
        }

        if (!TextUtils.isEmpty(phone)){
            supplierPhoneView.setText(phone);
            supplierPhoneView.setVisibility(View.VISIBLE);
        } else {
            supplierPhoneView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(email)){
            supplierEmailView.setText(email);
            supplierEmailView.setVisibility(View.VISIBLE);
        } else {
            supplierEmailView.setVisibility(View.GONE);
        }
    }
}
