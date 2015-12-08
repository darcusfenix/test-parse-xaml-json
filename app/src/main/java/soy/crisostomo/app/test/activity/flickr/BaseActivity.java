package soy.crisostomo.app.test.activity.flickr;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import soy.crisostomo.app.test.R;

/**
 * Created by darcusfenix on 12/7/15.
 */
public class BaseActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    public static final String FLICKR_QUERY = "FLICKR_QUERY";

    protected Toolbar activateToolbar(){
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.app_bar);
            if (mToolbar != null)
                setSupportActionBar(mToolbar);
        }
        return mToolbar;
    }

    protected Toolbar activateToolbarWithHomeEnable(){
        activateToolbar();
        if (mToolbar != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        return mToolbar;
    }

}