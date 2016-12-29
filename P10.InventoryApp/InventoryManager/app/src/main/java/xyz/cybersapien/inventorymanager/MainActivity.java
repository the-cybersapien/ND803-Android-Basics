package xyz.cybersapien.inventorymanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickListener(View v){
        Intent activityIntent;
        switch (v.getId()){
            case R.id.items_list_option:
                activityIntent = new Intent(this,ItemListActivity.class);
                startActivity(activityIntent);
                break;
            case R.id.suppliers_list_option:
                activityIntent = new Intent(this, SupplierListActivity.class);
                startActivity(activityIntent);
                break;
            case R.id.add_sale_option:
                activityIntent = new Intent(this, SaleActivity.class);
                startActivity(activityIntent);
                break;
            case R.id.add_purchase_option:
                activityIntent = new Intent(this, PurchaseActivity.class);
                startActivity(activityIntent);
                break;
            default:
                throw new NullPointerException("View case not found!");
        }
    }
}
