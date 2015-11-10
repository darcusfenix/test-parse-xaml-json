package soy.crisostomo.app.test.activity.flickr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import soy.crisostomo.app.test.R;

public class FlickrActivity extends AppCompatActivity {

    private static final String LOG_TAG = "FlickrActivity";
    private List<Photo> mPhotos = new ArrayList<Photo>();
    private RecyclerView mRecyclerView;
    private FlickerRecyclerViewAdapter mFlickerRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr);

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
}
