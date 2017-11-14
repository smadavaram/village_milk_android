package com.villagemilk.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.villagemilk.R;
import com.villagemilk.activities.AddressListActivity;
import com.villagemilk.activities.SelectFlatActivity;
import com.villagemilk.beans.Building;
import com.villagemilk.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 27-Aug-16.
 */

public class BuildingListAdapter extends RecyclerView.Adapter<BuildingListAdapter.ViewHolder> {
    private static final String TAG = "BuildingListAdapter";

    private Context context;

    private List<Building> buildingList = new ArrayList<>();

    private BuildingListAdapter buildingListAdapter;

    public BuildingListAdapter(Context context, List<Building> buildingList){
        this.context = context;
        this.buildingList = new ArrayList<>();

        for(Building building : buildingList){

            if(((AddressListActivity) context).buildingId != null && ((AddressListActivity) context).buildingId.equals(building.getId())){

                this.buildingList.add(building);
                buildingList.remove(building);
                break;

            }

        }

        this.buildingList.addAll(buildingList);

        buildingListAdapter = this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private RadioButton radioButtonBuilding;
        private TextView tvBuildingName;
        private TextView tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);

            radioButtonBuilding = (RadioButton) itemView.findViewById(R.id.radioButtonBuilding);
            tvBuildingName = (TextView) itemView.findViewById(R.id.tvBuildingName);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < buildingListAdapter.getItemCount(); i++) {
                        if (i == getAdapterPosition()){
                            radioButtonBuilding.setChecked(true);
                        } else {
                            radioButtonBuilding.setChecked(false);
                        }
                    }
                    Intent intent = new Intent(context, SelectFlatActivity.class);
                    intent.putExtra(Constants.BUILDING_ID, buildingList.get(getLayoutPosition()).getId());
                    intent.putExtra(Constants.BUILDING_NAME, buildingList.get(getLayoutPosition()).getBuildingName());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.building_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Building building = buildingList.get(position);

        holder.tvBuildingName.setText(building.getBuildingName());

        holder.tvAddress.setText(building.getAddress());

        if(((AddressListActivity) context).buildingId != null && ((AddressListActivity) context).buildingId.equals(building.getId())){
            holder.radioButtonBuilding.setChecked(true);
        } else {
            holder.radioButtonBuilding.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return buildingList.size();
    }

    public void animateTo(List<Building> filteredBuildingList) {
        applyAndAnimateRemovals(filteredBuildingList);
        applyAndAnimateAdditions(filteredBuildingList);
        applyAndAnimateMovedItems(filteredBuildingList);
    }

    private void applyAndAnimateRemovals(List<Building> filteredBuildingList) {
        for (int i = buildingList.size() - 1; i >= 0; i--) {
            final Building building = buildingList.get(i);
            if (!filteredBuildingList.contains(building)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Building> filteredBuildingList) {
        for (int i = 0, count = filteredBuildingList.size(); i < count; i++) {
            final Building building = filteredBuildingList.get(i);
            if (!buildingList.contains(building)) {
                addItem(i, building);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Building> buildingList) {
        for (int toPosition = buildingList.size() - 1; toPosition >= 0; toPosition--) {
            final Building building = buildingList.get(toPosition);
            final int fromPosition = buildingList.indexOf(building);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Building removeItem(int position) {
        final Building building = buildingList.remove(position);
        notifyItemRemoved(position);
        return building;
    }

    public void addItem(int position, Building building) {
        buildingList.add(position, building);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Building building = buildingList.remove(fromPosition);
        buildingList.add(toPosition, building);
        notifyItemMoved(fromPosition, toPosition);
    }
}
