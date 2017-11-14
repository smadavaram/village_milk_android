package com.villagemilk.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.villagemilk.R;
import com.villagemilk.beans.SubscriptionMaster;
import com.villagemilk.databinding.SubsriptionListItemBinding;
import com.villagemilk.model.SubscriptionObject;
import com.villagemilk.model.SubscriptionWrapper;
import com.villagemilk.viewmodel.SubscriptionItemViewModel;

/**
 * Created by android on 29/12/16.
 */

public class AdapterSubscriptionList extends RecyclerView.Adapter<AdapterSubscriptionList.ViewHolder> {

    Context mContext;
    SubscriptionWrapper[] subscriptionObjects;


    public AdapterSubscriptionList(Context mContext, SubscriptionWrapper[] subscriptionObjects) {
        this.mContext = mContext;
        this.subscriptionObjects = subscriptionObjects;
    }

    @Override
    public AdapterSubscriptionList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        SubsriptionListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.subsription_list_item,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AdapterSubscriptionList.ViewHolder holder, int position) {

        holder.setViewModel(subscriptionObjects[position].getSubscriptionMasters().get(0));

    }

    @Override
    public int getItemCount() {
        return subscriptionObjects.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SubsriptionListItemBinding binding;

        public ViewHolder(SubsriptionListItemBinding binding) {

            super(binding.getRoot());
            this.binding = binding;

        }


        public void setViewModel(SubscriptionMaster object){

            if (binding.getViewModel() == null) {
                binding.setViewModel(new SubscriptionItemViewModel(mContext,object));
            } else {
                binding.getViewModel().setModel(object);
            }
        }


    }
}
