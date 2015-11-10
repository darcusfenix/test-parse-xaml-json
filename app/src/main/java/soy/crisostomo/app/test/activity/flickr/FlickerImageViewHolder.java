package soy.crisostomo.app.test.activity.flickr;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import soy.crisostomo.app.test.R;

/**
 * Created by darcusfenix on 9/11/15.
 */
public class FlickerImageViewHolder extends RecyclerView.ViewHolder {

    protected ImageView imageView;
    protected TextView textView;

    public FlickerImageViewHolder(View itemView) {
        super(itemView);
        this.imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
        this.textView = (TextView) itemView.findViewById(R.id.title);
    }
}
