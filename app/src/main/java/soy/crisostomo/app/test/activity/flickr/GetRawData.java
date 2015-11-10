package soy.crisostomo.app.test.activity.flickr;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by darcusfenix on 7/11/15.
 */

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMTY, OK}

public class GetRawData {
    private String LOG_TAG = GetRawData.class.getName();
    private String mRawUrl;
    private String mData;
    private DownloadStatus mDownloadStatus;

    public GetRawData(String mRawUrl){
        this.mRawUrl = mRawUrl;
        this.mDownloadStatus = DownloadStatus.IDLE;
    }

    public void setmRawUrl(String mRawUrl) {
        this.mRawUrl = mRawUrl;
    }

    public void reset(){
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mData = null;
        this.mRawUrl = null;
    }

    public String getmData() {
        return mData;
    }

    public DownloadStatus getmDownloadStatus() {
        return mDownloadStatus;
    }
    public void execute(){
        this.mDownloadStatus = DownloadStatus.PROCESSING;
        DownloadRawData downloadRawData = new DownloadRawData();
        downloadRawData.execute(mRawUrl);
        Log.d(LOG_TAG, "EXECUTE BACKGROUND");
    }
    public class DownloadRawData extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            if (params == null)
                return null;
            try{
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                if (inputStream == null)
                    return null;

                StringBuffer stringBuffer = new StringBuffer();

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    stringBuffer.append(line + "\n");

                return stringBuffer.toString();

            }catch (IOException e){
                Log.e(LOG_TAG,"ERROR", e);
                return null;
            }finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
                if (bufferedReader != null)
                    try {
                        bufferedReader.close();
                    }catch (IOException e){
                        Log.e(LOG_TAG, "ERROR", e);
                    }
            }
        }

        protected void onPostExecute(String webData) {
            mData = webData;
            Log.v(LOG_TAG, "DATA RETURNED WAS: " + mData);
            if (mData == null)
                if (mRawUrl == null)
                    mDownloadStatus = DownloadStatus.NOT_INITIALISED;
                else
                    mDownloadStatus = DownloadStatus.FAILED_OR_EMTY;
            else
                mDownloadStatus = DownloadStatus.OK;
        }
    }
}
