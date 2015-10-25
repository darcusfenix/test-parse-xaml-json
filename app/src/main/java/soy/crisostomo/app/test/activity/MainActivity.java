package soy.crisostomo.app.test.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import soy.crisostomo.app.test.R;
import soy.crisostomo.app.test.activity.office365.Office365Activity;
import soy.crisostomo.app.test.activity.youtube.StandAloneActivity;
import soy.crisostomo.app.test.activity.youtube.YoutubeActivity;
import soy.crisostomo.app.test.model.Song;
import soy.crisostomo.app.test.service.ParseSongsServiceXmlPullParserImp;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Button buttonParseXML;
    Button buttonPlayVideo;
    Button buttonStandalone;
    ListView listView;
    String xmlData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        buttonParseXML = (Button) findViewById(R.id.buttonParse);
        buttonPlayVideo = (Button) findViewById(R.id.buttonPlay);
        listView = (ListView) findViewById(R.id.listSongs);

        buttonParseXML.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ParseSongsServiceXmlPullParserImp parseSongs = new ParseSongsServiceXmlPullParserImp(xmlData);
                boolean status = parseSongs.process();

                if (status) {
                    ArrayList<Song> allSongs = parseSongs.getSongs();
                    ArrayAdapter<Song> adapter = new ArrayAdapter<Song>(MainActivity.this, R.layout.adapter, allSongs);
                    listView.setVisibility(listView.VISIBLE);
                    listView.setAdapter(adapter);
                }
            }

        });

        buttonPlayVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,YoutubeActivity.class);
                startActivity(intent);
            }

        });


        new DonwloadData().execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=100/xml");

        buttonStandalone = (Button) findViewById(R.id.buttonSubMenu);

        buttonStandalone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,StandAloneActivity.class);
                startActivity(intent);
            }

        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up buttonParseXML, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(MainActivity.this, Office365Activity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class DonwloadData extends AsyncTask<String, Void, String> {

        String myXmlData;

        protected String doInBackground(String... urls) {
            try {
                myXmlData = downloadXML(urls[0]);
            } catch (IOException e) {
                return "Unable to download XML file";
            }
            return "";
        }

        protected void onPostExecute(String result) {
            xmlData = myXmlData;
        }

        private String downloadXML(String theUrl) throws IOException {
            Integer BUFFER_SIZE = 2000;
            InputStream is = null;
            String xmlContents = "";
            try {
                URL url = new URL(theUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                Integer response = conn.getResponseCode();

                is = conn.getInputStream();

                InputStreamReader isr = new InputStreamReader(is);
                Integer charRead;

                char[] inputBuffer = new char[BUFFER_SIZE];
                try {
                    while ((charRead = isr.read(inputBuffer)) > 0) {
                        String readString = String.copyValueOf(inputBuffer, 0, charRead);
                        xmlContents += readString;
                        inputBuffer = new char[BUFFER_SIZE];
                    }
                    return xmlContents;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            } finally {
                if (is != null)
                    is.close();
            }
        }
    }
}
