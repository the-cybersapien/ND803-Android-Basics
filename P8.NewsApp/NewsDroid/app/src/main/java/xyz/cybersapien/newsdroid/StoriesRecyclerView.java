package xyz.cybersapien.newsdroid;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ogcybersapien on 7/10/16.
 * Simple RecyclerView subclass that supports providing an empty view
 */

public class StoriesRecyclerView extends RecyclerView {

    private View emptyView;

    private AdapterDataObserver dataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            updateEmptyView();
        }
    };

    public StoriesRecyclerView(Context context) {
        super(context);
    }

    public StoriesRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StoriesRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEmptyView(View emptyView){
        this.emptyView = emptyView;
    }

    public void setAdapter(StoryAdapter adapter) {
        if (getAdapter()!=null){
            getAdapter().unregisterAdapterDataObserver(dataObserver);
        }
        if (adapter != null){
            adapter.registerAdapterDataObserver(dataObserver);
        }
        super.setAdapter(adapter);
        updateEmptyView();
    }

    private void updateEmptyView(){
        if (emptyView != null && getAdapter()!=null){
            boolean showEmptyView = getAdapter().getItemCount() == 0;
            emptyView.setVisibility((showEmptyView ? VISIBLE: GONE));
            setVisibility(showEmptyView? GONE: VISIBLE);
        }
    }
}
