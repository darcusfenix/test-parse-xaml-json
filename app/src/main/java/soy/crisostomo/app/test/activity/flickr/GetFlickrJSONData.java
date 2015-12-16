package soy.crisostomo.app.test.activity.flickr;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by darcusfenix on 7/11/15.
 */
public class GetFlickrJSONData extends GetRawData {
    private String LOG_TAG = GetFlickrJSONData.class.getName();
    private List<Photo> mPhotos;
    private Uri mDestination;

    public GetFlickrJSONData(String searchCriteria, boolean matchAll) {
        super(null);
        this.mPhotos = new ArrayList<Photo>();
        createAndUpdateUrl(searchCriteria, matchAll);
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public void execute(){
        super.setmRawUrl(mDestination.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "BUILT URI: " + mDestination.toString());
        downloadJsonData.execute(mDestination.toString());
    }

    public boolean createAndUpdateUrl(String searchCriteria, boolean matchAll) {
        final String FLICKR_API_BASE_RUL = "https://api.flickr.com/services/feeds/photos_public.gne";
        final String FLICKR_TAGS_PARAM = "tag";
        final String FLICKR_TAGMODE_PARAM = "tagmode";
        final String FLICKR_FORMAT_PARAM = "format";
        final String FLICKR_NO_JSON_CALLBACK_PRAM = "nojsoncallback";

        mDestination = Uri.parse(FLICKR_API_BASE_RUL).buildUpon()
                .appendQueryParameter(FLICKR_TAGS_PARAM, searchCriteria)
                .appendQueryParameter(FLICKR_TAGMODE_PARAM, matchAll ? "ALL" : "ANY")
                .appendQueryParameter(FLICKR_FORMAT_PARAM, "json")
                .appendQueryParameter(FLICKR_NO_JSON_CALLBACK_PRAM, "1").build();
        Log.e(LOG_TAG, mDestination.toString());
        return mDestination != null;
    }

    public void processResult() {
        if (getmDownloadStatus() != DownloadStatus.OK){
            Log.e(LOG_TAG, "ERROR DOWNLOADING RAW DATA");
            return;
        }

        final String FLICKR_ITEMS = "items";
        final String FLICKR_TITLE = "title";
        final String FLICKR_MEDIA = "media";
        final String FLICKR_PHOTO_URL = "m";
        final String FLICKR_AUTHOR = "author";
        final String FLICKR_AUTHOR_ID = "author_id";
        final String FLICKR_LINK = "link";
        final String FLICKR_TAGS = "tags";

        try {
            JSONObject jsonObject = new JSONObject(getmData());
            JSONArray jsonArray = jsonObject.getJSONArray(FLICKR_ITEMS);
            for (int i = 0; i< jsonArray.length(); i++){
                JSONObject jsonPhoto =  jsonArray.getJSONObject(i);
                String title = jsonPhoto.getString(FLICKR_TITLE);
                String author = jsonPhoto.getString(FLICKR_AUTHOR);
                String authorId = jsonPhoto.getString(FLICKR_AUTHOR_ID);
                String tags = jsonPhoto.getString(FLICKR_TAGS);

                JSONObject jsonMedia = jsonPhoto.getJSONObject(FLICKR_MEDIA);
                String urlPhoto = jsonMedia.getString(FLICKR_PHOTO_URL);

                String link = urlPhoto.replace("_m.", "_b.");

                        Photo photo = new Photo(title,author,authorId, link,tags,urlPhoto);
                this.mPhotos.add(photo);
            }
            for (Photo photo: this.mPhotos) {
                Log.v(LOG_TAG, photo.toString());
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.e(LOG_TAG, "ERROR PROCESSING JSON DATA");
        }
    }

    public class DownloadJsonData extends DownloadRawData{
        protected void onPostExecute(String webData){
            super.onPostExecute(webData);
            processResult();
        }
        protected String doInBackground(String... params){
            String[] par = {mDestination.toString()};
            return super.doInBackground(par);
        }
    }
}