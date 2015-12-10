package soy.crisostomo.app.test.activity.mapbox;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;
import org.json.JSONArray;
import org.json.JSONObject;
import soy.crisostomo.app.test.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MapBoxActivity extends AppCompatActivity {

    private MapView mapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_box);

        mapView = (MapView) findViewById(R.id.mapboxMapView);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);
        //mapView.setStyleUrl("https://www.mapbox.com/android-sdk/files/mapbox-raster-v8.json");
        mapView.setCenterCoordinate(new LatLng(45.5076, -122.6736));
        mapView.setZoomLevel(12);

        mapView.addMarker(new MarkerOptions()
                .position(new LatLng(48.13863, 11.57603))
                .title("Hello World!")
                .snippet("Welcome to my marker."));
        mapView.onCreate(savedInstanceState);
        new DrawGeoJSON().execute();
        drawPolygon();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_box, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause()  {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void drawPolygon() {

        ArrayList<LatLng> polygon = new ArrayList<>();

        polygon.add(new LatLng(45.522585, -122.685699));
        polygon.add(new LatLng(45.534611, -122.708873));
        polygon.add(new LatLng(45.530883, -122.678833));
        polygon.add(new LatLng(45.547115, -122.667503));
        polygon.add(new LatLng(45.530643, -122.660121));
        polygon.add(new LatLng(45.533529, -122.636260));
        polygon.add(new LatLng(45.521743, -122.659091));
        polygon.add(new LatLng(45.510677, -122.648792));
        polygon.add(new LatLng(45.515008, -122.664070));
        polygon.add(new LatLng(45.502496, -122.669048));
        polygon.add(new LatLng(45.515369, -122.678489));
        polygon.add(new LatLng(45.506346, -122.702007));
        polygon.add(new LatLng(45.522585, -122.685699));

        mapView.addPolygon(new PolygonOptions()
                .addAll(polygon)
                .fillColor(Color.parseColor("#3bb2d0")));
    }

    private class DrawGeoJSON extends AsyncTask<Void, Void, List<LatLng>> {
        @Override
        protected List<LatLng> doInBackground(Void... voids) {

            ArrayList<LatLng> points = new ArrayList<LatLng>();

            try {
                // Load GeoJSON file
                InputStream inputStream = getAssets().open("example.geojson");
                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }

                inputStream.close();

                // Parse JSON
                JSONObject json = new JSONObject(sb.toString());
                JSONArray features = json.getJSONArray("features");
                JSONObject feature = features.getJSONObject(0);
                JSONObject geometry = feature.getJSONObject("geometry");
                if (geometry != null) {
                    String type = geometry.getString("type");

                    // Our GeoJSON only has one feature: a line string
                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("LineString")) {

                        // Get the Coordinates
                        JSONArray coords = geometry.getJSONArray("coordinates");
                        for (int lc = 0; lc < coords.length(); lc++) {
                            JSONArray coord = coords.getJSONArray(lc);
                            LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                            points.add(latLng);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TAG", "Exception Loading GeoJSON: " + e.toString());
            }

            return points;
        }

        @Override
        protected void onPostExecute(List<LatLng> points) {
            super.onPostExecute(points);

            if (points.size() > 0) {
                LatLng[] pointsArray = points.toArray(new LatLng[points.size()]);

                // Draw Points on MapView
                mapView.addPolyline(new PolylineOptions()
                        .add(pointsArray)
                        .color(Color.parseColor("#3bb2d0"))
                        .width(2));
            }
        }
    }
}
