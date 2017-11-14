package com.villagemilk.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import com.villagemilk.BaseApplication;
import com.villagemilk.activities.SelectFlatActivity;
import com.villagemilk.beans.Building;
import com.villagemilk.util.Constants;
import com.villagemilk.view.ActivityBillingAddress;

/**
 * Created by android on 27/12/16.
 */

public class BuildingListViewModel {

    Building building;
    Context mContext;

    public ObservableField<String> getBuildingName() {
        return buildingName;
    }

    public ObservableField<String> getAreaName() {
        return areaName;
    }

    ObservableField<String> buildingName;
    ObservableField<String> areaName;

    public ObservableBoolean showTick;

    public BuildingListViewModel(Context context, Building building) {

        buildingName = new ObservableField<>(building.getBuildingName());
        areaName = new ObservableField<>(building.getAddress());
        if(BaseApplication.getInstance().getUser() !=null) {
            if(BaseApplication.getInstance().getUser().getBuilding() != null) {
                if (BaseApplication.getInstance().getUser().getBuilding().getId().equals(building.getId())) {
                    showTick = new ObservableBoolean(true);
                } else {
                    showTick = new ObservableBoolean(false);

                }
            }
        }
        this.building = building;
        mContext = context;
        updateValues();
    }

    public void setModel(Building building){

        this.building = building;
        updateValues();

    }

    public void updateValues(){

        buildingName.set(building.getBuildingName());
        areaName.set(building.getAddress());


    }

    public void onItemClick(View view){

        Intent intent = new Intent(mContext, ActivityBillingAddress.class);
        intent.putExtra(Constants.BUILDING_ID, building.getId());
        intent.putExtra(Constants.BUILDING_NAME, building.getBuildingName());
        mContext.startActivity(intent);


    }


}
