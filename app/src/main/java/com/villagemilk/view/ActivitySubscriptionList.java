package com.villagemilk.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villagemilk.BaseApplication;
import com.villagemilk.R;
import com.villagemilk.activities.BaseActivity;
import com.villagemilk.adapters.AdapterSubscriptionList;
import com.villagemilk.beans.SubscriptionMaster;
import com.villagemilk.databinding.ActivitySubscriptionListNewBinding;
import com.villagemilk.model.SubscriptionObject;
import com.villagemilk.model.SubscriptionWrapper;
import com.villagemilk.util.Constants;
import com.villagemilk.viewmodel.SubscriptionListViewModel;
import com.google.gson.Gson;

import java.util.Arrays;

/**
 * Created by android on 29/12/16.
 */

public class ActivitySubscriptionList extends BaseActivity {

    ActivitySubscriptionListNewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription_list_new);
        binding.setViewModel(new SubscriptionListViewModel(this));

        setUpToolbar();

//        getSubscriptions();

        Object[] objects =  BaseApplication.getInstance().getSubscriptionList().toArray();
        setUpRecyclerView(Arrays.copyOf(objects,objects.length,SubscriptionWrapper[].class));
    }



    public void setUpToolbar(){

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("My subscriptions");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);



    }


    public void setUpRecyclerView(SubscriptionWrapper[] subscriptions){

        AdapterSubscriptionList adapter = new AdapterSubscriptionList(this,subscriptions);

        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.rvSubscriptionList.setLayoutManager(manager);
        binding.rvSubscriptionList.setAdapter(adapter);


    }

/*
    private void getSubscriptions() {
        if (isNetworkAvailable()) {
            showDialog();

            String url = Constants.API_PROD + "/subscriptionMaster/listUserSubscriptions/" + BaseApplication.getInstance().getUser().getId();

            final StringRequest stringRequest = new StringRequest( url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Gson gson = new Gson();

                    if(response != null){

                        SubscriptionObject[] subscriptions = gson.fromJson(response,SubscriptionObject[].class);
                        if(subscriptions.length ==0){
                            binding.tvEmpty.setVisibility(View.VISIBLE);
                            binding.rvSubscriptionList.setVisibility(View.GONE);
                        } else
                            setUpRecyclerView(subscriptions);


                    } else {
                        Toast.makeText(ActivitySubscriptionList.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }

                    hideDialog();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    alert("Connection Error");

                    hideDialog();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            BaseApplication.getInstance().addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(ActivitySubscriptionList.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
*/

}
