package com.villagemilk.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.villagemilk.R;
import com.villagemilk.activities.BaseActivity;
import com.villagemilk.databinding.ActivityBuildingListBinding;
import com.villagemilk.viewmodel.BUildingActivityViewModel;

/**
 * Created by android on 27/12/16.
 */

public class ActivityBuildingList extends BaseActivity {

    ActivityBuildingListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_building_list);
        binding.setViewModel(new BUildingActivityViewModel(this,binding));



        setUpToolbar();


    }


    public void setUpToolbar(){

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Select your delivery location");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);



    }




}
