package soy.crisostomo.app.test.activity.flickr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import soy.crisostomo.app.test.R;

/**
 * Created by darcusfenix on 9/11/15.
 */
public class FlickerRecyclerViewAdapter extends RecyclerView.Adapter<FlickerImageViewHolder>{
    private List<Photo> mPhotos;
    private Context mContext;
    private final String LOG_TAG = FlickerRecyclerViewAdapter.class.getSimpleName();

    public FlickerRecyclerViewAdapter(List<Photo> mPhotos, Context mContext) {
        this.mPhotos = mPhotos;
        this.mContext = mContext;
    }

    @Override
    public FlickerImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, null);
        FlickerImageViewHolder flickerImageViewHolder = new FlickerImageViewHolder(view);
        return flickerImageViewHolder;
    }

    @Override
    public void onBindViewHolder(FlickerImageViewHolder flickerImageViewHolder, int position) {
        Photo photo = mPhotos.get(position);
        Log.d(LOG_TAG," Processing: " + photo.getTitle() + " ---> " + Integer.toString(position));
        Picasso.with(mContext).load(photo.getImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder).into(flickerImageViewHolder.imageView);
        flickerImageViewHolder.textView.setText(photo.getTitle());
    }

    @Override
    public int getItemCount() {
        return (null != mPhotos ? mPhotos.size() : 0) ;
    }

    public void loadNewData(List<Photo> photos){
        mPhotos = photos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position){
        return (mPhotos != null ? mPhotos.get(position) : null);
    }
}
