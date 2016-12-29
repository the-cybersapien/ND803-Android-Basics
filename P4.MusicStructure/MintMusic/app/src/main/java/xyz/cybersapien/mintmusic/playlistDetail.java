package xyz.cybersapien.mintmusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class playlistDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);

        //Creates a button to set the onClick listener and starting the Song Details activity using an explicit intent
        Button songDetail = (Button) findViewById(R.id.SongDetailsPlaylistButton);
        songDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSongDetail = new Intent(getApplicationContext(), SongDetails.class);
                startActivity(toSongDetail);
            }
        });

    }
}
