package xyz.cybersapien.chandigarh;

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
        Intent intent;
        switch (v.getId()){
            case R.id.tourist_attraction:
                intent = new Intent(MainActivity.this, TouristAttraction.class);
                startActivity(intent);
                break;
            case R.id.mall_attraction:
                intent = new Intent(MainActivity.this, Malls.class);
                startActivity(intent);
                break;
            case R.id.hotel_restaurant:
                intent = new Intent(MainActivity.this, Restaurants.class);
                startActivity(intent);
                break;
            case R.id.places_worship:
                intent = new Intent(MainActivity.this, PlacesOfWorship.class);
                startActivity(intent);
                break;
        }
    }
}
