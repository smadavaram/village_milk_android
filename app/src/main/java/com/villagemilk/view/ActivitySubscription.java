package com.villagemilk.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.BaseActivity;
import com.villagemilk.beans.SubscriptionMaster;
import com.villagemilk.databinding.ActivitySubscriptionNewBinding;
import com.villagemilk.model.SubscriptionWrapper;
import com.villagemilk.viewmodel.SubscriptionViewModel;
import com.google.gson.Gson;

/**
 * Created by android on 4/1/17.
 */

public class ActivitySubscription extends BaseActivity {

    String TAG = "SUBSCRIPTIONS";
    ActivitySubscriptionNewBinding binding;
    SubscriptionMaster subscriptionMaster;
    SubscriptionWrapper subscriptionWrapper;
    String productMaster = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription_new);

        setUpToolbar();

        subscriptionMaster = new Gson().fromJson(getIntent().getExtras().getString("subscription_master"),SubscriptionMaster.class);
        productMaster = getIntent().getExtras().getString("product_master");

        if(!TextUtils.isEmpty(productMaster)){
            subscriptionWrapper = BaseApplication.getInstance().containsSubscriptionMaster(productMaster);
            Log.e(TAG, "onCreate: wrapper founded + "+ subscriptionWrapper.toString() );
        }


//        if(BaseApplication.getInstance().containsSubscriptionMaster(subscriptionMaster)!=null)

        setUpDeliveryDays();
        boolean isNew = TextUtils.isEmpty(productMaster);


        SubscriptionViewModel subscriptionViewModel = new SubscriptionViewModel(this,isNew,productMaster,subscriptionMaster,binding);

        binding.setViewModel(subscriptionViewModel);

    }

    public void setUpToolbar(){

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Subscription");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);



    }

    public void setUpDeliveryDays(){

       for(String day: BaseApplication.getInstance().getDeliveryDays()) {

           if(day.equals("Monday")) {
               binding.chkboxMon.setEnabled(true);

           }else if(day.equals("Tuesday")) {
               binding.chkboxTue.setEnabled(true);

           }else if(day.equals("Wednesday")) {
               binding.chkboxWed.setEnabled(true);

           }else if(day.equals("Thursday")) {
               binding.chkboxThu.setEnabled(true);

           }else if(day.equals("Friday")) {
               binding.chkboxFri.setEnabled(true);

           }else if(day.equals("Saturday")) {
               binding.chkboxSat.setEnabled(true);

           }else if(day.equals("Sunday")) {
               binding.chkboxSun.setEnabled(true);

           }

       }

       if(subscriptionWrapper!=null)
       if(subscriptionWrapper.getDeliveryDays()!=null){

           for(String day: subscriptionWrapper.getDeliveryDays()) {

               if(day.equals("Monday")) {
                   binding.chkboxMon.setChecked(true);

               }else if(day.equals("Tuesday")) {
                   binding.chkboxTue.setChecked(true);

               }else if(day.equals("Wednesday")) {
                   binding.chkboxWed.setChecked(true);

               }else if(day.equals("Thursday")) {
                   binding.chkboxThu.setChecked(true);

               }else if(day.equals("Friday")) {
                   binding.chkboxFri.setChecked(true);

               }else if(day.equals("Saturday")) {
                   binding.chkboxSat.setChecked(true);

               }else if(day.equals("Sunday")) {
                   binding.chkboxSun.setChecked(true);

               }

           }



       }

    }

}
