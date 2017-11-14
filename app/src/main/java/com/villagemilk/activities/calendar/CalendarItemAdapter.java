package com.villagemilk.activities.calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.villagemilk.R;
import com.villagemilk.model.calendar.Subscription;

import java.util.List;

/**
 * Created by android on 3/12/16.
 */

public class CalendarItemAdapter extends RecyclerView.Adapter<CalendarItemAdapter.ViewHolder> {


    Context mContext;

    List<Subscription> subscriptions;

    public CalendarItemAdapter(Context mContext, List<Subscription> subscriptions) {
        this.mContext = mContext;
        this.subscriptions = subscriptions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.checkout_item,null,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return subscriptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);


        }
    }
}
