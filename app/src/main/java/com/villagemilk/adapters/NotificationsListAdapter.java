package com.villagemilk.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.villagemilk.beans.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 07-03-2016.
 */
public class NotificationsListAdapter extends BaseAdapter{
    private static final String TAG = "Notifications List Adapter";

    private Context context;
    private List<Notification> notificationList = new ArrayList<>();

    public NotificationsListAdapter(Context context, List<Notification> notificationList){
        this.context = context;
        this.notificationList = notificationList;
    }

    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){

        }

        return view;
    }

    private static class ViewHolder{

    }
}
