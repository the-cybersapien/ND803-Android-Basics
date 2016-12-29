package xyz.cybersapien.mintmusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NowPlaying extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        //Creates a button to set the onClick listener and starting the Song Details activity using an explicit intent
        Button songDetailsButton = (Button) findViewById(R.id.SongDetailsNowPlayingButton);
        songDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), SongDetails.class);
                startActivity(intent);
            }
        });
    }
}
