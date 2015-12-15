package soy.crisostomo.app.test.activity.flickr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by darcusfenix on 12/14/15.
 */
public class RecyclerItemClickListenner implements RecyclerView.OnItemTouchListener {

    public static interface OnItemClickListenner {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    private OnItemClickListenner mListenner;
    private GestureDetector mGestureDetector;

    public RecyclerItemClickListenner(Context context, final  RecyclerView recyclerView, OnItemClickListenner listenner){
        mListenner = listenner;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            public boolean onSingleTapUp(MotionEvent e){
                return true;
            }
            public void onLongPress(MotionEvent e){
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && mListenner != null)
                {
                    mListenner.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent e){
        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListenner != null && mGestureDetector.onTouchEvent(e)){
            mListenner.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
