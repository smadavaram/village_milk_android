package com.villagemilk.view;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.BaseActivity;
import com.villagemilk.activities.SelectFlatActivity;
import com.villagemilk.databinding.ActivityBillingAddressBinding;
import com.villagemilk.util.Constants;
import com.villagemilk.viewmodel.BillingAddressViewModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by android on 6/7/17.
 */


/**
 * http://139.59.33.222:8080/user/updateBillingAddress

 Parameters are following
 {
 "id" : userId,
 "zipCode" : self.enterZipCode.text,
 "billingAddress" : self.enterBillingAddress.text
 };
 */
public class ActivityBillingAddress extends BaseActivity {

    ActivityBillingAddressBinding binding;

    private String buildingId;

    private String buildingName;

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;

    private boolean isFromPaymentScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_billing_address);

        sharedPreferences = getSharedPreferences(Constants.NINJA_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(Constants.BUILDING_ID) &&
                getIntent().getExtras().containsKey(Constants.BUILDING_NAME)) {
            buildingId = getIntent().getExtras().getString(Constants.BUILDING_ID);
            buildingName = getIntent().getExtras().getString(Constants.BUILDING_NAME);
            isFromPaymentScreen = getIntent().getBooleanExtra("fromPaymentScreen",false);


        } else {
            Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        }

        if(isFromPaymentScreen){

            String billingAdress = sharedPreferences.getString("billingAddress","");
            if(!TextUtils.isEmpty(billingAdress)){
                binding.checkbox.setChecked(true);
            }

        }

        binding.setViewModel(new BillingAddressViewModel(this,buildingId,buildingName,isFromPaymentScreen));

        setUpToolbar();

    }

    public void setUpToolbar(){

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Enter your billing address");
        binding.toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);



    }



}
