package xyz.cybersapien.chandigarh;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Restaurants extends AppCompatActivity {

    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_list);

        //Creating an ArrayList of Place objects which will contain a number of Restaurants
        //The Place constructor here takes 4 input Parameters
        // Place Name, Place Address, Place Image and Official Contact number
        final ArrayList<Place> places = new ArrayList<>();
        places.add(new Place(getString(R.string.barbeque_nation),getString(R.string.barbeque_address),R.drawable.barbeque_nation,getString(R.string.barbeque_call),getString(R.string.barbeque_desc)));
        places.add(new Place(getString(R.string.lalit_24_7),getString(R.string.lalit_address),R.drawable.the_lalit,getString(R.string.lalit_24_7_rest_tel),getString(R.string.lalit_24_7_desc)));
        places.add(new Place(getString(R.string.saffron_marriott),getString(R.string.marriot_address),R.drawable.saffron_marriott,getString(R.string.saffron_marriott_tel),getString(R.string.saffron_desc)));
        places.add(new Place(getString(R.string.gopals_35),getString(R.string.gopals_35_address),R.drawable.gopals,getString(R.string.gopals_tel),getString(R.string.gopals_desc)));
        places.add(new Place(getString(R.string.pal_dhaba),getString(R.string.pal_address),R.drawable.pal_dhabba,getString(R.string.pal_dhaba_tel),getString(R.string.pal_dhaba_desc)));
        places.add(new Place(getString(R.string.virgin_courtyard),getString(R.string.virgin_address),R.drawable.virgin_courtyard,getString(R.string.virgin_courtyard_tel),getString(R.string.virgin_courtyard_desc)));
        places.add(new Place(getString(R.string.pirates_of_grill),getString(R.string.pirates_address),R.drawable.pirates_of_grill,getString(R.string.pirates_of_grill_tel),getString(R.string.pirates_of_grill_desc)));

        //Creating a PlacesAdapter for this Activity and the places ArrayList
        PlacesAdapter hotelAdapter = new PlacesAdapter(this, places);

        //Creating a listView to attach the said Places adapter
        ListView placesList = (ListView) findViewById(R.id.places_list);
        placesList.setAdapter(hotelAdapter);

        placesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = new Dialog(context);

                final String contact = places.get(position).getContactNumber();
                dialog.setContentView(R.layout.dialog_popup);
                dialog.setTitle(places.get(position).getPlaceName());

                TextView headingView = (TextView) dialog.findViewById(R.id.heading_view);
                headingView.setText(places.get(position).getPlaceName());

                TextView addressView = (TextView) dialog.findViewById(R.id.dialog_address);
                addressView.setText(places.get(position).getAddress());

                TextView phoneView = (TextView) dialog.findViewById(R.id.dialog_contact_tel);

                if (places.get(position).hasContactNumber()){
                    phoneView.setText(contact);
                    phoneView.setVisibility(View.VISIBLE);
                    phoneView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + contact));
                            startActivity(callIntent);
                        }
                    });
                } else {
                    phoneView.setVisibility(View.GONE);
                }

                TextView placeDescription = (TextView) dialog.findViewById(R.id.dialog_desc);
                placeDescription.setText(places.get(position).getPlaceDescription());

                dialog.show();
            }
        });
    }
}
