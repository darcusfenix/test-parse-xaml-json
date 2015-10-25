package soy.crisostomo.app.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.api.services.youtube.YouTube;

import soy.crisostomo.app.test.R;

/**
 * Created by darcusfenix on 10/23/15.
 */
public class StandAloneActivity extends Activity implements View.OnClickListener{

    public static final String GOOGLE_API_KEY = "AIzaSyCDDd0culBn_a73niIEPLNgd3h_vEZp_xw";
    public static final String YOUTUBE_VIDEO_ID = "XmSdTa9kaiQ";
    public static final String YOUTUBE_PLAYLIST_ID = "PL7516FD72E35D8222";


    private Button buttonPLayVideo;
    private Button buttonPLayList;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stand_alone);

        buttonPLayVideo = (Button) findViewById(R.id.buttonStart);
        buttonPLayList = (Button) findViewById(R.id.buttonPlayList);

        buttonPLayVideo.setOnClickListener(this);
        buttonPLayList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v==buttonPLayVideo){
            // play a single video
            intent = YouTubeStandalonePlayer.createVideoIntent(this,GOOGLE_API_KEY, YOUTUBE_VIDEO_ID);
        }else if(v==buttonPLayList){
            // play a play list
            intent = YouTubeStandalonePlayer.createPlaylistIntent(this,GOOGLE_API_KEY, YOUTUBE_PLAYLIST_ID);
        }

        if (intent != null){
            startActivityForResult(intent,0);
        }
    }
}