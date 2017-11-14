package com.villagemilk.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.BaseActivity;
import com.villagemilk.beans.User;
import com.villagemilk.databinding.ActivityProfileBinding;

/**
 * Created by dailyninja on 10/13/17.
 */

public class ActivityUserProfile extends BaseActivity {

    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        setUpToolbar();

        User  user = BaseApplication.getInstance().getUser();
        binding.setUser(user);

        String balance = getIntent().getExtras().getString("balance");
        binding.setBalance(balance);


        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityUserProfile.this, ActivityBuildingList.class);
                startActivity(intent);


            }
        });

    }

    public void setUpToolbar(){

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        getSupportActionBar().setTitle("My Profile");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);


    }


}
