package xyz.cybersapien.inventorymanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    public ItemAdapter(Context context, List<Item> objects) {
        super(context, R.layout.item_list_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;

        if (listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_item,parent,false);
        }

        Item currentItem = getItem(position);

        //Set item Name
        TextView itemNameView = (TextView) listView.findViewById(R.id.item_name_text_view);
        itemNameView.setText(currentItem.getName());

        //Set currentQuantity
        TextView quantityView = (TextView) listView.findViewById(R.id.current_quantity_text_view);
        quantityView.setText(String.valueOf(currentItem.getQuantity()));

        //Set Price
        TextView priceView = (TextView) listView.findViewById(R.id.price_text_view);
        priceView.setText(String.valueOf(currentItem.getPrice()));


        return listView;
    }
}
