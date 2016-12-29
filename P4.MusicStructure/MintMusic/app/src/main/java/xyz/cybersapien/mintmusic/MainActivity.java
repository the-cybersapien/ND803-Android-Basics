package xyz.cybersapien.mintmusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button nowPlaying, goToPlaylist, goToMyAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nowPlaying = (Button) findViewById(R.id.nowPlayingButton);
        goToPlaylist = (Button) findViewById(R.id.playListDetailButton);
        goToMyAccount = (Button) findViewById(R.id.my_account);

        nowPlaying.setOnClickListener(this);
        goToPlaylist.setOnClickListener(this);
        goToMyAccount.setOnClickListener(this);
    }

    //Creates a button to set the onClick listener and starting the activity using an explicit intent to the required Intent
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.my_account:
                Intent myAccountIntent = new Intent(this, myaccount.class);
                startActivity(myAccountIntent);
                break;
            case R.id.nowPlayingButton:
                Intent nowPlayingIntent = new Intent(this,NowPlaying.class);
                //We'll need to add some data to the nowPlayingIntent as and when required use .setData() Method.
                startActivity(nowPlayingIntent);
                break;
            case R.id.playListDetailButton:
                Intent playlistIntent = new Intent(this, playlistDetail.class);
                //Pass the name of the playlist using .setData() method
                startActivity(playlistIntent);
                break;
        }
    }
}
