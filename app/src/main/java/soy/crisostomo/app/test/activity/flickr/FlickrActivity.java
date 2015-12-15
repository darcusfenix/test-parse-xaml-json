package soy.crisostomo.app.test.activity.flickr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.view.Menu;
import android.view.MenuItem;
import soy.crisostomo.app.test.R;

public class FlickrActivity extends BaseActivity {

    private static final String LOG_TAG = "FlickrActivity";
    private List<Photo> mPhotos = new ArrayList<Photo>();
    private RecyclerView mRecyclerView;
    private FlickerRecyclerViewAdapter mFlickerRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr);

        activateToolbar();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ProcessPhotos processPhotos = new ProcessPhotos("flag,usa", true);
        processPhotos.execute();
    }
    public class ProcessPhotos extends GetFlickrJSONData{

        public ProcessPhotos(String searchCriteria, boolean matchAll) {
            super(searchCriteria, matchAll);
        }
        public void execute(){
            super.execute();
            ProcessData processData = new ProcessData();
            processData.execute();
        }
        public class  ProcessData extends DownloadJsonData{
            protected void onPostExecute(String webData){
                super.onPostExecute(webData);
                mFlickerRecyclerViewAdapter = new FlickerRecyclerViewAdapter(getmPhotos(), FlickrActivity.this);
                mRecyclerView.setAdapter(mFlickerRecyclerViewAdapter);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.menu_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mFlickerRecyclerViewAdapter != null){
            String query = getSavedPreferenceData(FLICKR_QUERY);
            if (query.length() > 0)
            {
                ProcessPhotos processPhotos = new ProcessPhotos(query, false);
                processPhotos.execute();
            }
        }
    }
    private String getSavedPreferenceData(String key){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getString(key, "");
    }

}
