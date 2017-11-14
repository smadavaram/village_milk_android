package com.villagemilk.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.villagemilk.R;
import com.villagemilk.beans.Building;
import com.villagemilk.databinding.BuildingListItemBinding;
import com.villagemilk.viewmodel.BuildingListViewModel;

import java.util.ArrayList;

/**
 * Created by android on 27/12/16.
 */

public class AdapterBuildingList extends RecyclerView.Adapter<AdapterBuildingList.ViewHolder> {

    Context mContext;
    ArrayList<Building> buildings;

    public AdapterBuildingList(Context mContext, ArrayList<Building> buildings) {
        this.mContext = mContext;
        this.buildings = buildings;
    }

    public void setBuildings(ArrayList buildings){

        this.buildings = buildings;
        notifyDataSetChanged();

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BuildingListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.building_list_item,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.setViewModel(mContext,buildings.get(position));


    }

    @Override
    public int getItemCount() {
        return buildings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        BuildingListItemBinding binding;

        public ViewHolder(BuildingListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setViewModel(Context context, Building building){

            if (binding.getViewModel() == null) {
                binding.setViewModel(new BuildingListViewModel(context,building));
            } else {
                binding.getViewModel().setModel(building);
            }
        }
    }
}
