package xyz.cybersapien.chandigarh;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TouristAttraction extends AppCompatActivity {

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Creating an ArrayList of Object Place with information like Place Name, Address and the Image Drawable Id
        final ArrayList<Place> places = new ArrayList<>();
        places.add(new Place(getString(R.string.rock_garden), getString(R.string.rock_garden_add),getString(R.string.rock_garden_desc),R.drawable.rock_garden));
        places.add(new Place(getString(R.string.sukhna_lake), getString(R.string.sector_1),getString(R.string.sukhna_lake_desc),R.drawable.sukhna_lake));
        places.add(new Place(getString(R.string.garden_of_silence), getString(R.string.silence_address),getString(R.string.garden_of_silence_desc),R.drawable.garden_of_silence));
        places.add(new Place(getString(R.string.rose_garden), getString(R.string.rose_garden_address),getString(R.string.rose_garden_desc),R.drawable.rose_garden));
        places.add(new Place(getString(R.string.open_hand_monument), getString(R.string.sector_1),getString(R.string.open_hand_monument_desc),R.drawable.open_hand ));
        places.add(new Place(getString(R.string.govt_museum), getString(R.string.sector_10),getString(R.string.govt_museum_desc),R.drawable.museum_of_art));
        places.add(new Place(getString(R.string.zoo), getString(R.string.zoo_address),getString(R.string.zoo_desc),R.drawable.chattbir_zoo));
        places.add(new Place(getString(R.string.tower_of_shadows),getString(R.string.sector_1),getString(R.string.tower_of_shadows_desc),R.drawable.tower_of_silence));
        places.add(new Place(getString(R.string.gallery_of_portraits),getString(R.string.sector_17),getString(R.string.portraits_desc),R.drawable.national_gallery_of_portraits));

        //Sorting of the ArrayList and a Custom Comparator for correct comparison of Objects
        //Although not really necessary, I felt the need to do this here
        Collections.sort(places, new Comparator<Place>() {
            @Override
            public int compare(Place o1, Place o2) {
                return o1.getPlaceName().compareTo(o2.getPlaceName());
            }
        });

        //Creating a custom Array Adapter for this Tourist Attractions
        PlacesAdapter placesAdapter = new PlacesAdapter(this, places);

        //Calling a listView to attach the Places Adapter to and then attaching it
        ListView mainListView = (ListView) findViewById(R.id.places_list);
        mainListView.setAdapter(placesAdapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                phoneView.setVisibility(View.GONE);

                TextView placeDescription = (TextView) dialog.findViewById(R.id.dialog_desc);
                placeDescription.setText(places.get(position).getPlaceDescription());
                dialog.show();
            }
        });
    }
}
