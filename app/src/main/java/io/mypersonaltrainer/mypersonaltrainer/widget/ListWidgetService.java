package io.mypersonaltrainer.mypersonaltrainer.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import io.mypersonaltrainer.mypersonaltrainer.R;

/**
 * Created by aditya on 7/19/17.
 */

public class ListWidgetService extends RemoteViewsService{

    Context mContext;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        mContext = getApplicationContext();
        return (new ListRemoteViewsFactory(mContext));
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    Context mContext;

    public ListRemoteViewsFactory(Context c){
        this.mContext = c;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int pos) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_list_item);
        views.setTextViewText(R.id.tvWidgetItem,"widgetItem");
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
